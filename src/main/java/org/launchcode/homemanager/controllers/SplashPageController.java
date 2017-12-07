package org.launchcode.homemanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SplashPageController extends MainController{

    //creates a splashpage after login which sets the login cookie for the entire domain
    //This is important because during development, app is launched on localhost, and chrome
    //won't allow you to set persistent cookies on domains higher than the one they are set on
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String welcome(Model model,
                          HttpServletResponse response) {
        Cookie loggedInUserCookie = SplashPageController.getLoggedInUser();
        loggedInUserCookie.setMaxAge(60*60);
        response.addCookie(loggedInUserCookie);
        SplashPageController.setLoggedInUser(null);

        return "splashpage";
    }
}