package org.launchcode.homemanager.controllers;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.launchcode.homemanager.models.ListItem;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.ListItemDao;
import org.launchcode.homemanager.models.data.MessageDao;
import org.launchcode.homemanager.models.data.TaskDao;
import org.launchcode.homemanager.models.data.UserDao;
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

/**
 * Created by schwifty on 10/28/17.
 */
@Controller
public class IndexController extends MainController {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ListItemDao listItemDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        if (IndexController.getLoggedInUser() == null) {
            return "redirect:/user/login";
        }
        Cookie loggedInCookie = IndexController.getLoggedInUser();
        response.addCookie(loggedInCookie);
        int userId = Integer.parseInt(loggedInCookie.getValue());
        User loggedInUser = userDao.findOne(userId);


        model.addAttribute("title", "Dashboard");
        model.addAttribute("tasks", taskDao.findAll());
        model.addAttribute("shoppingList", listItemDao.findAll());
        model.addAttribute("messages", messageDao.findAll());
        model.addAttribute("user", "Welcome, " + loggedInUser.getName());
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
        IndexController.setLoggedInUser(null);
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }

}
