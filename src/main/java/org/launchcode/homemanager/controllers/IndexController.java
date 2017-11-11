package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.data.ListItemDao;
import org.launchcode.homemanager.models.data.MessageDao;
import org.launchcode.homemanager.models.data.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by schwifty on 10/28/17.
 */
@Controller
public class IndexController {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ListItemDao listItemDao;

    @Autowired
    private MessageDao messageDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("title", "Tasks");
        model.addAttribute("tasks", taskDao.findAll());
        model.addAttribute("shoppingList", listItemDao.findAll());
        model.addAttribute("messages", messageDao.findAll());
        return "index";

    }

}
