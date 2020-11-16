package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.CategoriesRepository;
import edu.iastate.adamcorp.expensetracker.data.ExpensesRepository;
import edu.iastate.adamcorp.expensetracker.data.MonthlyExpensesRepository;
import edu.iastate.adamcorp.expensetracker.data.models.Expense;
import edu.iastate.adamcorp.expensetracker.ui.viewholders.ExpenseViewHolder;

public abstract class ExpenseSummaryFragment extends DaggerFragment {
    @Inject
    CategoriesRepository categoriesRepository;

    @Inject
    ExpensesRepository expensesRepository;

    @Inject
    MonthlyExpensesRepository monthlyExpensesRepository;
    private ListenerRegistration monthlyExpensesListenerRegistration;
    private FirestoreRecyclerAdapter<Expense, ExpenseViewHolder> adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //final PieChart pieChart = view.findViewById(R.id.chart);
        final Spinner spinner = view.findViewById(R.id.month_spinner);
        recyclerView = view.findViewById(R.id.recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        monthlyExpensesListenerRegistration = monthlyExpensesRepository.getMonthlyExpenses().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                ArrayList<String> yearMonthKeys = new ArrayList<>();
                if (queryDocumentSnapshots != null) {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    Collections.reverse(documents);
                    for (DocumentSnapshot monthlyExpenseSnapshot : documents) {
                        yearMonthKeys.add(monthlyExpenseSnapshot.getId());
                    }
                }
                spinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, yearMonthKeys));
                spinner.setSelection(0);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) adapterView.getAdapter();
                onYearMonthSelected(adapter.getItem(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public abstract Query getQuery(String yearMonthId);

    public abstract void onExpenseClick(String expenseId);

    public void onYearMonthSelected(String yearMonthId) {
        if (yearMonthId == null) {
            return;
        }
        Query query = getQuery(yearMonthId);
        FirestoreRecyclerOptions<Expense> options = new FirestoreRecyclerOptions.Builder<Expense>()
                .setQuery(query, Expense.class)
                .setLifecycleOwner(this)
                .build();
        if (adapter == null) {
            adapter = new FirestoreRecyclerAdapter<Expense, ExpenseViewHolder>(options) {
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
                            onExpenseClick(id);
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
            adapter.startListening();
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateOptions(options);
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        monthlyExpensesListenerRegistration.remove();
    }
}
