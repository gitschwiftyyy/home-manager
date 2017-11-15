package org.launchcode.homemanager.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by schwifty on 11/14/17.
 */
@Entity
public class BudgetMonth {

    @Id
    @GeneratedValue
    private int id;

    private String month;
    private double rent;
    private double electric;
    private double gas;
    private double internet;
    private double etc;

    public BudgetMonth(String month, double rent, double electric, double gas, double internet, double etc) {
        this.month = month;
        this.rent = rent;
        this.electric = electric;
        this.gas = gas;
        this.internet = internet;
        this.etc = etc;
    }

    public BudgetMonth() {}

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public double getElectric() {
        return electric;
    }

    public void setElectric(double electric) {
        this.electric = electric;
    }

    public double getGas() {
        return gas;
    }

    public void setGas(double gas) {
        this.gas = gas;
    }

    public double getInternet() {
        return internet;
    }

    public void setInternet(double internet) {
        this.internet = internet;
    }

    public double getEtc() {
        return etc;
    }

    public void setEtc(double etc) {
        this.etc = etc;
    }

    public int getId() {
        return id;
    }
}
