package edu.iastate.adamcorp.expensetracker.data.models;

public class User {
    private String symbol;
    private Double monthlyBudget;
    private Integer dayReminder;

    public User() {
    }

    public User(String currencySymbol, Double monthlyBudget, Integer dayReminder) {
        this.symbol = currencySymbol;
        this.monthlyBudget = monthlyBudget;
        this.dayReminder = dayReminder;
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

    public Integer getDayReminder() {
        return dayReminder;
    }

    public void setDayReminder(Integer dayReminder) {
        this.dayReminder = dayReminder;
    }
}
