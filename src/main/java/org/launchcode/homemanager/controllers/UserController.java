package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.mindrot.jbcrypt.BCrypt;

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


    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {

        boolean usernameExists = false;
        boolean passwordCorrect = false;
        int userId;

        String usernameError = "";
        String passwordError = "";

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        for (User user : userDao.findAll()) {
            if (username.equals(user.getName())) {
                usernameExists = true;
                userId = user.getId();
                User thisUser = userDao.findOne(userId);
                if (BCrypt.checkpw(password, thisUser.getPasswordHash())) {
                    passwordCorrect = true;
                } else {
                    passwordError = "Incorrect Password";
                }
            } else {
                usernameError = "Invalid Username";
            }
        }

        if (usernameExists && passwordCorrect) {
            return "redirect:/"; //TODO: add cookies
        } else {
            model.addAttribute("usernameError", usernameError);
            model.addAttribute("passwordError", passwordError);
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            return "user/login";
        }

    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String displayRegister(Model model) {
        model.addAttribute("title", "Register");


        return "user/register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String processRegister(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  @RequestParam String email,
                                  Model model) {

        User newUser = new User();

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        String confirmPasswordHash = BCrypt.hashpw(confirmPassword, BCrypt.gensalt());


        boolean usernameTaken = false;
        boolean passwordsMatch = false;
        boolean emailTaken = false;

        String usernameError = "";
        String passwordError = "";
        String emailError = "";

        for (User user : userDao.findAll()) {
            if (username.equals(user.getName())) {
                usernameTaken = true;
                usernameError = "Username already taken!";
            }

        }

        for (User user: userDao.findAll()) {
            if (email.equals(user.getEmail())) {
                emailTaken = true;
                emailError = "Email already taken!";
            }
        }

        if (password.equals(confirmPassword)) {
            passwordsMatch = true;
        } else {
            passwordError = "Passwords do not match!";
        }

        if (!usernameTaken && passwordsMatch && !emailTaken) {
            newUser.setName(username);
            newUser.setPasswordHash(passwordHash);
            newUser.setEmail(email);

            userDao.save(newUser);

            return "redirect:";
        } else {
            if (passwordsMatch) {
                model.addAttribute("password", password);
                model.addAttribute("confirmPassword", confirmPassword);
            }
            model.addAttribute("usernameError", usernameError);
            model.addAttribute("passwordError", passwordError);
            model.addAttribute("emailError", emailError);
            model.addAttribute("username", username);
            model.addAttribute("email", email);

            return "user/register";
        }

    }
}