package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseCategory;
import edu.iastate.adamcorp.expensetracker.data.models.MonthlyExpense;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class ExpenseMonthlySummaryFragment extends ExpenseSummaryFragment {

    private PieChart pieChart;
    private ListenerRegistration graphRegistration;
    private ListenerRegistration monthlyExpenseRegistration;

    private TextView totalTextView;
    private ProgressBar progressBar;
    private TextView minExpense;
    private TextView maxExpense;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_monthly, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_summary_monthly_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.toggle_graph_menu) {
            if(pieChart.getVisibility() == View.VISIBLE) {
                pieChart.setVisibility(View.GONE);
            } else {
                pieChart.setVisibility(View.VISIBLE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.pieChart = view.findViewById(R.id.chart);
        totalTextView = view.findViewById(R.id.total_text_view);
        progressBar = view.findViewById(R.id.progress_bar);
        maxExpense = view.findViewById(R.id.max_expense);
        minExpense = view.findViewById(R.id.min_expense);
    }

    @Override
    public Query getQuery(String yearMonthId) {
        return expensesRepository.getExpenses().whereEqualTo("yearMonthId", yearMonthId);
    }

    @Override
    public void onYearMonthSelected(String yearMonthId) {
        super.onYearMonthSelected(yearMonthId);
        if (monthlyExpenseRegistration != null) {
            monthlyExpenseRegistration.remove();
        }
        monthlyExpenseRegistration = monthlyExpensesRepository.getMonthlyExpenses().document(yearMonthId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                MonthlyExpense monthlyExpense = value.toObject(MonthlyExpense.class);
                if(monthlyExpense == null || monthlyExpense.getMonthlyBudget() == null || monthlyExpense.getSymbol() == null) {
                    return;
                }
                totalTextView.setText(String.format("%s%.2f", monthlyExpense.getSymbol(), monthlyExpense.getTotalAmount()));

                minExpense.setText(String.format("%s0", monthlyExpense.getSymbol()));
                maxExpense.setText(String.format("%s%.2f", monthlyExpense.getSymbol(), monthlyExpense.getMonthlyBudget()));

                progressBar.setMax(1000);
                int progressValue = (int) ((monthlyExpense.getTotalAmount() / monthlyExpense.getMonthlyBudget()) * 1000);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(progressValue, true);
                } else {
                    progressBar.setProgress(progressValue);
                }
                if(monthlyExpense.getTotalAmount() > monthlyExpense.getMonthlyBudget()) {
                    DrawableCompat.setTint(progressBar.getProgressDrawable(),getResources().getColor(android.R.color.holo_red_dark) );
                } else {
                    DrawableCompat.setTint(progressBar.getProgressDrawable(),getResources().getColor(R.color.colorAccent) );
                }
            }
        });
        if (graphRegistration != null) {
            graphRegistration.remove();
        }
        graphRegistration = monthlyExpensesRepository.getMonthlyExpenses().document(yearMonthId).collection("monthly_categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<ExpenseCategory> expenseCategories = value.toObjects(ExpenseCategory.class);
                double sum = 0.0;
                for (ExpenseCategory expenseCategory : expenseCategories) {
                    sum += expenseCategory.getTotalAmount();
                }

                List<PieEntry> pieEntries = new ArrayList<>();
                for (ExpenseCategory expenseCategory : expenseCategories) {
                    pieEntries.add(new PieEntry((float) (expenseCategory.getTotalAmount())/*/ sum) * 100*/, expenseCategory.getName()));
                }
                PieDataSet pieSet = new PieDataSet(pieEntries, null);
                pieSet.setColors(rgb("#ff6698"), rgb("#ffb366"), rgb("#ffff66"), rgb("#98ff66"), rgb("#6698ff"));
                pieSet.setSliceSpace(2.5f);

                PieData pieData = new PieData(pieSet);
                pieData.setValueTextSize(12.5f);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setData(pieData);
                pieChart.setDescription(null);
                pieChart.getLegend().setEnabled(false);
                pieChart.invalidate();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (graphRegistration != null) {
            graphRegistration.remove();
        }
        if (monthlyExpenseRegistration != null) {
            monthlyExpenseRegistration.remove();
        }
    }

    @Override
    public void onExpenseClick(String expenseId) {
        NavDirections action = ExpenseMonthlySummaryFragmentDirections.actionExpenseMonthlySummaryFragmentToEditExpenseFragment(expenseId);
        NavHostFragment.findNavController(this).navigate(action);
    }
}
