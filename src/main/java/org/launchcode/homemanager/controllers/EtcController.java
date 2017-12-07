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
public class EtcController {

    @Autowired
    BudgetMonthDao budgetMonthDao;

    @RequestMapping(value = "etc", method = RequestMethod.GET)
    public String displayEtc(Model model,
                             @CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                             @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }
        Integer thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double etc = thisBudgetMonth.getEtc();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String etcString = formatter.format(etc);

        model.addAttribute("title", "Other");
        model.addAttribute("etcAmount", etcString);
        return "budget/etc";
    }

    @RequestMapping(value = "etc", method = RequestMethod.POST, params = {"update"})
    public String processEtc(@CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                             @RequestParam String updateAmountString) {
        int thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double updateAmount = Double.parseDouble(updateAmountString);
        thisBudgetMonth.setEtc(updateAmount);
        budgetMonthDao.save(thisBudgetMonth);
        return "redirect:/budget";
    }

    @RequestMapping(value = "etc", method = RequestMethod.POST, params = {"logout"})
    public String logoutEtc(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}