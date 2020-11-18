package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.ExpensesRepository;
import edu.iastate.adamcorp.expensetracker.data.models.Expense;
import edu.iastate.adamcorp.expensetracker.di.ViewModelFactory;
import edu.iastate.adamcorp.expensetracker.ui.viewholders.ExpenseViewHolder;
import edu.iastate.adamcorp.expensetracker.ui.viewmodel.ExpenseListViewModel;

public class ExpenseListFragment extends DaggerFragment implements View.OnClickListener {
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    ExpensesRepository expensesRepository;

    private ExpenseListViewModel expenseListViewModel;
    private FirestoreRecyclerAdapter<Expense, ExpenseViewHolder> recyclerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_expense_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.date_asc) {
            recyclerAdapter.updateOptions(expenseListViewModel.expenseListRecyclerOptionsByDate(Query.Direction.ASCENDING));
            recyclerAdapter.startListening();
        } else if (item.getItemId() == R.id.date_dsc) {
            recyclerAdapter.updateOptions(expenseListViewModel.expenseListRecyclerOptionsByDate(Query.Direction.DESCENDING));
            recyclerAdapter.startListening();
        } else if (item.getItemId() == R.id.amount_asc) {
            recyclerAdapter.updateOptions(expenseListViewModel.expenseListRecyclerOptionsByAmount(Query.Direction.ASCENDING));
            recyclerAdapter.startListening();
        } else if (item.getItemId() == R.id.amount_dsc) {
            recyclerAdapter.updateOptions(expenseListViewModel.expenseListRecyclerOptionsByAmount(Query.Direction.DESCENDING));
            recyclerAdapter.startListening();
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_list_fab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        expenseListViewModel = new ViewModelProvider(this, viewModelFactory).get(ExpenseListViewModel.class);
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);


        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        FirestoreRecyclerOptions<Expense> expenseCategoryRecyclerOptions = expenseListViewModel.expenseListRecyclerOptions();
        recyclerAdapter = new FirestoreRecyclerAdapter<Expense, ExpenseViewHolder>(expenseCategoryRecyclerOptions) {

            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View inflate = getLayoutInflater().inflate(R.layout.list_item_expense, parent, false);
                return new ExpenseViewHolder(inflate);
            }

            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, final int position, @NonNull Expense model) {
                final String id = getSnapshots().getSnapshot(position).getId();
                holder.updateView(model);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ExpenseListFragmentDirections.ActionExpenseListFragmentToEditExpenseFragment action = ExpenseListFragmentDirections.actionExpenseListFragmentToEditExpenseFragment(id);
                        NavHostFragment.findNavController(ExpenseListFragment.this).navigate(action);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //expenseCategoryViewModel.deleteCategory(id);
                        return false;
                    }
                });
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        final FloatingActionButton fab = view.findViewById(R.id.floating_action_button);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });

        fab.setOnClickListener(this);

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
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_expenseListFragment_to_addExpenseFragment);
        }
    }
}
