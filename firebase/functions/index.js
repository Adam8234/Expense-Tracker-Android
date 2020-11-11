const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp(functions.config().firebase);

const db = admin.firestore();
const usersCollection = db.collection("users");

exports.addExpense = functions.firestore
  .document("users/{userId}/expense_queue/{docId}")
  .onCreate(async (snap, context) => {
    let data = snap.data();
    let userRef = usersCollection.doc(context.params.userId);
    let expensesRef = userRef.collection("expenses");
    let userCategoriesRef = userRef.collection("categories");

    let userSnapshot = await userRef.get();
    let categorySnapshot = await userCategoriesRef.doc(data.categoryId).get();

    //Denormalize category
    data.category = categorySnapshot.data();

    //Denormalize symbol
    let userData = userSnapshot.data();
    if (userData && userData.symbol) {
      data.symbol = userData.symbol;
    } else {
      data.symbol = "$";
    }

    expensesRef.add(data);

    snap.ref.delete();
  });

exports.writeUser = functions.firestore
  .document("users/{userId}")
  .onWrite(async (change, context) => {
    const userRef = change.after.ref;
    const userData = change.after.data();

    let symbol = "$";
    if (userData && userData.symbol) {
      symbol = userData.symbol;
    }

    let expensesSnapshot = await userRef.collection("expenses").get();
    const batch = db.batch();
    // Update denormalized data for symbol
    for (var snapshot of expensesSnapshot.docs) {
      let data = snapshot.data();
      data.symbol = symbol;
      batch.update(snapshot.ref, data);
    }
    return batch.commit();
  });
