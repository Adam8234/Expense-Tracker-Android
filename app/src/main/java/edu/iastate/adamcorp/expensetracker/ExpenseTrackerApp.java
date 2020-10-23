package edu.iastate.adamcorp.expensetracker;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class ExpenseTrackerApp extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return null;
    }
}
