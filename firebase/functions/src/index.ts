import functions = require("firebase-functions");
import admin = require("firebase-admin");
import { handleDeleteCategory, handleWriteCategory } from "./categories";
import { handleUserWrite } from "./users";
import { handleExpenseQueue, handleWriteExpense } from "./expenses";

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
