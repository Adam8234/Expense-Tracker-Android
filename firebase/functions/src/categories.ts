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
    const monthlyCategoriesSnaps = await db
      .collectionGroup("monthly_categories")
      .where("categoryId", "==", change.after.id)
      .get();

    let batch = db.batch();
    for (const monthlyCategory of monthlyCategoriesSnaps.docs) {
      batch.delete(monthlyCategory.ref);
    }

    for (var expenseDeleteSnap of expenseSnaps.docs) {
      batch.delete(expenseDeleteSnap.ref);
    }
    return batch.commit();
  } else {
    //Updated
    const expenseSnaps = await getRootExpensesCollection(context)
      .where("categoryId", "==", change.before.id)
      .get();
    const monthlyCategoriesSnaps = await db
      .collectionGroup("monthly_categories")
      .where("categoryId", "==", change.after.id)
      .get();
    let batch = db.batch();
    for (const monthlyCategory of monthlyCategoriesSnaps.docs) {
      batch.set(
        monthlyCategory.ref,
        {
          name: change.after.data()?.name,
        },
        { merge: true }
      );
    }
    for (var expenseSnap of expenseSnaps.docs) {
      batch.update(expenseSnap.ref, "category", change.after.data());
    }
    return batch.commit();
  }
  return null;
}

export async function handleMonthlyCategoryWrite(
  change: Change<DocumentSnapshot>,
  context: EventContext
) {
  if (!change.before.exists) {
    //Create
    const categorySnap = await getCategoriesCollection(context)
      .doc(change.after.id)
      .get();
    await change.after.ref.set(
      { categoryId: change.after.id },
      { merge: true }
    );
    return change.after.ref.set(categorySnap.data()!!, { merge: true });
  } else if (!change.after.exists) {
    //Delete
  } else if (change.after.data()?.totalExpenses <= 0) {
    //Update
    return change.after.ref.delete();
  }
  return null;
}
