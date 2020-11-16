package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_monthly, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.pieChart = view.findViewById(R.id.chart);
        totalTextView = view.findViewById(R.id.total_text_view);
    }

    @Override
    public Query getQuery(String yearMonthId) {
        return expensesRepository.getExpenses().whereEqualTo("yearMonthId", yearMonthId);
    }

    @Override
    public void onYearMonthSelected(String yearMonthId) {
        super.onYearMonthSelected(yearMonthId);
        if(monthlyExpenseRegistration != null) {
            monthlyExpenseRegistration.remove();
        }
        monthlyExpenseRegistration = monthlyExpensesRepository.getMonthlyExpenses().document(yearMonthId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                MonthlyExpense monthlyExpense = value.toObject(MonthlyExpense.class);
                totalTextView.setText(String.format("%s%.2f", monthlyExpense.getSymbol(), monthlyExpense.getTotalAmount()));
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
        if(monthlyExpenseRegistration != null) {
            monthlyExpenseRegistration.remove();
        }
    }

    @Override
    public void onExpenseClick(String expenseId) {
        NavDirections action = ExpenseMonthlySummaryFragmentDirections.actionExpenseMonthlySummaryFragmentToEditExpenseFragment(expenseId);
        NavHostFragment.findNavController(this).navigate(action);
    }
}
