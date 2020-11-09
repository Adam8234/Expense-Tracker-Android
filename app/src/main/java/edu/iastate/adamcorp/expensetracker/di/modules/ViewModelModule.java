package edu.iastate.adamcorp.expensetracker.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import edu.iastate.adamcorp.expensetracker.di.ViewModelFactory;
import edu.iastate.adamcorp.expensetracker.di.ViewModelKey;
import edu.iastate.adamcorp.expensetracker.ui.viewmodel.ExpenseCategoryViewModel;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ExpenseCategoryViewModel.class)
    abstract ViewModel bindPostListViewModel(ExpenseCategoryViewModel postListViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
