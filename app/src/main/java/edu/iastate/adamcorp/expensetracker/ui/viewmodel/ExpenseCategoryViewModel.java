package edu.iastate.adamcorp.expensetracker.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import edu.iastate.adamcorp.expensetracker.data.CategoriesRepository;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseCategory;

public class ExpenseCategoryViewModel extends ViewModel {
    private CategoriesRepository categoriesRepository;

    @Inject
    public ExpenseCategoryViewModel(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public FirestoreRecyclerOptions<ExpenseCategory> expenseCategoryRecyclerOptions() {
        return new FirestoreRecyclerOptions.Builder<ExpenseCategory>()
                .setQuery(this.categoriesRepository.getCategories(), ExpenseCategory.class)
                .build();
    }
}
