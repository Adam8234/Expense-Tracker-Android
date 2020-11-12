const functions = require("firebase-functions");
const admin = require("firebase-admin");
const { user } = require("firebase-functions/lib/providers/auth");

admin.initializeApp(functions.config().firebase);

const db = admin.firestore();
const usersCollection = db.collection("users");

/**
 * Whenever an expense is added, denormalize user data and cateorial data to make it easier on the client
 */
exports.addExpense = functions.firestore
  .document("users/{userId}/expense_queue/{docId}")
  .onCreate(async (snap, context) => {
    let data = snap.data();
    const userRef = usersCollection.doc(context.params.userId);
    const expensesRef = userRef.collection("expenses");
    const userCategoriesRef = userRef.collection("categories");

    const userSnapshot = await userRef.get();
    const categorySnapshot = await userCategoriesRef.doc(data.categoryId).get();

    //Denormalize category
    data.category = categorySnapshot.data();

    //Denormalize symbol
    let userData = userSnapshot.data();
    if (userData && userData.symbol) {
      data.symbol = userData.symbol;
    } else {
      data.symbol = "$";
    }

    const expense = await expensesRef.add(data);
    categorySnapshot.ref.collection("expenses").doc(expense.id).set(data);

    snap.ref.delete();
  });

exports.updateExpense = functions.firestore
  .document("users/{userId}/expenses/{docId}")
  .onUpdate(async (change, context) => {
    const userRef = usersCollection.doc(context.params.userId);
    const categoriesRef = userRef.collection("categories");
    const docRef = change.after.ref;
    //Update category
    if (change.before.data().categoryId !== change.after.data().categoryId) {
      //Delete old expense in category
      categoriesRef
        .doc(change.before.data().categoryId)
        .collection("expenses")
        .doc(context.params.docId)
        .delete();

      const categoryRef = categoriesRef.doc(change.after.data().categoryId);
      const categorySnap = await categoryRef.get();

      await docRef.update({ category: categorySnap.data() });
    }

    const docSnap = await docRef.get();
    return categoriesRef
      .doc(change.after.data().categoryId)
      .collection("expenses")
      .doc(context.params.docId)
      .set(docSnap.data());
  });

exports.deleteExpense = functions.firestore
  .document("users/{userId}/expenses/{docId}")
  .onDelete((snap, context) => {
    const userRef = usersCollection.doc(context.params.userId);
    return userRef
      .collection("categories")
      .doc(snap.data().categoryId)
      .collection("expenses")
      .doc(context.params.docId)
      .delete();
  });

exports.writeExpenseInCategory = functions.firestore
  .document("users/{userId}/categories/{categoryId}/expenses/{docId}")
  .onWrite(async (change, context) => {
    const categoryRef = usersCollection
      .doc(context.params.userId)
      .collection("categories")
      .doc(context.params.categoryId);
    let categorySnap = await categoryRef.get();
    let totalAmount = categorySnap.data().totalExpenses;
    let totalExpenses = categorySnap.data().numOfExpenses;
    if (!totalExpenses) {
      totalExpenses = 0;
    }
    //Add zero value
    if (!totalAmount) {
      totalAmount = 0.0;
    }
    if (!change.before.exists) {
      totalAmount = totalAmount + change.after.data().amount;
      totalExpenses += 1;
    } else if (!change.after.exists) {
      totalAmount = totalAmount - change.before.data().amount;
      totalExpenses -= 1;
    } else {
      totalAmount =
        totalAmount +
        (change.after.data().amount - change.before.data().amount);
    }
    return categoryRef.update({
      totalExpenses: totalAmount.toFixed(2),
      numOfExpenses: totalExpenses,
    });
  });

/**
 * Whenever a user changes, we need to update our denormalized data
 */
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

/**
 * Update denormalized data for category in expenses
 */
exports.updateCategory = functions.firestore
  .document("users/{userId}/categories/{docId}")
  .onUpdate(async (change, context) => {
    const expensesSnapshots = await usersCollection
      .doc(context.params.userId)
      .collection("expenses")
      .where("categoryId", "==", context.params.docId)
      .get();
    const batch = db.batch();
    for (var snapshot of expensesSnapshots.docs) {
      batch.update(snapshot.ref, { category: change.after.data() });
    }
    batch.commit();
  });

/**
 * Delete expenses if we delete category
 */
exports.deleteCategory = functions.firestore
  .document("users/{userId}/categories/{docId}")
  .onDelete(async (snap, context) => {
    const id = snap.id;
    const expenses = await usersCollection
      .doc(context.params.userId)
      .collection("expenses")
      .where("categoryId", "==", id)
      .get();
    const batch = db.batch();
    for (var snapshot of expenses.docs) {
      batch.delete(snapshot.ref);
    }
    batch.commit();
  });
