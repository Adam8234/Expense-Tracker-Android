package edu.iastate.adamcorp.expensetracker.di.modules;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
