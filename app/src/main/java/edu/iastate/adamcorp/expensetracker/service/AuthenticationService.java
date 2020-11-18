package edu.iastate.adamcorp.expensetracker.service;

import android.content.Context;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthenticationService {
    private Context context;
    private FirebaseAuth firebaseAuth;

    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    @Inject
    public AuthenticationService(Context context, FirebaseAuth firebaseAuth) {
        this.context = context;
        this.firebaseAuth = firebaseAuth;
    }

    public Intent buildSignInIntent() {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false, false)
                .setAvailableProviders(getProviders()).build();
    }


    public boolean isAuthenticated() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public String getUserId() {
        return firebaseAuth.getUid();
    }

    public List<AuthUI.IdpConfig> getProviders() {
        return providers;
    }

    public void signOut() {
        firebaseAuth.signOut();
    }
}
