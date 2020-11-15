import {
  CollectionReference,
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

  await updateTotalsInDoc(
    categoriesCollectionRef,
    getCategoryId(change.before),
    getCategoryId(change.after),
    change,
    batch,
    "category"
  );
  await updateTotalsInDoc(
    monthlyCollectionRef,
    getMonthYearKey(change.before),
    getMonthYearKey(change.after),
    change,
    batch
  );
  batch.update(expenseRef, { yearMonthId: getMonthYearKey(change.after) });
  return batch.commit();
}

async function updateTotalsInDoc(
  collectionRef: CollectionReference,
  beforeId: string | undefined,
  afterId: string | undefined,
  change: Change<DocumentSnapshot>,
  batch: WriteBatch,
  field?: string
) {
  const afterAmount: number = change.after.data()?.amount;
  const beforeAmount: number = change.before.data()?.amount;

  if (!change.before.exists) {
    //Created
    batch.set(
      collectionRef.doc(afterId!!),
      {
        totalAmount: FieldValue.increment(afterAmount),
        totalExpenses: FieldValue.increment(1),
      },
      { merge: true }
    );
    return;
  } else if (!change.after.exists) {
    //Deleted
    batch.update(collectionRef.doc(beforeId!!), {
      totalAmount: FieldValue.increment(-beforeAmount),
      totalExpenses: FieldValue.increment(-1),
    });
    return;
  } else if (beforeId == afterId) {
    //Update Amount
    batch.update(collectionRef.doc(afterId!!), {
      totalAmount: FieldValue.increment(afterAmount - beforeAmount),
    });
  } else {
    //Changed ID
    batch.update(collectionRef.doc(beforeId!!), {
      totalAmount: FieldValue.increment(-beforeAmount),
      totalExpenses: FieldValue.increment(-1),
    });
    batch.set(
      collectionRef.doc(afterId!!),
      {
        totalAmount: FieldValue.increment(afterAmount),
        totalExpenses: FieldValue.increment(1),
      },
      { merge: true }
    );
    if (field) {
      const afterData = await collectionRef.doc(afterId!!).get();
      batch.update(change.after.ref, field, afterData.data());
    }
  }
}

function getMonthYearKey(snap?: DocumentSnapshot) {
  const date = snap?.data()?.date.toDate();
  if (date) {
    return date.getFullYear() + "-" + (date.getMonth() + 1);
  }
  return undefined;
}

function getCategoryId(snap?: DocumentSnapshot) {
  return snap?.data()?.categoryId;
}
