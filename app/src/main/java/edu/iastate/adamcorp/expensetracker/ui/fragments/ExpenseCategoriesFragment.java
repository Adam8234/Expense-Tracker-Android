package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseCategory;
import edu.iastate.adamcorp.expensetracker.di.ViewModelFactory;
import edu.iastate.adamcorp.expensetracker.ui.viewholders.ExpenseCategoryHolder;
import edu.iastate.adamcorp.expensetracker.ui.viewmodel.ExpenseCategoryViewModel;

public class ExpenseCategoriesFragment extends DaggerFragment implements View.OnClickListener {
    @Inject
    ViewModelFactory viewModelFactory;
    private ExpenseCategoryViewModel expenseCategoryViewModel;
    private FirestoreRecyclerAdapter<ExpenseCategory, ExpenseCategoryHolder> recyclerAdapter;

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


        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        FirestoreRecyclerOptions<ExpenseCategory> expenseCategoryRecyclerOptions = expenseCategoryViewModel.expenseCategoryRecyclerOptions();
        recyclerAdapter = new FirestoreRecyclerAdapter<ExpenseCategory, ExpenseCategoryHolder>(expenseCategoryRecyclerOptions) {

            @NonNull
            @Override
            public ExpenseCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View inflate = getLayoutInflater().inflate(R.layout.list_item_expense_category, parent, false);
                return new ExpenseCategoryHolder(inflate);
            }

            @Override
            protected void onBindViewHolder(@NonNull ExpenseCategoryHolder holder, final int position, @NonNull ExpenseCategory model) {

                final String id = getSnapshots().getSnapshot(position).getId();
                holder.updateView(model);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NavDirections action = ExpenseCategoriesFragmentDirections.actionExpenseCategoriesFragmentToExpenseCategoryMonthlySummaryFragment(id);
                        NavHostFragment.findNavController(ExpenseCategoriesFragment.this).navigate(action);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        expenseCategoryViewModel.deleteCategory(id);
                        return false;
                    }
                });
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        view.findViewById(R.id.floating_action_button).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.floating_action_button) {
            View inflatedView = getLayoutInflater().inflate(R.layout.dialog_expense_category, null, false);
            final EditText input = inflatedView.findViewById(R.id.input);
            new MaterialAlertDialogBuilder(requireContext())
                    .setView(inflatedView)
                    .setTitle("Add Category")
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = input.getText().toString();
                            expenseCategoryViewModel.addExpenseCategory(new ExpenseCategory(name));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Do Nothing...
                        }
                    })
                    .create().show();
        }
    }
}
