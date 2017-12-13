package org.launchcode.homemanager.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by schwifty on 11/14/17.
 */
@Entity
public class BudgetMonth {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany
//    @JoinColumn(name = "budgetMonth_id")
    private List<Payment> payments = new ArrayList<>();

    private Integer month;
    private Integer year;
    private Double rent = 0.0;
    private Double electric = 0.0;
    private Double gas = 0.0;
    private Double internet = 0.0;
//    private Double water = 0.0;
    private Double etc = 0.0;

    public BudgetMonth(Integer month, Integer year) {
        this.month = month;
        this.year = year;
    }

    public BudgetMonth() {}

    public Double total() {
        Double thisTotal;
        thisTotal = rent + electric + gas + internet + etc;
        return thisTotal;
    }

    public static String monthName(Integer month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    public static Integer monthInt(String month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < monthNames.length; i++) {
            if (month.equals(monthNames[i])) {
                return i;
            }
        }
        return null;
    }



    public Double eachOwes(int numberOfUsers) {
        Double whatEachOwes = total() / numberOfUsers;
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

    public Double getRent() {
        return rent;
    }

    public void setRent(Double rent) {
        this.rent = rent;
    }

    public Double getElectric() {
        return electric;
    }

    public void setElectric(Double electric) {
        this.electric = electric;
    }

    public double getGas() {
        return gas;
    }

    public void setGas(Double gas) {
        this.gas = gas;
    }

    public Double getInternet() {
        return internet;
    }

    public void setInternet(Double internet) {
        this.internet = internet;
    }

//    public Double getWater() {
//        return water;
//    }
//
//    public void setWater(Double water) {
//        this.water = water;
//    }

    public Double getEtc() {
        return etc;
    }

    public void setEtc(Double etc) {
        this.etc = etc;
    }

    public int getId() {
        return id;
    }
}
