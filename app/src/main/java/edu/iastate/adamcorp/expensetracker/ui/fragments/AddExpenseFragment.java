package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.CategoriesRepository;
import edu.iastate.adamcorp.expensetracker.data.ExpensesRepository;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseCategory;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseQueue;

public class AddExpenseFragment extends DaggerFragment {
    @Inject
    CategoriesRepository categoriesRepository;
    @Inject
    ExpensesRepository expensesRepository;

    private List<DocumentSnapshot> categories;
    private ListenerRegistration categoriesListenerRegistration;
    private int itemSelected = ListView.INVALID_POSITION;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.categories_auto_complete);
        final Button addExpenseButton = view.findViewById(R.id.add_expense_button);
        final EditText amountEditText = view.findViewById(R.id.amount_edit_text);
        final EditText nameEditText = view.findViewById(R.id.name_edit_text);
        categoriesListenerRegistration = categoriesRepository.getCategories().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                List<ExpenseCategory> expenseCategories = queryDocumentSnapshots.toObjects(ExpenseCategory.class);
                categories = queryDocumentSnapshots.getDocuments();
                autoCompleteTextView.setAdapter(new ArrayAdapter<ExpenseCategory>(requireContext(), android.R.layout.simple_dropdown_item_1line, expenseCategories));
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = i;
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selection = itemSelected;
                final ExpenseQueue expenseQueue = new ExpenseQueue(nameEditText.getText().toString(), "", Double.parseDouble(amountEditText.getText().toString()), Timestamp.now());

                if (selection == ListView.INVALID_POSITION) {
                    //Add Category
                    categoriesRepository.addCategory(new ExpenseCategory(autoCompleteTextView.getText().toString())).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            expenseQueue.setCategoryId(documentReference.getId());
                            expensesRepository.addExpense(expenseQueue);

                            NavHostFragment.findNavController(AddExpenseFragment.this).navigateUp();
                        }
                    });
                } else {
                    expenseQueue.setCategoryId(categories.get(selection).getId());
                    expensesRepository.addExpense(expenseQueue);
                    NavHostFragment.findNavController(AddExpenseFragment.this).navigateUp();
                }

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        categoriesListenerRegistration.remove();
    }
}
