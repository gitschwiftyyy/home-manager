package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.MailService;
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

@Controller
public class RentController {

    @Autowired
    BudgetMonthDao budgetMonthDao;

    @Autowired
    UserDao userDao;

    @Autowired
    MailService mailService;

    @RequestMapping(value = "/budget/{year}/{month}/rent", method = RequestMethod.GET)
    public String displayRent(Model model,
                              @PathVariable String year,
                              @PathVariable String month,
                              @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }
        BudgetMonth thisBudgetMonth = budgetMonthDao.findByYearAndMonth(Integer.parseInt(year), BudgetMonth.monthInt(month));
        Double rent = thisBudgetMonth.getRent();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String rentString = formatter.format(rent);

        model.addAttribute("title", "Rent");
        model.addAttribute("rentAmount", rentString);
        return "budget/rent";
    }

    @RequestMapping(value = "/budget/{year}/{month}/rent", method = RequestMethod.POST, params = {"update"})
    public String processRent(@PathVariable String year,
                              @PathVariable String month,
                              @RequestParam String updateAmountString,
                              @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        int userId = Integer.parseInt(loggedInUserId);
        User thisUser = userDao.findOne(userId);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        BudgetMonth thisBudgetMonth = budgetMonthDao.findByYearAndMonth(Integer.parseInt(year), BudgetMonth.monthInt(month));
        Double updateAmount = Double.parseDouble(updateAmountString);
        thisBudgetMonth.setRent(updateAmount);
        budgetMonthDao.save(thisBudgetMonth);


        String emailSubject = "Budget updated for " + month + " " + year;
        String emailText = thisUser.getName() + " updated " + month + "'s rent amount to " + formatter.format(updateAmount);
        mailService.sendToAll(emailSubject, emailText);

        return "redirect:/budget/" + year + "/" + month;
    }


}