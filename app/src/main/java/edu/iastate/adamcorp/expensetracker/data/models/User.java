package edu.iastate.adamcorp.expensetracker.data.models;

public class User {
    private String symbol;
    private double monthlyBudget;

    public User() {
    }

    public User(String currencySymbol, double monthlyBudget) {
        this.symbol = currencySymbol;
        this.monthlyBudget = monthlyBudget;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }
}
