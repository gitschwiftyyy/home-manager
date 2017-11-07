package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.ListItem;
import org.launchcode.homemanager.models.ListItemData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by schwifty on 11/6/17.
 */
@Controller
public class ShoppingListController {

    @RequestMapping(value = "shopping-list", method = RequestMethod.GET)
    public String displayShoppingList (Model model) {

        model.addAttribute("items", ListItemData.getAll());
        model.addAttribute("listItem", new ListItem());
        model.addAttribute("title", "Shopping List");

        return "list-items/shopping-list";
    }

    @RequestMapping(value = "shopping-list", method = RequestMethod.POST)
    public String addListItem(@ModelAttribute ListItem newListItem) {
        ListItemData.add(newListItem);
        return "redirect:";
    }
}
