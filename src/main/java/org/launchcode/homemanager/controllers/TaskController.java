package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by schwifty on 10/28/17.
 */
@Controller
public class TaskController {


    //May need to be moved to a "IndexControllerClass"
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("title", "Tasks");
        return "index";

    }
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddTaskForm(Model model) {

        model.addAttribute("task", new Task());
        model.addAttribute("title", "Add Task");
        return "tasks/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddTaskForm()
}
