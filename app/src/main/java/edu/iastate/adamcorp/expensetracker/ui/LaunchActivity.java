package edu.iastate.adamcorp.expensetracker.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.service.AuthenticationService;

public class LaunchActivity extends DaggerAppCompatActivity {
    private static final String TAG = "LaunchActivity";
    private int RC_SIGN_IN = 9001;

    @Inject
    AuthenticationService authenticationService;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                // Error
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (authenticationService.isAuthenticated()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivityForResult(authenticationService.buildSignInIntent(), RC_SIGN_IN);
            return;
        }
    }
}
