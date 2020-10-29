package edu.iastate.adamcorp.expensetracker.service;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthenticationService {
    private Context context;

    @Inject
    public AuthenticationService(Context context) {
        this.context = context;
    }

}
