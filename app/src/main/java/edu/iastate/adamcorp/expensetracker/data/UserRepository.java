package edu.iastate.adamcorp.expensetracker.data;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.iastate.adamcorp.expensetracker.data.models.User;
import edu.iastate.adamcorp.expensetracker.service.AuthenticationService;

@Singleton
public class UserRepository {
    private FirebaseFirestore firebaseFirestore;
    private AuthenticationService authenticationService;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "UserRepository";

    @Inject
    public UserRepository(FirebaseFirestore firebaseFirestore, AuthenticationService authenticationService) {
        this.firebaseFirestore = firebaseFirestore;
        this.authenticationService = authenticationService;
        this.firebaseAuth = firebaseAuth;
        firebaseFirestore.collectionGroup("monthly_categories").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                Log.d(TAG, "onSuccess() called with: queryDocumentSnapshots = [" + queryDocumentSnapshots + "]");
            }
        });
    }

    public DocumentReference getUserDocument() {
        return firebaseFirestore.collection("users")
                .document(authenticationService.getUserId());
    }

    public void changeCurrencySymbol(String currency) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("symbol", currency);
        getUserDocument().set(map, SetOptions.merge());
    }

    public void changeMonthlyBudget(Double budget) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("monthlyBudget", budget);
        getUserDocument().set(map, SetOptions.merge());
    }
}
