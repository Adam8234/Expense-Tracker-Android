package edu.iastate.adamcorp.expensetracker.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.iastate.adamcorp.expensetracker.service.AuthenticationService;

@Singleton
public class UserRepository {
    private FirebaseFirestore firebaseFirestore;
    private AuthenticationService authenticationService;
    private FirebaseAuth firebaseAuth;

    @Inject
    public UserRepository(FirebaseFirestore firebaseFirestore, AuthenticationService authenticationService) {
        this.firebaseFirestore = firebaseFirestore;
        this.authenticationService = authenticationService;
        this.firebaseAuth = firebaseAuth;
    }

    public DocumentReference getUserDocument() {
        return firebaseFirestore.collection("users")
                .document(authenticationService.getUserId());
    }
}
