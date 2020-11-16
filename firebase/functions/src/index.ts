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
import { Timestamp } from "@google-cloud/firestore";

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

exports.billReminder = functions.pubsub
  .schedule("0 9 * * *")
  .onRun(async (context) => {
    const date = Timestamp.now().toDate();
    const day = date.getDate();
    const queryDate = new Date(date);
    queryDate.setHours(0, 0, 0, 0);
    console.log(`Query Day: ${day}`);
    const userSnapshots = await usersCollection
      .where("dayReminder", "==", day)
      .get();

    for (const userSnapshot of userSnapshots.docs) {
      const token = userSnapshot.data().fcmToken;
      if (token) {
        console.log(`Sending notification to user: ${userSnapshot.id}`);
        await admin.messaging().sendToDevice(token, {
          notification: {
            title: "Bill Reminder",
            body: "Your bills are due soon. Add your bill expenses to the app.",
          },
        });
        await userSnapshot.ref.update({ lastSent: Timestamp.now() });
      }
    }
  });
