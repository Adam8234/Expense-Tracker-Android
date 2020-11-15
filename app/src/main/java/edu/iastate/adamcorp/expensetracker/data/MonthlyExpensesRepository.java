package edu.iastate.adamcorp.expensetracker.data;

import com.google.firebase.firestore.CollectionReference;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MonthlyExpensesRepository {

    private final UserRepository userRepository;

    @Inject
    public MonthlyExpensesRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CollectionReference getMonthlyExpenses() {
        return userRepository.getUserDocument().collection("monthly_expenses");
    }

}
