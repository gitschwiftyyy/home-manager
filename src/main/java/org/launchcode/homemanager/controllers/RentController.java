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
public class RentController {

    @Autowired
    BudgetMonthDao budgetMonthDao;

    @RequestMapping(value = "rent", method = RequestMethod.GET)
    public String displayRent(Model model,
                              HttpServletResponse response,
                              @CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                              @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }
        Integer thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double rent = thisBudgetMonth.getRent();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String rentString = formatter.format(rent);

        model.addAttribute("title", "Rent");
        model.addAttribute("rentAmount", rentString);
        return "budget/rent";
    }

    @RequestMapping(value = "rent", method = RequestMethod.POST, params = {"update"})
    public String processRent(@CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                              @RequestParam String updateAmountString) {
        int thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double updateAmount = Double.parseDouble(updateAmountString);
        thisBudgetMonth.setRent(updateAmount);
        budgetMonthDao.save(thisBudgetMonth);
        return "redirect:/budget";
    }

    @RequestMapping(value = "rent", method = RequestMethod.POST, params = {"logout"})
    public String logoutRent(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}