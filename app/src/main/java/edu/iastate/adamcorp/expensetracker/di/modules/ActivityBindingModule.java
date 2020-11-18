package edu.iastate.adamcorp.expensetracker.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import edu.iastate.adamcorp.expensetracker.ui.LaunchActivity;
import edu.iastate.adamcorp.expensetracker.ui.MainActivity;
import edu.iastate.adamcorp.expensetracker.ui.SignInActivity;

@Module(includes = ViewModelModule.class)
public abstract class ActivityBindingModule {
    @ContributesAndroidInjector
    abstract LaunchActivity bindLaunchActivity();

    @ContributesAndroidInjector(modules = MainActivityBindingModule.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract SignInActivity bindSignInActivity();
}
