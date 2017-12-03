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

    private Integer month;
    private Integer year;
    private double rent = 0.0;
    private double electric = 0.0;
    private double gas = 0.0;
    private double internet = 0.0;
    private double etc = 0.0;

    public BudgetMonth(Integer month, Integer year) {
        this.month = month;
        this.year = year;
    }

    public BudgetMonth() {}

    public double total() {
        double thisTotal;
        thisTotal = rent + electric + gas + internet + etc;
        return thisTotal;
    }

    public static String monthName(Integer month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }



    public double eachOwes(int numberOfUsers) {
        double whatEachOwes = total() / numberOfUsers;
        return whatEachOwes;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
