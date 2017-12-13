package org.launchcode.homemanager.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Payment {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private BudgetMonth budgetMonth;

    private Double amount = 0.0;

    private Double owes;

    public Payment(double amount) {
        this.amount = amount;
    }

    public Payment(){}

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BudgetMonth getBudgetMonth() {
        return budgetMonth;
    }

    public void setBudgetMonth(BudgetMonth budgetMonth) {
        this.budgetMonth = budgetMonth;
    }

    public Double getOwes() {
        return owes;
    }

    public void setOwes(Double owes) {
        this.owes = owes;
    }
}
