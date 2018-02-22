package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "calendar")
public class CalendarController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BudgetMonthDao budgetMonthDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayCalendar (Model model,
                                   @CookieValue(value = "loggedInCookie", required = false) String loggedInCookieString,
                                   @CookieValue(value = "thisBudgetMonth", required = false) String thisBudgetMonthId) {

        if (loggedInCookieString == "" || loggedInCookieString == null) {
            return "redirect:/user/login";
        }

        User loggedInUser = userDao.findOne(Integer.parseInt(loggedInCookieString));
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(Integer.parseInt(thisBudgetMonthId));

        model.addAttribute("title", "Calendar");
        return "calendar/calendar";
    }
}