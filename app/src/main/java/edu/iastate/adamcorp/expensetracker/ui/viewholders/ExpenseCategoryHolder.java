package edu.iastate.adamcorp.expensetracker.ui.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseCategory;

public class ExpenseCategoryHolder extends RecyclerView.ViewHolder {
    private final TextView mNameTextView;

    public ExpenseCategoryHolder(@NonNull View itemView) {
        super(itemView);

        mNameTextView = this.itemView.findViewById(R.id.expense_category_name);
    }

    public void updateView(ExpenseCategory expenseCategory) {
        mNameTextView.setText(expenseCategory.getName());
    }
}
