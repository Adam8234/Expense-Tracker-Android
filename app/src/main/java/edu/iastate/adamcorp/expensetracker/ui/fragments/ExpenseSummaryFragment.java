package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.CategoriesRepository;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseCategory;

public class ExpenseSummaryFragment extends DaggerFragment {
    @Inject
    CategoriesRepository categoriesRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final PieChart pieChart = view.findViewById(R.id.chart);
        categoriesRepository.getCategories().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<ExpenseCategory> expenseCategories = value.toObjects(ExpenseCategory.class);
                List<PieEntry> pieEntries = new ArrayList<>();
                for (ExpenseCategory expenseCategory : expenseCategories) {
                    pieEntries.add(new PieEntry((float) (expenseCategory.getTotalExpenses()), expenseCategory.getName()));
                }
                PieDataSet pieSet = new PieDataSet(pieEntries, null);
                pieSet.setSliceSpace(4f);
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
}
