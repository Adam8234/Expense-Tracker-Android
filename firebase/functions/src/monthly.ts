import { DocumentSnapshot } from "@google-cloud/firestore";
import { Change, EventContext } from "firebase-functions";

export async function handleMonthlyExpenseWrite(
  change: Change<DocumentSnapshot>,
  context: EventContext
) {
  if (!change.before.exists) {
    //Create
  } else if (!change.after.exists) {
    //Delete
  } else if (change.after.data()?.totalExpenses <= 0) {
    //Update
    return change.after.ref.delete();
  }
  return null;
}
