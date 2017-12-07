package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.Task;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.TaskDao;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayAddTaskForm(Model model,
                                     HttpServletResponse response,
                                     @CookieValue(value = "loggedInCookie", required = false) String loggedInUserId) {
        if (loggedInUserId == "" || loggedInUserId == null) {
            return "redirect:/user/login";
        }

        int userId = Integer.parseInt(loggedInUserId);
        User loggedInUser = userDao.findOne(userId);

        model.addAttribute("task", new Task());
        model.addAttribute("tasks", taskDao.findAll());
        model.addAttribute("title", "Add Task");
        model.addAttribute("user", loggedInUser.getName());
        return "tasks/add";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"postTask"})
    public String processAddTaskForm(@ModelAttribute Task newTask, Model model) {
        taskDao.save(newTask);
        return "redirect:/task";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params = {"logout"})
    public String logout(HttpServletResponse response) {
        Cookie logoutCookie = new Cookie("loggedInCookie", "");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        return "redirect:/user/login";
    }
}
