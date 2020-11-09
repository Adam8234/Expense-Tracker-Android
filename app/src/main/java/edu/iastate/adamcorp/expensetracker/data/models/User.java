package edu.iastate.adamcorp.expensetracker.data.models;

public class User {
    private String currencySymbol;
    private double monthlyBudget;

    public User(String currencySymbol, double monthlyBudget) {
        this.currencySymbol = currencySymbol;
        this.monthlyBudget = monthlyBudget;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }
}
