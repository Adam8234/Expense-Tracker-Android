package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseCategory;
import edu.iastate.adamcorp.expensetracker.di.ViewModelFactory;
import edu.iastate.adamcorp.expensetracker.ui.viewholders.ExpenseCategoryHolder;
import edu.iastate.adamcorp.expensetracker.ui.viewmodel.ExpenseCategoryViewModel;

public class ExpenseCategoriesFragment extends DaggerFragment {
    @Inject
    ViewModelFactory viewModelFactory;
    private ExpenseCategoryViewModel expenseCategoryViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_list_fab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        expenseCategoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ExpenseCategoryViewModel.class);
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        FirestoreRecyclerOptions<ExpenseCategory> expenseCategoryRecyclerOptions = expenseCategoryViewModel.expenseCategoryRecyclerOptions();
        FirestoreRecyclerAdapter<ExpenseCategory, ExpenseCategoryHolder> recyclerAdapter = new FirestoreRecyclerAdapter<ExpenseCategory, ExpenseCategoryHolder>(expenseCategoryRecyclerOptions) {

            @NonNull
            @Override
            public ExpenseCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull ExpenseCategoryHolder holder, int position, @NonNull ExpenseCategory model) {

            }
        };
        recyclerView.setAdapter(recyclerAdapter);
    }
}
