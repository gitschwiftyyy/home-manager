package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.ListItem;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.ListItemDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by schwifty on 11/6/17.
 */
@Controller
@RequestMapping(value = "shopping-list")
public class ShoppingListController extends MainController {

    @Autowired
    private ListItemDao listItemDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayShoppingList (Model model,
                                       HttpServletResponse response) {

        if (ShoppingListController.getLoggedInUser() == null) {
            return "redirect:/user/login";
        }
        Cookie loggedInCookie = ShoppingListController.getLoggedInUser();
        response.addCookie(loggedInCookie);
        int userId = Integer.parseInt(loggedInCookie.getValue());
        User loggedInUser = userDao.findOne(userId);

        model.addAttribute("items", listItemDao.findAll());
        model.addAttribute("listItem", new ListItem());
        model.addAttribute("title", "Shopping List");
        model.addAttribute("user", loggedInUser.getName());

        return "list-items/shopping-list";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"add"})
    public String addItem (@ModelAttribute ListItem newListItem) {

        listItemDao.save(newListItem);
        return "redirect:/shopping-list";

    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"delete"})
    public String deleteItem (@RequestParam int itemId) {

        ListItem itemToBeDeleted = listItemDao.findOne(itemId);
        listItemDao.delete(itemToBeDeleted);
        return "redirect:/shopping-list";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"logout"})
    public String logout(HttpServletResponse response) {
        ShoppingListController.setLoggedInUser(null);
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}
