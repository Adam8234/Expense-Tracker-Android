package edu.iastate.adamcorp.expensetracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.IdpResponse;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.service.AuthenticationService;

public class SignInActivity extends DaggerAppCompatActivity {
    private static final String TAG = "LaunchActivity";
    @Inject
    AuthenticationService authenticationService;
    private int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(authenticationService.buildSignInIntent(), RC_SIGN_IN);
            }
        });
    }

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
}
