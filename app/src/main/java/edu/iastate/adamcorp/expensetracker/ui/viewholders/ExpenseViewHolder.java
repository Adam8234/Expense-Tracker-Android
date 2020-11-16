package edu.iastate.adamcorp.expensetracker.ui.viewholders;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.Date;

import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.models.Expense;

public class ExpenseViewHolder extends RecyclerView.ViewHolder {
    private final TextView mExpenseNameTextView;
    private final TextView mExpenseCategoryTextView;
    private final TextView mExpenseAmountTextView;
    private final DecimalFormat numberFormat;

    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);
        numberFormat = new DecimalFormat("#.00");
        mExpenseNameTextView = itemView.findViewById(R.id.expense_name);
        mExpenseCategoryTextView = itemView.findViewById(R.id.expense_category);
        mExpenseAmountTextView = itemView.findViewById(R.id.expense_amount);
    }

    public void updateView(Expense expense) {
        mExpenseNameTextView.setText(expense.getName());
        CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(expense.getDate().toDate().getTime(), new Date().getTime(), DateUtils.DAY_IN_MILLIS);
        mExpenseCategoryTextView.setText(String.format("%s - %s", expense.getCategory(), relativeTimeSpanString));
        mExpenseAmountTextView.setText(String.format("-%s%s", expense.getSymbol(), numberFormat.format(expense.getAmount())));
    }
}
