package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

@Controller
@RequestMapping(value = "")
public class SplashPageController extends MainController{

    //creates a splashpage after login which sets the login cookie for the entire domain
    //This is important because during development, app is launched on localhost, and chrome
    //won't allow you to set persistent cookies on domains higher than the one they are set on
    @Autowired
    BudgetMonthDao budgetMonthDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String welcome(Model model,
                          HttpServletResponse response) {
        if (SplashPageController.getLoggedInUser() == null) {
            return "redirect:/dashboard";
        }
            Cookie loggedInUserCookie = SplashPageController.getLoggedInUser();
            loggedInUserCookie.setMaxAge(60 * 60);
            response.addCookie(loggedInUserCookie);
            SplashPageController.setLoggedInUser(null);

        //Determine current year and month
        Calendar cal = Calendar.getInstance();
        Integer month = cal.get(Calendar.MONTH);
        Integer year = cal.get(Calendar.YEAR);

        //Determine which BudgetMonth to use, or create one as necessary
        BudgetMonth thisBudgetMonth = null;
        for (BudgetMonth budgetMonth : budgetMonthDao.findAll()) {
            if (budgetMonth.getMonth().equals(month) && budgetMonth.getYear().equals(year)) {
                thisBudgetMonth = budgetMonth;
            }
        }
        if (thisBudgetMonth == null) {
            thisBudgetMonth = new BudgetMonth(month, year);
            budgetMonthDao.save(thisBudgetMonth);
        }

        int thisBudgetMonthId = thisBudgetMonth.getId();
        Cookie thisBudgetMonthCookie = new Cookie("thisBudgetMonth", Integer.toString(thisBudgetMonthId));
        response.addCookie(thisBudgetMonthCookie);

        return "splashpage";
    }
}