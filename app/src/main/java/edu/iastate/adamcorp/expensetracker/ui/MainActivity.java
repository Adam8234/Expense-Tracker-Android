package edu.iastate.adamcorp.expensetracker.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.ExpensesRepository;

public class MainActivity extends DaggerAppCompatActivity {
    private BottomNavigationView mNavBottomView;
    private Toolbar mToolbar;
    private NavController mNavController;
    @Inject
    ExpensesRepository expensesRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavBottomView = findViewById(R.id.bottom_navigation_view);
        mToolbar = findViewById(R.id.toolbar);

        mNavController = NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
        NavigationUI.setupWithNavController(mNavBottomView, mNavController);

        setSupportActionBar(mToolbar);
    }
}
