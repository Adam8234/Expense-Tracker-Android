import { DocumentSnapshot } from "@google-cloud/firestore";
import { Change, EventContext } from "firebase-functions";
import { db } from ".";
import { getMonthlyExpensesCollection } from "./references";

export async function handleUserWrite(
  change: Change<DocumentSnapshot>,
  context: EventContext
) {
  const userRef = change.after.ref;
  const userData = change.after.data();

  let symbol = "$";
  if (userData && userData.symbol) {
    symbol = userData.symbol;
  }

  let expensesRefs = await userRef.collection("expenses").listDocuments();

  const batch = db.batch();
  // Update denormalized data for symbol
  for (var ref of expensesRefs) {
    batch.update(ref, "symbol", symbol);
  }

  let monthlyRefs = await getMonthlyExpensesCollection(context).listDocuments();
  for (var monthlyRef of monthlyRefs) {
    batch.update(monthlyRef, "symbol", symbol);
    batch.update(
      monthlyRef,
      "monthlyBudget",
      change.after.data()?.monthlyBudget
    );
  }

  return batch.commit();
}
