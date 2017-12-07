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


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayBudget(Model model,
                                HttpServletResponse response,
                                @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }

        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserId));

        //Determine current year and month
        Calendar cal = Calendar.getInstance();
        Integer month = cal.get(Calendar.MONTH);
        String monthString = BudgetMonth.monthName(month);
        Integer year = cal.get(Calendar.YEAR);
        String yearString = Integer.toString(year);

        //Determine number of users
        int numberOfUsers = 0;
        for (User user : userDao.findAll()) {
            numberOfUsers ++;
        }

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


        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String rentString = formatter.format(thisBudgetMonth.getRent());
        String electricString = formatter.format(thisBudgetMonth.getElectric());
        String gasString = formatter.format(thisBudgetMonth.getGas());
        String internetString = formatter.format(thisBudgetMonth.getInternet());
//        String waterString = formatter.format(thisBudgetMonth.getWater());
        String etcString = formatter.format(thisBudgetMonth.getEtc());
        String totalString = formatter.format(thisBudgetMonth.total());
        String perPersonString = formatter.format(thisBudgetMonth.total()/numberOfUsers);

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

        return "budget/index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"logout"})
    public String logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}
