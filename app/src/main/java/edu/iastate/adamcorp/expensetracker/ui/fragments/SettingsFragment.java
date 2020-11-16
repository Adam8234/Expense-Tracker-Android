package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.UserRepository;
import edu.iastate.adamcorp.expensetracker.data.models.User;

public class SettingsFragment extends PreferenceFragmentCompat implements HasAndroidInjector, Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    UserRepository userRepository;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //Preference expense_categories = findPreference("expense_categories");
        final EditTextPreference currency_symbol = findPreference("currency_symbol");
        final EditTextPreference monthly_budget = findPreference("monthly_budget");

        //expense_categories.setOnPreferenceClickListener(this);
        currency_symbol.setOnPreferenceChangeListener(this);
        monthly_budget.setOnPreferenceChangeListener(this);

        userRepository.getUserDocument().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                User user = value.toObject(User.class);
                String symbol = user.getSymbol();
                Double budget = user.getMonthlyBudget();
                if(symbol == null) {
                    symbol = "$";
                    userRepository.changeCurrencySymbol(symbol);
                }
                if(budget == null) {
                    budget = -1.0;
                    userRepository.changeMonthlyBudget(-1.0);
                }
                monthly_budget.setText(budget.toString());
                currency_symbol.setText(symbol);
            }
        });
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("expense_categories")) {
            NavController navController = NavHostFragment.findNavController(this);
            //navController.navigate(R.id.action_settingsFragment_to_expenseCategoriesFragment);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals("currency_symbol")) {
            userRepository.changeCurrencySymbol((String) newValue);
            return true;
        } else if(preference.getKey().equals("monthly_budget")) {
            userRepository.changeMonthlyBudget(Double.parseDouble((String) newValue));
        }
        return false;
    }
}
