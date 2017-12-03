package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * Created by schwifty on 11/15/17.
 */
@Controller
@RequestMapping(value = "budget")
public class BudgetMonthController extends MainController {

    @Autowired
    BudgetMonthDao budgetMonthDao;

    @Autowired
    UserDao userDao;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayBudget(Model model, HttpServletResponse response) {
        if (BudgetMonthController.getLoggedInUser() == null) {
            return "redirect:/user/login";
        }

        Cookie loggedInCookie = BudgetMonthController.getLoggedInUser();
        response.addCookie(loggedInCookie);
        int thisUserId = Integer.parseInt(loggedInCookie.getValue());
        User thisUser = userDao.findOne(thisUserId);

        Calendar cal = Calendar.getInstance();
        Integer month = cal.get(Calendar.MONTH);
        String monthString = BudgetMonth.monthName(month);
        Integer year = cal.get(Calendar.YEAR);
        String yearString = Integer.toString(year);


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

        int numberOfUsers = 0;
        for (User user : userDao.findAll()) {
            numberOfUsers ++;
        }


        model.addAttribute("title", monthString + ", " + yearString);
        model.addAttribute("user", thisUser.getName());

        return "budget/index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"logout"})
    public String logout(HttpServletResponse response) {
        BudgetMonthController.setLoggedInUser(null);
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}
