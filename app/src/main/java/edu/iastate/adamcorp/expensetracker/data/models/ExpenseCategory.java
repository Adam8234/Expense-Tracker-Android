package edu.iastate.adamcorp.expensetracker.data.models;

public class ExpenseCategory {
    private String name;
    private int numOfExpenses;
    private double totalExpenses;

    public ExpenseCategory() {
    }

    public ExpenseCategory(String name, int numOfExpenses, double totalExpenses) {
        this.name = name;
        this.numOfExpenses = numOfExpenses;
        this.totalExpenses = totalExpenses;
    }


    public ExpenseCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getNumOfExpenses() {
        return numOfExpenses;
    }

    public void setNumOfExpenses(int numOfExpenses) {
        this.numOfExpenses = numOfExpenses;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
}
