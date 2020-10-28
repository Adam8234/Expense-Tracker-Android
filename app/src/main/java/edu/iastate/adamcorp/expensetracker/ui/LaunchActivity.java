package edu.iastate.adamcorp.expensetracker.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class LaunchActivity extends DaggerAppCompatActivity {
    private static final String TAG = "LaunchActivity";
    @Inject
    FirebaseAuth auth;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );
    private int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (auth.getUid() == null) {
            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers).build()
                    , RC_SIGN_IN);
        }
    }
}
