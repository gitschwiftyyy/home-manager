package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.Message;
import org.launchcode.homemanager.models.data.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by schwifty on 11/10/17.
 */
@Controller
@RequestMapping(value = "messages")
public class MessageController {

    @Autowired
    private MessageDao messageDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayMessages(Model model) {
        model.addAttribute("title", "Message Board");
//        model.addAttribute("message", new Message());
        model.addAttribute("messages", messageDao.findAll());

        return "messages/messages";
    }


    //For some inexplicable reason, this handler REFUSED to work with model binding,
    //so I had to make it work the old-fashioned way. Idk it works now, so I've stopped
    //questioning it for the time being
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processMessages(@RequestParam String message ) {
        Message newMessage = new Message(message);

        messageDao.save(newMessage);

        return "redirect:";
    }
}
