package edu.iastate.adamcorp.expensetracker.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.android.support.DaggerAppCompatActivity;
import edu.iastate.adamcorp.expensetracker.R;

public class MainActivity extends DaggerAppCompatActivity {
    private BottomNavigationView mNavBottomView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavBottomView = findViewById(R.id.bottom_navigation_view);
        final NavController navController = NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
        mNavBottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.expenses:
                        navController.navigate(R.id.expenseListFragment);
                        return true;
                    case R.id.settings:
                        navController.navigate(R.id.settingsFragment);
                        return true;
                }
                return false;
            }
        });
    }
}
