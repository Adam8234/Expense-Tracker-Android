package edu.iastate.adamcorp.expensetracker.data.models;

public class MonthlyExpense {
    private double totalAmount;
    private int totalExpenses;
    private String symbol;
    private Double monthlyBudget;

    public MonthlyExpense() {
    }

    public MonthlyExpense(double totalAmount, int totalExpenses, String symbol, Double monthlyBudget) {
        this.totalAmount = totalAmount;
        this.totalExpenses = totalExpenses;
        this.symbol = symbol;
        this.monthlyBudget = monthlyBudget;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(Double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
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
