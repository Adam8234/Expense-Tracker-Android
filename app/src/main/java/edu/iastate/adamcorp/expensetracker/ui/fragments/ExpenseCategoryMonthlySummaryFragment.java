package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.firestore.Query;

public class ExpenseCategoryMonthlySummaryFragment extends ExpenseSummaryFragment {

    private ExpenseCategoryMonthlySummaryFragmentArgs parsedArgs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parsedArgs = ExpenseCategoryMonthlySummaryFragmentArgs.fromBundle(getArguments());
    }

    @Override
    public Query getQuery(String yearMonthId) {
        return expensesRepository.getExpenses().whereEqualTo("yearMonthId", yearMonthId).whereEqualTo("categoryId", parsedArgs.getCategoryId());
    }

    @Override
    public void onExpenseClick(String expenseId) {
        NavDirections action = ExpenseCategoryMonthlySummaryFragmentDirections.actionExpenseCategoryMonthlySummaryFragmentToEditExpenseFragment(expenseId);
        NavHostFragment.findNavController(this).navigate(action);
    }
}
