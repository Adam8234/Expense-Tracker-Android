package edu.iastate.adamcorp.expensetracker.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.service.AuthenticationService;

public class LaunchActivity extends DaggerAppCompatActivity {


    @Inject
    AuthenticationService authenticationService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (authenticationService.isAuthenticated()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
    }
}
