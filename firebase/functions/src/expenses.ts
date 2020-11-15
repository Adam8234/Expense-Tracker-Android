import {
  CollectionReference,
  DocumentReference,
  DocumentSnapshot,
  FieldValue,
  WriteBatch,
} from "@google-cloud/firestore";
import { Change, EventContext } from "firebase-functions";
import { db } from ".";
import {
  getCategoriesCollection,
  getMonthlyExpensesCollection,
  getRootExpensesCollection,
  getUserReference,
} from "./references";
export async function handleExpenseQueue(
  snap: DocumentSnapshot,
  context: EventContext
) {
  let data = snap.data()!!;

  //Denormalize symbol
  let userSnap = await getUserReference(context).get();
  let symbol = userSnap.data()?.symbol;
  if (!symbol) {
    symbol = "$";
    if (!userSnap.data()) {
      await userSnap.ref.set({ symbol: symbol });
    } else {
      await userSnap.ref.update("symbol", symbol);
    }
  }
  data.symbol = symbol;

  const categorySnap = await getCategoriesCollection(context)
    .doc(getCategoryId(snap))
    .get();

  data.category = categorySnap.data();
  data.yearMonthId = getMonthYearKey(snap);

  return Promise.all([
    getRootExpensesCollection(context).doc(snap.id).set(data),
    snap.ref.delete(),
    ,
  ]);
}

export async function handleWriteExpense(
  change: Change<DocumentSnapshot>,
  context: EventContext
) {
  const batch = db.batch();
  const expenseRef = change.after.ref;
  const categoriesCollectionRef = getCategoriesCollection(context);
  const monthlyCollectionRef = getMonthlyExpensesCollection(context);

  await updateTotalsInDocCollection(
    categoriesCollectionRef,
    getCategoryId(change.before),
    getCategoryId(change.after),
    change,
    batch,
    "category"
  );
  await updateTotalsInDocCollection(
    monthlyCollectionRef,
    getMonthYearKey(change.before),
    getMonthYearKey(change.after),
    change,
    batch
  );

  await updateTotalsInDoc(
    monthlyCollectionRef
      .doc(getMonthYearKey(change.before))
      .collection("monthly_categories")
      .doc(getCategoryId(change.before)),
    monthlyCollectionRef
      .doc(getMonthYearKey(change.after))
      .collection("monthly_categories")
      .doc(getCategoryId(change.after)),
    change,
    batch,
    undefined
  );

  if (change.after.exists) {
    batch.update(expenseRef, { yearMonthId: getMonthYearKey(change.after) });
  }

  return batch.commit();
}

async function updateTotalsInDocCollection(
  collectionRef: CollectionReference,
  beforeId: string,
  afterId: string,
  change: Change<DocumentSnapshot>,
  batch: WriteBatch,
  field?: string
) {
  return await updateTotalsInDoc(
    collectionRef.doc(beforeId),
    collectionRef.doc(afterId),
    change,
    batch,
    field
  );
}

async function updateTotalsInDoc(
  beforeRef: DocumentReference,
  afterRef: DocumentReference,
  change: Change<DocumentSnapshot>,
  batch: WriteBatch,
  field?: string
) {
  const afterAmount: number = change.after.data()?.amount;
  const beforeAmount: number = change.before.data()?.amount;

  if (!change.before.exists) {
    //Created
    batch.set(
      afterRef,
      {
        totalAmount: FieldValue.increment(afterAmount),
        totalExpenses: FieldValue.increment(1),
      },
      { merge: true }
    );
    return;
  } else if (!change.after.exists) {
    //Deleted
    if ((await beforeRef.get()).exists) {
      batch.set(
        beforeRef,
        {
          totalAmount: FieldValue.increment(-beforeAmount),
          totalExpenses: FieldValue.increment(-1),
        },
        { merge: true }
      );
    }
    return;
  } else if (beforeRef.isEqual(afterRef)) {
    //Update Amount
    batch.set(
      afterRef,
      {
        totalAmount: FieldValue.increment(afterAmount - beforeAmount),
      },
      { merge: true }
    );
  } else {
    //Changed ID
    batch.set(
      beforeRef,
      {
        totalAmount: FieldValue.increment(-beforeAmount),
        totalExpenses: FieldValue.increment(-1),
      },
      { merge: true }
    );
    batch.set(
      afterRef,
      {
        totalAmount: FieldValue.increment(afterAmount),
        totalExpenses: FieldValue.increment(1),
      },
      { merge: true }
    );
  }
  if (field && change.after.exists) {
    const afterData = await afterRef.get();
    batch.update(change.after.ref, field, afterData.data());
  }
}

function getMonthYearKey(snap?: DocumentSnapshot): string {
  const date = snap?.data()?.date.toDate();
  if (date) {
    return date.getFullYear() + "-" + (date.getMonth() + 1);
  }
  return "none";
}

function getCategoryId(snap?: DocumentSnapshot): string {
  const categoryId = snap?.data()?.categoryId;
  if (!categoryId) {
    return "none";
  }
  return categoryId;
}
