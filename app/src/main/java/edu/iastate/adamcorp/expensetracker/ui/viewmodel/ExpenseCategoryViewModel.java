package edu.iastate.adamcorp.expensetracker.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;

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
        CollectionReference categories = this.categoriesRepository.getCategories();
        return new FirestoreRecyclerOptions.Builder<ExpenseCategory>()
                .setQuery(categories, ExpenseCategory.class)
                .build();
    }

    public void addExpenseCategory(ExpenseCategory expenseCategory) {
        this.categoriesRepository.addCategory(expenseCategory);
    }

    public void deleteCategory(String id) {
        this.categoriesRepository.deleteCategory(id);
    }
}
