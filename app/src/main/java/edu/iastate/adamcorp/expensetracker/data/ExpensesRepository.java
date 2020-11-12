package edu.iastate.adamcorp.expensetracker.data;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.iastate.adamcorp.expensetracker.data.models.Expense;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseQueue;

@Singleton
public class ExpensesRepository implements FirebaseAuth.AuthStateListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private UserRepository userRepository;

    @Inject
    public ExpensesRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore, UserRepository userRepository) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
        this.userRepository = userRepository;
        firebaseAuth.addAuthStateListener(this);

//        for (int i = 0; i < 15; i++) {
//            addExpense(new ExpenseQueue("Test", "pS5wbl39sesS1OniyaSp", Math.random() * 500 + 20, Timestamp.now()));
//        }
    }

    public Query getExpenses() {
        return userRepository.getUserDocument().collection("expenses").orderBy("date", Query.Direction.DESCENDING);
    }

    public DocumentReference getExpense(String expenseId) {
        return userRepository.getUserDocument().collection("expenses").document(expenseId);
    }

    public CollectionReference getExpenseQueue() {
        return userRepository.getUserDocument().collection("expense_queue");
    }

    public Task<Void> updateExpense(String id, Expense expense) {
        return getExpense(id).set(expense);
    }

    public DocumentReference addExpense(ExpenseQueue expense) {
        DocumentReference doc = getExpenseQueue().document();
        doc.set(expense);
        return doc;
    }

    public String getAuthOrThrow() {
        String uid = this.firebaseAuth.getUid();
        if (uid == null) {
            throw new IllegalStateException("You must be signed in");
        }
        return uid;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
    }
}
