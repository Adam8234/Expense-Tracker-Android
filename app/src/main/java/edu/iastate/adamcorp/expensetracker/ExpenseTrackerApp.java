package edu.iastate.adamcorp.expensetracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import edu.iastate.adamcorp.expensetracker.di.ApplicationComponent;
import edu.iastate.adamcorp.expensetracker.di.DaggerApplicationComponent;

public class ExpenseTrackerApp extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        ApplicationComponent component = DaggerApplicationComponent
                .builder()
                .application(this)
                .build();
        component.inject(this);
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("bill_notifications", "Bill Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }
}
