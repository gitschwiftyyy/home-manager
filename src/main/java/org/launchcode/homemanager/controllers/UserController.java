package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("title", "User");

        return "user/index";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLogin(Model model) {
        model.addAttribute("title", "Login");

        return "user/login";
    }

    //TODO: validate entered login info

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLogin(@RequestParam String name, @RequestParam String password) {

        return "redirect:";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String displayRegister(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("usernameError", "Cats!");

        return "user/register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String processRegister(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  @RequestParam String email,
                                  Model model) {

        User newUser = new User();

        boolean usernameTaken = false;
        boolean passwordsMatch = false;

        String usernameError = "";
        String passwordError = "";

        for (User user : userDao.findAll()) {
            if (username == user.getName()) {
                usernameTaken = true;
                usernameError = "Username already taken!";
            }

        }
        //TODO: change to hash
        if (password == confirmPassword) {
            passwordsMatch = true;
        } else {
            passwordError = "Passwords do not match";
        }

        if (!usernameTaken && passwordsMatch) {
            newUser.setName(username);
            newUser.setPasswordHash(password); //TODO: change to hash
            newUser.setEmail(email);

            userDao.save(newUser);

            return "redirect:";
        } else {
            model.addAttribute("usernameError", usernameError);
            model.addAttribute("passwordError", passwordError);

            return "user/register";
        }

    }
}