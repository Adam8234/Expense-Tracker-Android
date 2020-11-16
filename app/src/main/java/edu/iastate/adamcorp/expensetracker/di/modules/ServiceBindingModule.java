package edu.iastate.adamcorp.expensetracker.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import edu.iastate.adamcorp.expensetracker.MyFirebaseMessagingService;

@Module
public abstract class ServiceBindingModule {
    @ContributesAndroidInjector
    abstract MyFirebaseMessagingService bindMyFirebaseMessagingService();
}
