package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.BudgetMonth;
import org.launchcode.homemanager.models.MailService;
import org.launchcode.homemanager.models.Task;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.BudgetMonthDao;
import org.launchcode.homemanager.models.data.TaskDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by schwifty on 11/6/17.
 */
@Controller
@RequestMapping(value = "task")
public class TaskController {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BudgetMonthDao budgetMonthDao;

    @Autowired
    private MailService mailService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayAddTaskForm(Model model,
                                     @CookieValue(value = "thisBudgetMonth", required = false) String thisBudgetMonthId,
                                     @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }

        int userId = Integer.parseInt(loggedInUserId);
        User loggedInUser = userDao.findOne(userId);
        BudgetMonth thisBudgetMonth = budgetMonthDao.findOne(Integer.parseInt(thisBudgetMonthId));


        model.addAttribute("task", new Task());
        model.addAttribute("tasks", taskDao.findAll());
        model.addAttribute("title", "Add Task");
        model.addAttribute("user", loggedInUser.getName());
        model.addAttribute("year", Integer.toString(thisBudgetMonth.getYear()));
        model.addAttribute("month", thisBudgetMonth.monthName(thisBudgetMonth.getMonth()));
        return "tasks/add";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"postTask"})
    public String processAddTaskForm(@ModelAttribute Task newTask, Model model,
                                     @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserId));
        taskDao.save(newTask);
        
        String emailSubject = "Home Manager Task List Updated";
        String emailText = thisUser.getName() + " added " + newTask.getName() + " to the task list";
        mailService.sendToAll(emailSubject, emailText);

        return "redirect:/task";
    }


}
