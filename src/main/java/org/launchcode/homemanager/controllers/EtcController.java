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
public class EtcController {

    @Autowired
    BudgetMonthDao budgetMonthDao;

    @RequestMapping(value = "/budget/{year}/{month}/etc", method = RequestMethod.GET)
    public String displayEtc(Model model,
                             @PathVariable String year,
                             @PathVariable String month,
                             @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }
        BudgetMonth thisBudgetMonth = budgetMonthDao.findByYearAndMonth(Integer.parseInt(year), BudgetMonth.monthInt(month));
        Double etc = thisBudgetMonth.getEtc();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String etcString = formatter.format(etc);

        model.addAttribute("title", "Other");
        model.addAttribute("etcAmount", etcString);
        return "budget/etc";
    }

    @RequestMapping(value = "/budget/{year}/{month}/etc", method = RequestMethod.POST, params = {"update"})
    public String processEtc(@PathVariable String year,
                             @PathVariable String month,
                             @RequestParam String updateAmountString) {
        BudgetMonth thisBudgetMonth = budgetMonthDao.findByYearAndMonth(Integer.parseInt(year), BudgetMonth.monthInt(month));
        Double updateAmount = Double.parseDouble(updateAmountString);
        thisBudgetMonth.setEtc(updateAmount);
        budgetMonthDao.save(thisBudgetMonth);
        return "redirect:/budget";
    }

}