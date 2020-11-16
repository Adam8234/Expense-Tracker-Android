package edu.iastate.adamcorp.expensetracker.data.models;

public class ExpenseCategory {
    private String name;
    private int totalExpenses;
    private double totalAmount;

    public ExpenseCategory() {
    }

    public ExpenseCategory(String name, int numOfExpenses, double totalAmount) {
        this.name = name;
        this.totalExpenses = numOfExpenses;
        this.totalAmount = totalAmount;
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

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(int totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
