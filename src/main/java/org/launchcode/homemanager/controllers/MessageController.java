package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.Message;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.launchcode.homemanager.models.data.MessageDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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

    @Autowired
    private BudgetMonthDao budgetMonthDao;

    @Autowired
    private MailSender mailSender;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayMessages(Model model,
                                  @CookieValue(value = "thisBudgetMonth", required = false) String thisBudgetMonthId,
                                  @CookieValue(value = "loggedInCookie", required = false) String loggedInUser) {
        if (loggedInUser == "" || loggedInUser == null) {
            return "redirect:/user/login";
        }
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(Integer.parseInt(thisBudgetMonthId));

        model.addAttribute("title", "Message Board");
        model.addAttribute("messages", messageDao.findAll());
        model.addAttribute("year", Integer.toString(thisBudgetMonth.getYear()));
        model.addAttribute("month", thisBudgetMonth.monthName(thisBudgetMonth.getMonth()));

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
        String fromEmail = "home.manager.test.1@gmail.com";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(fromEmail);
        email.setTo(thisUser.getEmail());
        email.setSubject("New Home Manager message from " + newMessage.getAuthor().getName());
        email.setText(newMessage.getMessage());

        mailSender.send(email);
        return "redirect:/messages";
    }


}
