package edu.iastate.adamcorp.expensetracker.data;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.iastate.adamcorp.expensetracker.data.models.Expense;

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
    }

    public CollectionReference getExpenses() {
        return userRepository.getUserDocument().collection("expenses");
    }

    public DocumentReference addExpense(Expense expense) {
        DocumentReference doc = getExpenses().document();
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
