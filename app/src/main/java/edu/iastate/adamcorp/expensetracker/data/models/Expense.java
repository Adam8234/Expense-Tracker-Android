package edu.iastate.adamcorp.expensetracker.data.models;


import com.google.firebase.Timestamp;

public class Expense {
    private String name;
    private String categoryId;
    private double amount;
    private Timestamp date;
    private String symbol;
    private ExpenseCategory category;

    public Expense() {
    }

    public Expense(String name, String categoryId, double amount, Timestamp date, String symbol, ExpenseCategory category) {
        this.name = name;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.symbol = symbol;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }
}
