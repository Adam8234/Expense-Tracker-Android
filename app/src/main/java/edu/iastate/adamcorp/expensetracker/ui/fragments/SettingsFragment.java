package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.firebase.ui.auth.AuthUI;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.UserRepository;
import edu.iastate.adamcorp.expensetracker.data.models.User;
import edu.iastate.adamcorp.expensetracker.service.AuthenticationService;
import edu.iastate.adamcorp.expensetracker.ui.SignInActivity;

public class SettingsFragment extends PreferenceFragmentCompat implements HasAndroidInjector, Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    UserRepository userRepository;


    @Inject
    AuthenticationService authenticationService;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference sign_out = findPreference("sign_out");
        final EditTextPreference currency_symbol = findPreference("currency_symbol");
        final EditTextPreference monthly_budget = findPreference("monthly_budget");
        final EditTextPreference bill_reminder = findPreference("bill_reminder");

        sign_out.setOnPreferenceClickListener(this);
        currency_symbol.setOnPreferenceChangeListener(this);
        monthly_budget.setOnPreferenceChangeListener(this);
        bill_reminder.setOnPreferenceChangeListener(this);

        userRepository.getUserDocument().addSnapshotListener((value, error) -> {
            User user = value.toObject(User.class);
            String symbol = user.getSymbol();
            Double budget = user.getMonthlyBudget();
            Integer dayReminder = user.getDayReminder();
            if (symbol == null) {
                symbol = "$";
                userRepository.changeCurrencySymbol(symbol);
            }
            if (budget == null) {
                budget = -1.0;
                userRepository.changeMonthlyBudget(budget);
            }
            if (dayReminder == null) {
                dayReminder = -1;
                userRepository.changeDayReminder(dayReminder);
            }
            monthly_budget.setText(budget.toString());
            currency_symbol.setText(symbol);
            bill_reminder.setText(dayReminder.toString());
        });
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("sign_out")) {
            AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener(task -> {
                        Intent intent = new Intent(requireContext(), SignInActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    });
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals("currency_symbol")) {
            userRepository.changeCurrencySymbol((String) newValue);
            return true;
        } else if (preference.getKey().equals("monthly_budget")) {
            userRepository.changeMonthlyBudget(Double.parseDouble((String) newValue));
        } else if (preference.getKey().equals("bill_reminder")) {
            userRepository.changeDayReminder(Integer.parseInt((String) newValue));
        }
        return false;
    }
}
