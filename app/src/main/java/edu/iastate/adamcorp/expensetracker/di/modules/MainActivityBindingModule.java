package edu.iastate.adamcorp.expensetracker.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import edu.iastate.adamcorp.expensetracker.ui.fragments.ExpenseListFragment;
import edu.iastate.adamcorp.expensetracker.ui.fragments.SettingsFragment;

@Module
public abstract class MainActivityBindingModule {
    @ContributesAndroidInjector
    abstract SettingsFragment provideSettingsFragment();

    @ContributesAndroidInjector
    abstract ExpenseListFragment provideExpenseListFragment();
}
