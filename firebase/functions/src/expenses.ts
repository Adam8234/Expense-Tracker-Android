import {
  CollectionReference,
  DocumentReference,
  DocumentSnapshot,
  QueryDocumentSnapshot,
  WriteResult,
} from "@google-cloud/firestore";
import { Change, EventContext } from "firebase-functions";
import {
  getCategoriesCollection,
  getMonthlyExpensesCollection,
  getRootExpensesCollection,
  getUserReference,
} from "./references";

export function handleDeleteExpense(
  snap: DocumentSnapshot,
  context: EventContext
) {
  let results: Promise<WriteResult>[] = [];
  results.push(
    getCategoriesCollection(context)
      .doc(snap.data().categoryId)
      .collection("expenses")
      .doc(snap.id)
      .delete()
  );
  results.push(
    getMonthlyExpensesCollection(context)
      .doc(getMonthYearKey(snap))
      .collection("expenses")
      .doc(snap.id)
      .delete()
  );

  return Promise.all(results);
}

export async function handleExpenseQueue(
  snap: DocumentSnapshot,
  context: EventContext
) {
  let data = snap.data();

  //Denormalize symbol
  let userSnap = await getUserReference(context).get();
  let symbol = userSnap.data().symbol;
  if (!symbol) {
    symbol = "$";
    await userSnap.ref.update("symbol", "$");
  }
  data.symbol = symbol;

  const categorySnap = await getCategoriesCollection(context)
    .doc(getCategoryId(snap))
    .get();
  data.category = categorySnap.data();

  return Promise.all([
    getRootExpensesCollection(context).add(data),
    snap.ref.delete(),
  ]);
}

export async function handleWriteExpense(
  change: Change<QueryDocumentSnapshot>,
  context: EventContext
) {
  if (!change.after.exists) {
    return null;
  }
  const collections: DocumentReference[] = [];
  const categoriesCollectionRef = getCategoriesCollection(context);
  const monthlyCollectionRef = getMonthlyExpensesCollection(context);
  collections.push(categoriesCollectionRef.doc(getCategoryId(change.after)));
  collections.push(monthlyCollectionRef.doc(getMonthYearKey(change.after)));

  const ref = change.after.ref;
  console.log("Denormalizing Category");
  await denormalizeDocumentChange(
    categoriesCollectionRef,
    getCategoryId(change.before),
    getCategoryId(change.after),
    change.after,
    "category"
  );

  console.log("Denormalizing Month Year");
  await denormalizeDocumentChange(
    monthlyCollectionRef,
    getMonthYearKey(change.before),
    getMonthYearKey(change.after),
    change.after,
    null
  );

  const docSnap = await ref.get();
  const writeResults = collections.map((value) => {
    return value.collection("expenses").doc(ref.id).set(docSnap.data());
  });
  return Promise.all(writeResults);
}

async function denormalizeDocumentChange(
  collectionRef: CollectionReference,
  beforeId: string,
  afterId: string,
  expense: DocumentSnapshot,
  field?: string
) {
  if (beforeId !== afterId) {
    let afterSnap = await collectionRef.doc(afterId).get();

    if (beforeId) {
      await collectionRef
        .doc(beforeId)
        .collection("expenses")
        .doc(expense.id)
        .delete();
    }

    if (field) {
      await expense.ref.update(field, afterSnap.data());
    }
  }
}

function getMonthYearKey(snap: DocumentSnapshot) {
  if (!snap || !snap.data()) {
    return null;
  }
  const date = snap.data().date.toDate();
  return date.getFullYear() + "-" + date.getMonth();
}

function getCategoryId(snap: DocumentSnapshot) {
  if (!snap || !snap.data()) {
    return null;
  }
  return snap.data().categoryId;
}
