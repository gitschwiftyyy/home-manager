package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.ListItem;
import org.launchcode.homemanager.models.data.ListItemDao;
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
@RequestMapping(value = "shopping-list")
public class ShoppingListController {

    @Autowired
    private ListItemDao listItemDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayShoppingList (Model model) {

        model.addAttribute("items", listItemDao.findAll());
        model.addAttribute("listItem", new ListItem());
        model.addAttribute("title", "Shopping List");

        return "list-items/shopping-list";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addListItem(@ModelAttribute ListItem newListItem) {
        listItemDao.save(newListItem);
        return "redirect:";
    }
}
