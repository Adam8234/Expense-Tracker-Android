package edu.iastate.adamcorp.expensetracker.data;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ExpensesRepository implements FirebaseAuth.AuthStateListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Inject
    public ExpensesRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
        firebaseAuth.addAuthStateListener(this);
    }

    public Query getExpenses() {
        return getUserDocument().collection("expenses");
    }

    public DocumentReference getUserDocument() {
        return firebaseFirestore.collection("users").document(getAuthOrThrow());
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
