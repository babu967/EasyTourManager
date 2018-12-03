package com.codex.easytourmanager.event_class;

import java.io.Serializable;

public class Expense implements Serializable {
    private String expenseDetails;
    private String expenseDateTime;
    private String expenseCost;
    private String expenseKey;
    private String eventKey;


    public Expense() {
    }

    public Expense(String expenseDetails, String expenseDateTime, String expenseCost, String expenseKey, String eventKey) {
        this.expenseDetails = expenseDetails;
        this.expenseDateTime = expenseDateTime;
        this.expenseCost = expenseCost;
        this.expenseKey = expenseKey;
        this.eventKey = eventKey;
    }

    public String getExpenseDetails() {
        return expenseDetails;
    }

    public String getExpenseCost() {
        return expenseCost;
    }

    public String getExpenseKey() {
        return expenseKey;
    }

    public String getExpenseDateTime() {
        return expenseDateTime;
    }

    public String getEventKey() {
        return eventKey;
    }
}

