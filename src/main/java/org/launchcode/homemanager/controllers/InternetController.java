package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;

@Controller
public class InternetController {

    @Autowired
    BudgetMonthDao budgetMonthDao;

    @RequestMapping(value = "/budget/{year}/{month}/internet", method = RequestMethod.GET)
    public String displayInternet(Model model,
                                  @PathVariable String year,
                                  @PathVariable String month,
                                  @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }
        BudgetMonth thisBudgetMonth = budgetMonthDao.findByYearAndMonth(Integer.parseInt(year), BudgetMonth.monthInt(month));
        Double internet = thisBudgetMonth.getInternet();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String internetString = formatter.format(internet);

        model.addAttribute("title", "Internet");
        model.addAttribute("internetAmount", internetString);
        return "budget/internet";
    }

    @RequestMapping(value = "/budget/{year}/{month}/internet", method = RequestMethod.POST, params = {"update"})
    public String processInternet(@PathVariable String year,
                                  @PathVariable String month,
                                  @RequestParam String updateAmountString) {
        BudgetMonth thisBudgetMonth = budgetMonthDao.findByYearAndMonth(Integer.parseInt(year), BudgetMonth.monthInt(month));
        Double updateAmount = Double.parseDouble(updateAmountString);
        thisBudgetMonth.setInternet(updateAmount);
        budgetMonthDao.save(thisBudgetMonth);
        return "redirect:/budget";
    }

}