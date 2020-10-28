package edu.iastate.adamcorp.expensetracker;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import edu.iastate.adamcorp.expensetracker.di.ApplicationComponent;
import edu.iastate.adamcorp.expensetracker.di.DaggerApplicationComponent;

public class ExpenseTrackerApp extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        ApplicationComponent component = DaggerApplicationComponent
                .builder()
                .application(this)
                .build();
        component.inject(this);
        return component;
    }
}
