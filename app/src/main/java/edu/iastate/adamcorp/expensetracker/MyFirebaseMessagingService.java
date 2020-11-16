package edu.iastate.adamcorp.expensetracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import edu.iastate.adamcorp.expensetracker.data.UserRepository;

public class MyFirebaseMessagingService extends FirebaseMessagingService implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Inject
    UserRepository userRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        userRepository.updateFCMToken(s);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("bill_notifications", "Bill Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}
