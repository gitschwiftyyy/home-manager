package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.ListItem;
import org.launchcode.homemanager.models.MailService;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.launchcode.homemanager.models.data.ListItemDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by schwifty on 11/6/17.
 */
@Controller
@RequestMapping(value = "shopping-list")
public class ShoppingListController {

    @Autowired
    private ListItemDao listItemDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BudgetMonthDao budgetMonthDao;

    @Autowired
    private MailService mailService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayShoppingList (Model model,
                                       @CookieValue(value = "thisBudgetMonth", required = false) String thisBudgetMonthId,
                                       @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {

        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }

        User loggedInUser = userDao.findOne(Integer.parseInt(loggedInUserId));
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(Integer.parseInt(thisBudgetMonthId));


        model.addAttribute("items", listItemDao.findAll());
        model.addAttribute("listItem", new ListItem());
        model.addAttribute("title", "Shopping List");
        model.addAttribute("user", loggedInUser.getName());
        model.addAttribute("year", Integer.toString(thisBudgetMonth.getYear()));
        model.addAttribute("month", thisBudgetMonth.monthName(thisBudgetMonth.getMonth()));

        return "list-items/shopping-list";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"add"})
    public String addItem (@ModelAttribute ListItem newListItem,
                           @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserId));
        listItemDao.save(newListItem);

        String emailSubject = "Home Manager Shopping List Updated";
        String emailText = thisUser.getName() + " added " + newListItem.getContent() + " to the shopping list";
        mailService.sendToAll(emailSubject, emailText);

        return "redirect:/shopping-list";

    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"delete"})
    public String deleteItem (@RequestParam int itemId,
                              @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {

        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserId));
        ListItem itemToBeDeleted = listItemDao.findOne(itemId);
        listItemDao.delete(itemToBeDeleted);

        String emailSubject = "Home Manager Shopping List Updated";
        String emailText = thisUser.getName() + " bought " + itemToBeDeleted.getContent() + " from the shopping list";
        mailService.sendToAll(emailSubject, emailText);
        return "redirect:/shopping-list";
    }


}
