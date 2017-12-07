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

    @RequestMapping(value = "electric", method = RequestMethod.GET)
    public String displayElectric(Model model,
                                  @CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                                  @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }
        Integer thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double electric = thisBudgetMonth.getElectric();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String electricString = formatter.format(electric);

        model.addAttribute("title", "Electric");
        model.addAttribute("electricAmount", electricString);
        return "budget/electric";
    }

    @RequestMapping(value = "electric", method = RequestMethod.POST, params = {"update"})
    public String processElectric(@CookieValue(value = "thisBudgetMonth") String thisBudgetMonthIdString,
                                  @RequestParam String updateAmountString) {
        int thisBudgetMonthId = Integer.parseInt(thisBudgetMonthIdString);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(thisBudgetMonthId);
        Double updateAmount = Double.parseDouble(updateAmountString);
        thisBudgetMonth.setElectric(updateAmount);
        budgetMonthDao.save(thisBudgetMonth);
        return "redirect:/budget";
    }

    @RequestMapping(value = "electric", method = RequestMethod.POST, params = {"logout"})
    public String logoutElectric(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }

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

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"logout"})
    public String logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}
