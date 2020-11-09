package edu.iastate.adamcorp.expensetracker.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.iastate.adamcorp.expensetracker.data.models.ExpenseCategory;

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

    public Task<DocumentReference> addCategory(ExpenseCategory expenseCategory) {
        return userRepository.getUserDocument().collection("categories").add(expenseCategory);
    }


    public Task<Void> deleteCategory(String id) {
        return userRepository.getUserDocument().collection("categories").document(id).delete();
    }
}
