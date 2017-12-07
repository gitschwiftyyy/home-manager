package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;

@Controller
public class InternetController {

    @Autowired
    BudgetMonthDao budgetMonthDao;

    @RequestMapping(value = "internet", method = RequestMethod.GET)
    public String displayInternet(Model model,
                                  @CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                                  @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }
        Integer thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double internet = thisBudgetMonth.getInternet();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String internetString = formatter.format(internet);

        model.addAttribute("title", "Internet");
        model.addAttribute("internetAmount", internetString);
        return "budget/internet";
    }

    @RequestMapping(value = "internet", method = RequestMethod.POST, params = {"update"})
    public String processInternet(@CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                                  @RequestParam String updateAmountString) {
        int thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double updateAmount = Double.parseDouble(updateAmountString);
        thisBudgetMonth.setInternet(updateAmount);
        budgetMonthDao.save(thisBudgetMonth);
        return "redirect:/budget";
    }

    @RequestMapping(value = "internet", method = RequestMethod.POST, params = {"logout"})
    public String logoutInternet(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}