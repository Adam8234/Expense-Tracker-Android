package edu.iastate.adamcorp.expensetracker.data.models;

import com.google.firebase.Timestamp;

public class ExpenseQueue {
    private String name;
    private String categoryId;
    private double amount;
    private Timestamp date;

    public ExpenseQueue() {
    }

    public ExpenseQueue(String name, String categoryId, double amount, Timestamp date) {
        this.name = name;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
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
}
