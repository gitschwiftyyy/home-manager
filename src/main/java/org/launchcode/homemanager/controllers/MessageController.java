package org.launchcode.homemanager.controllers;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.launchcode.homemanager.models.Message;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.MessageDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by schwifty on 11/10/17.
 */
@Controller
@RequestMapping(value = "messages")
public class MessageController {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayMessages(Model model,
                                  HttpServletResponse response,
                                  @CookieValue(value = "loggedInCookie", required = false) String loggedInUser) {
        if (loggedInUser == "" || loggedInUser == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("title", "Message Board");
        model.addAttribute("messages", messageDao.findAll());
//        Cookie loggedInCookie = MessageController.getLoggedInUser();
//        response.addCookie(loggedInCookie);

        return "messages/messages";
    }


    //For some inexplicable reason, this handler REFUSED to work with model binding,
    //so I had to make it work the old-fashioned way. Idk it works now, so I've stopped
    //questioning it for the time being
    @RequestMapping(value = "", method = RequestMethod.POST, params = {"postMessage"})
    public String processMessages(@RequestParam String message,
                                  @CookieValue(value = "loggedInCookie") String loggedInUserId) {
        int userId = Integer.parseInt(loggedInUserId);
        User thisUser = userDao.findOne(userId);
        Message newMessage = new Message(message);
        newMessage.setAuthor(thisUser);

        messageDao.save(newMessage);

        return "redirect:/messages";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"logout"})
    public String logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}
