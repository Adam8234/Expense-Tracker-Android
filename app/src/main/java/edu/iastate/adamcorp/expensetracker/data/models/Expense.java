package edu.iastate.adamcorp.expensetracker.data.models;

import java.util.Date;

public class Expense {
    private double amount;
    private String category;
    private Date date;
    private String place;

    public Expense(double amount, String category, Date date, String place) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.place = place;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }
}
