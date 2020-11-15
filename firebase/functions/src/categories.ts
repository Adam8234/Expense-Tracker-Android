import {
  DocumentSnapshot,
  QueryDocumentSnapshot,
} from "@google-cloud/firestore";
import { Change, EventContext } from "firebase-functions";
import { db } from ".";
import { getRootExpensesCollection } from "./references";

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

export async function handleWriteCategory(
  change: Change<DocumentSnapshot>,
  context: EventContext
) {
  if (!change.before.exists) {
    //Created
  } else if (!change.after.exists) {
    //Deleted
    const expenseSnaps = await getRootExpensesCollection(context)
      .where("categoryId", "==", change.before.id)
      .get();
    let batch = db.batch();
    for (var expenseDeleteSnap of expenseSnaps.docs) {
      batch.delete(expenseDeleteSnap.ref);
    }
    return batch.commit();
  } else {
    //Updated
    const expenseSnaps = await getRootExpensesCollection(context)
      .where("categoryId", "==", change.before.id)
      .get();
    let batch = db.batch();
    for (var expenseSnap of expenseSnaps.docs) {
      batch.update(expenseSnap.ref, "category", change.after.data());
    }
    return batch.commit();
  }
  return null;
}
