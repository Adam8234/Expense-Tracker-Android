package edu.iastate.adamcorp.expensetracker.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.android.support.DaggerAppCompatActivity;
import edu.iastate.adamcorp.expensetracker.R;

public class MainActivity extends DaggerAppCompatActivity {
    private BottomNavigationView mNavBottomView;
    private Toolbar mToolbar;
    private NavController mNavController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavBottomView = findViewById(R.id.bottom_navigation_view);
        mToolbar = findViewById(R.id.toolbar);

        mNavController = NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
        mNavBottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.expenses:
                        mNavController.navigate(R.id.expenseListFragment);
                        return true;
                    case R.id.settings:
                        mNavController.navigate(R.id.settingsFragment);
                        return true;
                    case R.id.summary:
                        mNavController.navigate(R.id.expenseSummaryFragment);
                        return true;
                }
                return false;
            }
        });

        setSupportActionBar(mToolbar);
    }
}
