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
public class GasController {

    @Autowired
    BudgetMonthDao budgetMonthDao;

    @RequestMapping(value = "gas", method = RequestMethod.GET)
    public String displayGas(Model model,
                             @CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                             @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }
        Integer thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double gas = thisBudgetMonth.getGas();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String gasString = formatter.format(gas);

        model.addAttribute("title", "Gas");
        model.addAttribute("gasAmount", gasString);
        return "budget/gas";
    }

    @RequestMapping(value = "gas", method = RequestMethod.POST, params = {"update"})
    public String processGas(@CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                             @RequestParam String updateAmountString) {
        int thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double updateAmount = Double.parseDouble(updateAmountString);
        thisBudgetMonth.setGas(updateAmount);
        budgetMonthDao.save(thisBudgetMonth);
        return "redirect:/budget";
    }

    @RequestMapping(value = "gas", method = RequestMethod.POST, params = {"logout"})
    public String logoutGas(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}