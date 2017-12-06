package org.launchcode.homemanager.controllers;

import javax.servlet.http.Cookie;

public class MainController {

    private static Cookie loggedInUser;
    private static Cookie thisBudgetMonth;


    public static Cookie getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Cookie loggedInUser) {
        MainController.loggedInUser = loggedInUser;
    }

    public static Cookie getThisBudgetMonth() {
        return thisBudgetMonth;
    }

    public static void setThisBudgetMonth(Cookie thisBudgetMonth) {
        MainController.thisBudgetMonth = thisBudgetMonth;
    }
}