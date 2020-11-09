package edu.iastate.adamcorp.expensetracker.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import edu.iastate.adamcorp.expensetracker.R;

public class SettingsFragment extends PreferenceFragmentCompat implements HasAndroidInjector, Preference.OnPreferenceClickListener {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        findPreference("expense_categories").setOnPreferenceClickListener(this);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals("expense_categories")) {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_settingsFragment_to_expenseCategoriesFragment);
            return true;
        }
        return false;
    }
}
