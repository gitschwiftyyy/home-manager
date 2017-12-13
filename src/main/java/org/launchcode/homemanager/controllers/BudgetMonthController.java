package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.Payment;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.launchcode.homemanager.models.data.PaymentDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;

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

    @Autowired
    PaymentDao paymentDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayBudgetRedirect(@CookieValue(value = "thisBudgetMonth", required = false) String thisBudgetMonthIdString,
                                        @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {

        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }
        Integer thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        String year = Integer.toString(thisBudgetMonth.getYear());
        String month = BudgetMonth.monthName(thisBudgetMonth.getMonth());
        return "redirect:/budget/" + year + "/" + month;
    }

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
            budgetMonthDao.save(thisBudgetMonth);
        }

        //Determine number of users
        int numberOfUsers = 0;
        for (User user : userDao.findAll()) {
            numberOfUsers ++;
        }

        Payment thisPayment = paymentDao.findByUserAndBudgetMonth(thisUser, thisBudgetMonth);
        if (thisPayment == null) {
            thisPayment = new Payment();
            thisPayment.setUser(thisUser);
            thisPayment.setBudgetMonth(thisBudgetMonth);
            paymentDao.save(thisPayment);
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
        String paymentString = formatter.format(thisPayment.getAmount());
        String owedString = formatter.format((thisBudgetMonth.total()/numberOfUsers) - thisPayment.getAmount());


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

        Integer currentMonth = budgetMonthDao.findOne(Integer.parseInt(thisBudgetMonthId)).getMonth();
        Integer currentYear = budgetMonthDao.findOne(Integer.parseInt(thisBudgetMonthId)).getYear();

        model.addAttribute("title", month + ", " + year);
        model.addAttribute("user", thisUser.getName());
        model.addAttribute("rent", rentString);
        model.addAttribute("electric", electricString);
        model.addAttribute("gas", gasString);
        model.addAttribute("internet", internetString);
//        model.addAttribute("water", waterString);
        model.addAttribute("etc", etcString);
        model.addAttribute("total", totalString);
        model.addAttribute("perPerson", perPersonString);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("prevMonth", BudgetMonth.monthName(prevMonth));
        model.addAttribute("prevYear", Integer.toString(prevYear));
        model.addAttribute("nextMonth", BudgetMonth.monthName(nextMonth));
        model.addAttribute("nextYear", Integer.toString(nextYear));
        model.addAttribute("currentMonth", BudgetMonth.monthName(currentMonth));
        model.addAttribute("currentYear", Integer.toString(currentYear));
        model.addAttribute("paymentAmount", paymentString);
        model.addAttribute("owed", "You Owe: " + owedString);

        return "budget/index";
    }

    @RequestMapping(value = "/{year}/{month}", method = RequestMethod.POST, params = {"payment"})
    public String processPayment(@CookieValue(value = "loggedInCookie") String loggedInUserId,
                                 @PathVariable String year,
                                 @PathVariable String month,
                                 @RequestParam String paymentAmount) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserId));
        BudgetMonth thisBudgetMonth = budgetMonthDao.findByYearAndMonth(Integer.parseInt(year), BudgetMonth.monthInt(month));
        Payment thisPayment = paymentDao.findByUserAndBudgetMonth(thisUser, thisBudgetMonth);
        Double thisAmount = Double.parseDouble(paymentAmount);
        thisPayment.setAmount(thisPayment.getAmount() + thisAmount);
        paymentDao.save(thisPayment);

        return "redirect:/budget/" + year + "/" + month;
    }

    @RequestMapping(value = "/{year}/{month}", method = RequestMethod.POST, params = {"logout"})
    public String logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}
