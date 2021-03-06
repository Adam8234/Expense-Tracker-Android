package edu.iastate.adamcorp.expensetracker.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import edu.iastate.adamcorp.expensetracker.R;
import edu.iastate.adamcorp.expensetracker.data.ExpensesRepository;
import edu.iastate.adamcorp.expensetracker.data.UserRepository;

public class MainActivity extends DaggerAppCompatActivity {
    @Inject
    ExpensesRepository expensesRepository;
    @Inject
    FirebaseMessaging firebaseMessaging;
    @Inject
    UserRepository userRepository;
    private BottomNavigationView mNavBottomView;
    private Toolbar mToolbar;
    private NavController mNavController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavBottomView = findViewById(R.id.bottom_navigation_view);
        mToolbar = findViewById(R.id.toolbar);

        mNavController = NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
        setSupportActionBar(mToolbar);
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.expenseListFragment);
        topLevelDestinations.add(R.id.expenseCategoriesFragment);
        topLevelDestinations.add(R.id.expenseMonthlySummaryFragment);
        topLevelDestinations.add(R.id.settingsFragment);
        appBarConfiguration =
                new AppBarConfiguration.Builder(topLevelDestinations).build();

        NavigationUI.setupWithNavController(mNavBottomView, mNavController);
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);

        firebaseMessaging.getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                userRepository.updateFCMToken(s);
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
