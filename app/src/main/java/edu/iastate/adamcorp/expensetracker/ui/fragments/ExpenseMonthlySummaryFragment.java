package edu.iastate.adamcorp.expensetracker.ui.fragments;

import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.firestore.Query;

public class ExpenseMonthlySummaryFragment extends ExpenseSummaryFragment{
    @Override
    public Query getQuery(String yearMonthId) {
        return expensesRepository.getExpenses().whereEqualTo("yearMonthId", yearMonthId);
    }

    @Override
    public void onExpenseClick(String expenseId) {
        NavDirections action = ExpenseMonthlySummaryFragmentDirections.actionExpenseMonthlySummaryFragmentToEditExpenseFragment(expenseId);
        NavHostFragment.findNavController(this).navigate(action);
    }
}
