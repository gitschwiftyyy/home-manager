package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.ListItemData;
import org.launchcode.homemanager.models.Task;
import org.launchcode.homemanager.models.TaskData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by schwifty on 10/28/17.
 */
@Controller
public class IndexController {


    //May need to be moved to a "IndexControllerClass"
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("title", "Tasks");
        model.addAttribute("tasks", TaskData.getAll());
        model.addAttribute("shoppingList", ListItemData.getAll());
        return "index";

    }

}
