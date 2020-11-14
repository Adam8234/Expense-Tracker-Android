import { EventContext } from "firebase-functions";
import { usersCollection } from "./index";

export function getUserReference(context: EventContext) {
  return usersCollection.doc(context.params.userId);
}

export function getCategoriesCollection(context: EventContext) {
  return getUserReference(context).collection("categories");
}

export function getMonthlyExpensesCollection(context: EventContext) {
  return getUserReference(context).collection("monthly_expenses");
}

export function getRootExpensesCollection(context: EventContext) {
  return getUserReference(context).collection("expenses");
}
