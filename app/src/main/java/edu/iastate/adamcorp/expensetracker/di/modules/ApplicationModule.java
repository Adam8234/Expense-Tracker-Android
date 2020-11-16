package edu.iastate.adamcorp.expensetracker.di.modules;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ApplicationModule {
    @Singleton
    @Provides
    static FirebaseFirestore firebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Singleton
    @Provides
    static FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    static FirebaseApp firebaseApp() {
        return FirebaseApp.getInstance();
    }

    @Singleton
    @Provides
    static FirebaseMessaging firebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }
}
