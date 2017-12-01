package org.launchcode.homemanager.controllers;

import javax.servlet.http.Cookie;

public class MainController {

    private static Cookie loggedInUser;


    public static Cookie getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Cookie loggedInUser) {
        MainController.loggedInUser = loggedInUser;
    }


}