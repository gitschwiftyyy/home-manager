package org.launchcode.homemanager.controllers;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.ListItem;
import org.launchcode.homemanager.models.Payment;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.text.Format;
import java.text.NumberFormat;

/**
 * Created by schwifty on 10/28/17.
 */
@Controller
@RequestMapping(value = "dashboard")
public class IndexController {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ListItemDao listItemDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BudgetMonthDao budgetMonthDao;

    @Autowired
    private PaymentDao paymentDao;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model,
                        @CookieValue(value = "loggedInCookie", required = false) String loggedInCookieString,
                        @CookieValue(value = "thisBudgetMonth", required = false) String thisBudgetMonthId) {

        if (loggedInCookieString == "" || loggedInCookieString == null) {
            return "redirect:/user/login";
        }

        User loggedInUser = userDao.findOne(Integer.parseInt(loggedInCookieString));
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(Integer.parseInt(thisBudgetMonthId));

        //Determine number of users
        int numberOfUsers = 0;
        for (User user : userDao.findAll()) {
            numberOfUsers ++;
        }
        //Determine which Payment to use, or create one accordingly
        Payment thisPayment = paymentDao.findByUserAndBudgetMonth(loggedInUser, thisBudgetMonth);
        if (thisPayment == null) {
            thisPayment = new Payment();
            thisPayment.setUser(loggedInUser);
            thisPayment.setBudgetMonth(thisBudgetMonth);
            paymentDao.save(thisPayment);
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Double owes = (thisBudgetMonth.total() / numberOfUsers) - thisPayment.getAmount();
        String owesString = formatter.format(owes);

        model.addAttribute("title", "Dashboard");
        model.addAttribute("tasks", taskDao.findAll());
        model.addAttribute("shoppingList", listItemDao.findAll());
        model.addAttribute("messages", messageDao.findAll());
        model.addAttribute("user", "Welcome, " + loggedInUser.getName());
        model.addAttribute("year", Integer.toString(thisBudgetMonth.getYear()));
        model.addAttribute("month", thisBudgetMonth.monthName(thisBudgetMonth.getMonth()));
        model.addAttribute("owes", "You Owe: " + owesString);
        return "index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"deleteListItem"})
    public String deleteListItem(@RequestParam int itemId) {
        ListItem itemToBeDeleted = listItemDao.findOne(itemId);
        listItemDao.delete(itemToBeDeleted);

        return "redirect:";

    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"logout"})
    public String logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }

}
