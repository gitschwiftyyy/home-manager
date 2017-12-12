package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * Created by schwifty on 11/15/17.
 */
@Controller
@RequestMapping(value = "budget")
public class BudgetMonthController {

    @Autowired
    BudgetMonthDao budgetMonthDao;

    @Autowired
    UserDao userDao;


    @RequestMapping(value = "/{year}/{month}", method = RequestMethod.GET)
    public String displayBudget(Model model,
                                HttpServletResponse response,
                                @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId,
                                @CookieValue(value = "thisBudgetMonth", required = false) String thisBudgetMonthId,
                                @PathVariable String year,
                                @PathVariable String month) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }

        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserId));

        BudgetMonth thisBudgetMonth = budgetMonthDao.findByYearAndMonth(Integer.parseInt(year), BudgetMonth.monthInt(month));
        if (thisBudgetMonth == null) {
            thisBudgetMonth = new BudgetMonth(BudgetMonth.monthInt(month), Integer.parseInt(year));
        }
        String monthString = thisBudgetMonth.monthName(thisBudgetMonth.getMonth());
        String yearString = Integer.toString(thisBudgetMonth.getYear());

        //Determine number of users
        int numberOfUsers = 0;
        for (User user : userDao.findAll()) {
            numberOfUsers ++;
        }



        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String rentString = formatter.format(thisBudgetMonth.getRent());
        String electricString = formatter.format(thisBudgetMonth.getElectric());
        String gasString = formatter.format(thisBudgetMonth.getGas());
        String internetString = formatter.format(thisBudgetMonth.getInternet());
//        String waterString = formatter.format(thisBudgetMonth.getWater());
        String etcString = formatter.format(thisBudgetMonth.getEtc());
        String totalString = formatter.format(thisBudgetMonth.total());
        String perPersonString = formatter.format(thisBudgetMonth.total()/numberOfUsers);

        Integer prevMonth;
        Integer prevYear;
        if (thisBudgetMonth.getMonth() == 0) {
            prevMonth = 11;
            prevYear = thisBudgetMonth.getYear() - 1;
        } else {
            prevMonth = thisBudgetMonth.getMonth() - 1;
            prevYear = thisBudgetMonth.getYear();
        }

        Integer nextMonth;
        Integer nextYear;
        if (thisBudgetMonth.getMonth() == 11) {
            nextMonth = 0;
            nextYear = thisBudgetMonth.getYear() + 1;
        } else {
            nextMonth = thisBudgetMonth.getMonth() + 1;
            nextYear = thisBudgetMonth.getYear();
        }


        model.addAttribute("title", monthString + ", " + yearString);
        model.addAttribute("user", thisUser.getName());
        model.addAttribute("rent", rentString);
        model.addAttribute("electric", electricString);
        model.addAttribute("gas", gasString);
        model.addAttribute("internet", internetString);
//        model.addAttribute("water", waterString);
        model.addAttribute("etc", etcString);
        model.addAttribute("total", totalString);
        model.addAttribute("perPerson", perPersonString);
        model.addAttribute("prevMonth", BudgetMonth.monthName(prevMonth));
        model.addAttribute("prevYear", Integer.toString(prevYear));
        model.addAttribute("nextMonth", BudgetMonth.monthName(nextMonth));
        model.addAttribute("nextYear", Integer.toString(nextYear));

        return "budget/index";
    }

    @RequestMapping(value = "/{year}/{month}", method = RequestMethod.POST, params = {"logout"})
    public String logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}
