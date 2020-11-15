import functions = require("firebase-functions");
import admin = require("firebase-admin");
import {
  handleDeleteCategory,
  handleMonthlyCategoryWrite,
  handleWriteCategory,
} from "./categories";
import { handleUserWrite } from "./users";
import { handleExpenseQueue, handleWriteExpense } from "./expenses";
import { handleMonthlyExpenseWrite } from "./monthly";

admin.initializeApp(functions.config().firebase);

export const db = admin.firestore();
export const usersCollection = db.collection("users");

exports.addExpense = functions.firestore
  .document("users/{userId}/expense_queue/{docId}")
  .onCreate(handleExpenseQueue);

exports.writeUser = functions.firestore
  .document("users/{userId}")
  .onWrite(handleUserWrite);

exports.writeExpense = functions.firestore
  .document("users/{userId}/expenses/{docId}")
  .onWrite(handleWriteExpense);

exports.writeCategory = functions.firestore
  .document("users/{userId}/categories/{categoryId}")
  .onWrite(handleWriteCategory);

exports.deleteCategory = functions.firestore
  .document("users/{userId}/categories/{categoryId}")
  .onDelete(handleDeleteCategory);

exports.writeMonthlyCategory = functions.firestore
  .document(
    "users/{userId}/monthly_expenses/{yearMonthId}/monthly_categories/{categoryId}"
  )
  .onWrite(handleMonthlyCategoryWrite);

exports.writeMonthlyExpense = functions.firestore
  .document("users/{userId}/monthly_expenses/{yearMonthId}")
  .onWrite(handleMonthlyExpenseWrite);
