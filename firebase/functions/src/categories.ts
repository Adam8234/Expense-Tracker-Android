import {
  DocumentSnapshot,
  QueryDocumentSnapshot,
} from "@google-cloud/firestore";
import { Change, EventContext } from "firebase-functions";
import { db } from ".";
import {
  getCategoriesCollection,
  getRootExpensesCollection,
} from "./references";

export async function handleDeleteCategory(
  snap: QueryDocumentSnapshot,
  context: EventContext
) {
  const expenses = await getRootExpensesCollection(context)
    .where("categoryId", "==", snap.id)
    .get();
  const batch = db.batch();
  for (var snapshot of expenses.docs) {
    batch.delete(snapshot.ref);
  }
  return batch.commit();
}

export async function handleUpdateCategory(
  change: Change<QueryDocumentSnapshot>,
  context: EventContext
) {
  const expensesSnapshots = await getRootExpensesCollection(context)
    .where("categoryId", "==", context.params.docId)
    .get();
  const batch = db.batch();
  for (var snapshot of expensesSnapshots.docs) {
    batch.update(snapshot.ref, { category: change.after.data() });
  }
  return batch.commit();
}

export async function handleExpenseUpdateInCategory(
  change: Change<DocumentSnapshot>,
  context: EventContext
) {
  const categoryRef = getCategoriesCollection(context).doc(
    context.params.categoryId
  );
  let categorySnap = await categoryRef.get();
  let totalAmount = categorySnap.data().totalExpenses;
  let totalExpenses = categorySnap.data().numOfExpenses;

  if (!totalExpenses) {
    totalExpenses = 0;
  }
  //Add zero value
  if (!totalAmount) {
    totalAmount = 0.0;
  }
  if (!change.before.exists) {
    totalAmount = totalAmount + change.after.data().amount;
    totalExpenses += 1;
  } else if (!change.after.exists) {
    totalAmount = totalAmount - change.before.data().amount;
    totalExpenses -= 1;
  } else {
    totalAmount =
      totalAmount + (change.after.data().amount - change.before.data().amount);
  }
  return categoryRef.update({
    totalExpenses: parseFloat(totalAmount.toFixed(2)),
    numOfExpenses: totalExpenses,
  });
}
