package edu.iastate.adamcorp.expensetracker.data;

import com.google.firebase.firestore.CollectionReference;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CategoriesRepository {
    UserRepository userRepository;

    @Inject
    public CategoriesRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CollectionReference getCategories() {
        return userRepository.getUserDocument().collection("categories");
    }
}
