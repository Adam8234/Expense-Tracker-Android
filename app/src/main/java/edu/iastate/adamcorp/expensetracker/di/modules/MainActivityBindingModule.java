package edu.iastate.adamcorp.expensetracker.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import edu.iastate.adamcorp.expensetracker.ui.fragments.AddExpenseFragment;
import edu.iastate.adamcorp.expensetracker.ui.fragments.EditExpenseFragment;
import edu.iastate.adamcorp.expensetracker.ui.fragments.ExpenseCategoriesFragment;
import edu.iastate.adamcorp.expensetracker.ui.fragments.ExpenseCategoryMonthlySummaryFragment;
import edu.iastate.adamcorp.expensetracker.ui.fragments.ExpenseListFragment;
import edu.iastate.adamcorp.expensetracker.ui.fragments.ExpenseMonthlySummaryFragment;
import edu.iastate.adamcorp.expensetracker.ui.fragments.ExpenseSummaryFragment;
import edu.iastate.adamcorp.expensetracker.ui.fragments.SettingsFragment;

@Module
public abstract class MainActivityBindingModule {
    @ContributesAndroidInjector
    abstract SettingsFragment provideSettingsFragment();

    @ContributesAndroidInjector
    abstract ExpenseListFragment provideExpenseListFragment();

    @ContributesAndroidInjector
    abstract ExpenseCategoriesFragment provideExpenseCategoriesFragment();

    @ContributesAndroidInjector
    abstract AddExpenseFragment provideAddExpenseFragment();

    @ContributesAndroidInjector
    abstract EditExpenseFragment provideEditExpenseFragment();

    @ContributesAndroidInjector
    abstract ExpenseMonthlySummaryFragment provideExpenseMonthlySummaryFragment();

    @ContributesAndroidInjector
    abstract ExpenseCategoryMonthlySummaryFragment provideExpenseCategoryMonthlySummaryFragment();
}
