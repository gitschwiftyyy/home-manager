package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.Task;
import org.launchcode.homemanager.models.data.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by schwifty on 11/6/17.
 */
@Controller
@RequestMapping(value = "task")
public class TaskController {

    @Autowired
    private TaskDao taskDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayAddTaskForm(Model model) {

        model.addAttribute("task", new Task());
        model.addAttribute("tasks", taskDao.findAll());
        model.addAttribute("title", "Add Task");
        return "tasks/add";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processAddTaskForm(@ModelAttribute Task newTask, Model model) {
        taskDao.save(newTask);
        return "redirect:/task";
    }
}
