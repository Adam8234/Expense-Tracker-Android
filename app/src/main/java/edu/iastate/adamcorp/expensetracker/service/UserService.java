package edu.iastate.adamcorp.expensetracker.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.iastate.adamcorp.expensetracker.data.ExpensesRepository;

@Singleton
public class UserService {
    @Inject
    public UserService(ExpensesRepository expensesRepository) {
    }
}
