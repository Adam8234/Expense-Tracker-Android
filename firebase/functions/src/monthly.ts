import { DocumentSnapshot } from "@google-cloud/firestore";
import { Change, EventContext } from "firebase-functions";
import { getUserReference } from "./references";

export async function handleMonthlyExpenseWrite(
  change: Change<DocumentSnapshot>,
  context: EventContext
) {
  if (!change.before.exists) {
    //Create
    const userSnap = await getUserReference(context).get();
    return change.after.ref.update({
      yearMonthId: change.after.id,
      monthlyBudget: userSnap.data()?.monthlyBudget,
      symbol: userSnap.data()?.symbol,
    });
  } else if (!change.after.exists) {
    //Delete
  } else if (change.after.data()?.totalExpenses <= 0) {
    //Update
    return change.after.ref.delete();
  }
  return null;
}
