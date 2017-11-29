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

    private int month;
    private double rent;
    private double electric;
    private double gas;
    private double internet;
    private double etc;

    public BudgetMonth(int month, double rent, double electric, double gas, double internet, double etc) {
        this.month = month;
        this.rent = rent;
        this.electric = electric;
        this.gas = gas;
        this.internet = internet;
        this.etc = etc;
    }

    public BudgetMonth() {}

    public double total() {
        double thisTotal;
        thisTotal = rent + electric + gas + internet + etc;
        return thisTotal;
    }

    public static String monthName(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }



    public double eachOwes(int numberOfUsers) {
        double whatEachOwes = total() / numberOfUsers;
        return whatEachOwes;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
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
