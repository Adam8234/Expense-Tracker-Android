package edu.iastate.adamcorp.expensetracker.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import edu.iastate.adamcorp.expensetracker.di.ViewModelFactory;
import edu.iastate.adamcorp.expensetracker.di.ViewModelKey;
import edu.iastate.adamcorp.expensetracker.ui.viewmodel.ExpenseCategoryViewModel;
import edu.iastate.adamcorp.expensetracker.ui.viewmodel.ExpenseListViewModel;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ExpenseCategoryViewModel.class)
    abstract ViewModel bindExpenseCategoryViewModel(ExpenseCategoryViewModel postListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseListViewModel.class)
    abstract ViewModel bindExpenseListViewModel(ExpenseListViewModel postListViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
