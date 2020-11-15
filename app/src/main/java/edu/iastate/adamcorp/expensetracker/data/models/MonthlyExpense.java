package edu.iastate.adamcorp.expensetracker.data.models;

public class MonthlyExpense {
    private double totalAmount;
    private int totalExpenses;

    public MonthlyExpense(double totalAmount, int totalExpenses) {
        this.totalAmount = totalAmount;
        this.totalExpenses = totalExpenses;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(int totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
}
