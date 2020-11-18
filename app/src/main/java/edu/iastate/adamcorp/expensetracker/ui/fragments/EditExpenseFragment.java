package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.CategoriesRepository;
import edu.iastate.adamcorp.expensetracker.data.ExpensesRepository;
import edu.iastate.adamcorp.expensetracker.data.models.Expense;
import edu.iastate.adamcorp.expensetracker.data.models.ExpenseCategory;

public class EditExpenseFragment extends DaggerFragment {
    @Inject
    CategoriesRepository categoriesRepository;
    @Inject
    ExpensesRepository expensesRepository;

    private Expense expense;
    private List<DocumentSnapshot> categories;
    private ListenerRegistration categoriesListenerRegistration, expenseListenerRegistration;
    private String selectedCategoryId = null;
    private String expenseId;
    private Timestamp currentTimestamp;

    private EditText dateEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_expense_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_expense) {
            expensesRepository.deleteExpense(expenseId);
            NavHostFragment.findNavController(this).navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePicker(Timestamp timestamp) {
        MaterialDatePicker.Builder<Long> longBuilder = MaterialDatePicker.Builder.datePicker();
        longBuilder.setSelection(timestamp.toDate().getTime());
        MaterialDatePicker<Long> picker = longBuilder.build();
        picker.show(getChildFragmentManager(), "date_picker");
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                LocalDateTime now = LocalDateTime.now();
                ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(now);
                updateExpenseTimestamp(new Timestamp(new Date(selection - offset.getTotalSeconds() * DateUtils.SECOND_IN_MILLIS)));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_editor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expenseId = EditExpenseFragmentArgs.fromBundle(getArguments()).getExpenseId();
        final AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.categories_auto_complete);
        final Button addExpenseButton = view.findViewById(R.id.add_expense_button);
        final EditText amountEditText = view.findViewById(R.id.amount_edit_text);
        final EditText nameEditText = view.findViewById(R.id.name_edit_text);
        dateEditText = view.findViewById(R.id.date_edit_text);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(currentTimestamp);
            }
        });

        expenseListenerRegistration = expensesRepository.getExpense(expenseId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                expense = value.toObject(Expense.class);
                autoCompleteTextView.setText(expense.getCategory().getName());
                selectedCategoryId = expense.getCategoryId();
                amountEditText.setText(Double.toString(expense.getAmount()));
                nameEditText.setText(expense.getName());
                updateExpenseTimestamp(expense.getDate());
            }
        });


        categoriesListenerRegistration = categoriesRepository.getCategories().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                List<ExpenseCategory> expenseCategories = queryDocumentSnapshots.toObjects(ExpenseCategory.class);
                categories = queryDocumentSnapshots.getDocuments();
                autoCompleteTextView.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, expenseCategories));
            }
        });

        autoCompleteTextView.setOnItemClickListener((adapterView, view1, i, l) -> selectedCategoryId = categories.get(i).getId());

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                selectedCategoryId = null;
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expense.setName(nameEditText.getText().toString());
                expense.setAmount(Double.parseDouble(amountEditText.getText().toString()));
                expense.setDate(currentTimestamp);
                if (selectedCategoryId == null) {
                    //Add Category
                    categoriesRepository.addCategory(new ExpenseCategory(autoCompleteTextView.getText().toString())).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            expense.setCategoryId(documentReference.getId());
                            expensesRepository.updateExpense(expenseId, expense);
                        }
                    });
                } else {
                    expense.setCategoryId(selectedCategoryId);
                    expensesRepository.updateExpense(expenseId, expense);
                    NavHostFragment.findNavController(EditExpenseFragment.this).navigateUp();
                }

            }
        });

    }

    private void updateExpenseTimestamp(Timestamp date) {
        currentTimestamp = date;
        dateEditText.setText(DateFormat.format("MM/dd/yyyy", date.toDate().getTime()));
    }

    @Override
    public void onStop() {
        super.onStop();
        categoriesListenerRegistration.remove();
        expenseListenerRegistration.remove();
    }
}
