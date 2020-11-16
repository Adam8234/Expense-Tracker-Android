package edu.iastate.adamcorp.expensetracker.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

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
    public UserRepository(FirebaseFirestore firebaseFirestore, AuthenticationService authenticationService, FirebaseAuth firebaseAuth, FirebaseMessaging firebaseMessaging) {
        this.firebaseFirestore = firebaseFirestore;
        this.authenticationService = authenticationService;
        this.firebaseAuth = firebaseAuth;
        firebaseAuth.addAuthStateListener(auth -> {
            if(firebaseAuth.getUid() != null) {
                firebaseMessaging.getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        updateFCMToken(s);
                    }
                });
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
    public void changeDayReminder(Integer budget) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("dayReminder", budget);
        getUserDocument().set(map, SetOptions.merge());
    }

    public void updateFCMToken(String s) {
        if (!authenticationService.isAuthenticated()) {
            return;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("fcmToken", s);
        getUserDocument().set(map, SetOptions.merge());
    }
}
