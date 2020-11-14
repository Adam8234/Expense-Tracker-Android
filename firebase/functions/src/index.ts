import functions = require("firebase-functions");
import admin = require("firebase-admin");
import {
  handleDeleteCategory,
  handleExpenseUpdateInCategory,
  handleUpdateCategory,
} from "./categories";
import { handleUserWrite } from "./users";
import {
  handleDeleteExpense,
  handleExpenseQueue,
  handleWriteExpense,
} from "./expenses";

admin.initializeApp(functions.config().firebase);

export const db = admin.firestore();
export const usersCollection = db.collection("users");

/**
 * Whenever an expense is added, denormalize user data and cateorial data to make it easier on the client
 */
exports.addExpense = functions.firestore
  .document("users/{userId}/expense_queue/{docId}")
  .onCreate(handleExpenseQueue);

exports.writeExpense = functions.firestore
  .document("users/{userId}/expenses/{expenseId}")
  .onWrite(handleWriteExpense);

exports.deleteExpense = functions.firestore
  .document("users/{userId}/expenses/{docId}")
  .onDelete(handleDeleteExpense);

exports.writeExpenseInCategory = functions.firestore
  .document("users/{userId}/categories/{categoryId}/expenses/{expenseId}")
  .onWrite(handleExpenseUpdateInCategory);

exports.writeUser = functions.firestore
  .document("users/{userId}")
  .onWrite(handleUserWrite);

exports.updateCategory = functions.firestore
  .document("users/{userId}/categories/{categoryId}")
  .onUpdate(handleUpdateCategory);

exports.deleteCategory = functions.firestore
  .document("users/{userId}/categories/{categoryId}")
  .onDelete(handleDeleteCategory);
