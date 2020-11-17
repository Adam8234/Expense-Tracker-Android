package edu.iastate.adamcorp.expensetracker.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import edu.iastate.adamcorp.expensetracker.data.ExpensesRepository;
import edu.iastate.adamcorp.expensetracker.data.models.Expense;

public class ExpenseListViewModel extends ViewModel {
    private ExpensesRepository expensesRepository;

    @Inject
    public ExpenseListViewModel(ExpensesRepository expensesRepository) {
        this.expensesRepository = expensesRepository;
    }


    public FirestoreRecyclerOptions<Expense> expenseListRecyclerOptions() {
        Query expenses = this.expensesRepository.getExpenses();
        return new FirestoreRecyclerOptions.Builder<Expense>()
                .setQuery(expenses, Expense.class)
                .build();
    }

    public FirestoreRecyclerOptions<Expense> expenseListRecyclerOptions(Query.Direction direction) {
        Query expenses = this.expensesRepository.getExpenses().orderBy("date", direction);
        return new FirestoreRecyclerOptions.Builder<Expense>()
                .setQuery(expenses, Expense.class)
                .build();
    }
}
