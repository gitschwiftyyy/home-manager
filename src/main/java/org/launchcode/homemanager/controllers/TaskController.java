package org.launchcode.homemanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by schwifty on 10/28/17.
 */
@Controller
public class TaskController {

    @RequestMapping
    public String index(Model model) {

        model.addAttribute("title", "Tasks");
        return "index";

    }

}
