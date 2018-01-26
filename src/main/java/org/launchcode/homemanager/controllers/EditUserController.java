package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.MailService;
import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.UserDao;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EditUserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MailService mailService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String displayUserPage (Model model,
                                   @CookieValue(value = "loggedInCookie", required = false) String loggedInUserIdString) {
        if (loggedInUserIdString == "" || loggedInUserIdString == null) {
            return "redirect:/user/login";
        }

        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserIdString));
        String username = thisUser.getName();
        String email = thisUser.getEmail();

        model.addAttribute("title", "Your Account");
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        return "user/userPage";

    }
    @RequestMapping(value = "user/username", method = RequestMethod.GET)
    public String displayUpdateUsername (Model model,
                                         @CookieValue(value = "loggedInCookie", required = false) String loggedInUserString) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserString));

        model.addAttribute("title", "Change Username");
        model.addAttribute("currentUsername", thisUser.getName());

        return  "user/username";
    }

    @RequestMapping(value = "user/username", method = RequestMethod.POST, params = {"updateUsername"})
    public String processUpdateUsername (@CookieValue(value = "loggedInCookie") String loggedInUserString,
                                         @RequestParam String newUsername,
                                         Model model) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserString));
        for (User user : userDao.findAll()) {
            if (newUsername.equals(user.getName())) {
                model.addAttribute("title", "Change Username");
                model.addAttribute("currentUsername", thisUser.getName());
                model.addAttribute("usernameError", "Username is already taken!");
                return "user/username";
            }
        }
        String emailSubject = "Account Update";
        String emailText = "You changed your username to " + newUsername;
        mailService.sendToOne(thisUser, emailSubject, emailText);
        String groupEmailText = thisUser.getName() + " changed their name to " + newUsername;
        mailService.sendToOthers(thisUser, emailSubject, groupEmailText);

        thisUser.setName(newUsername);
        userDao.save(thisUser);

        return "redirect:/user";
    }

    @RequestMapping(value = "user/password", method = RequestMethod.GET)
    public String displayUpdatePassword (Model model) {

        model.addAttribute("title", "Change Password");
        model.addAttribute("passwordCheck", "true");

        return "user/password";
    }

    @RequestMapping(value = "user/password", method = RequestMethod.POST, params = {"passwordCheck"})
    public String processPasswordCheck (Model model,
                                        @CookieValue(value = "loggedInCookie") String loggedInUserString,
                                        @RequestParam String password) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserString));
        for (User user : userDao.findAll()) {
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                model.addAttribute("title", "Change Password");
                model.addAttribute("updatePassword", "true");
                return "user/password";
            }
        }
        model.addAttribute("title", "Change Password");
        model.addAttribute("passwordCheck", "true");
        model.addAttribute("passwordError", "Incorrect password!");
        return "user/password";
    }

    @RequestMapping(value = "user/password", method = RequestMethod.POST, params = {"updatePassword"})
    public String processUpdatePassword (@CookieValue(value = "loggedInCookie") String loggedInUserString,
                                         @RequestParam String newPassword,
                                         @RequestParam String newPasswordConfirm,
                                         Model model) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserString));
        if (newPassword.equals(newPasswordConfirm)) {
            String newPasswordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            thisUser.setPasswordHash(newPasswordHash);
            userDao.save(thisUser);
            return "redirect:/user";
        } else {
            model.addAttribute("title", "Change Password");
            model.addAttribute("updatePassword", "true");
            model.addAttribute("passwordError", "Passwords do not match!");

            return "user/password";
        }

    }

    @RequestMapping(value = "user/email", method = RequestMethod.GET)
    public String displayUpdateEmail (Model model,
                                      @CookieValue(value = "loggedInCookie", required = false) String loggedInUserString) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserString));

        model.addAttribute("title", "Change Email");
        model.addAttribute("currentEmail", thisUser.getEmail());

        return "user/email";
    }
    @RequestMapping(value = "user/email", method = RequestMethod.POST, params = {"updateEmail"})
    public String processUpdateEmail (Model model,
                                      @CookieValue(value = "loggedInCookie") String loggedInUserString,
                                      @RequestParam String newEmail) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserString));
        for (User user : userDao.findAll()) {
            if (newEmail.equals(user.getEmail())) {
                model.addAttribute("title", "Change Email");
                model.addAttribute("currentEmail", thisUser.getEmail());
                model.addAttribute("emailError", "Email is already taken!");

                return "user/email";
            }
        }

        thisUser.setEmail(newEmail);
        userDao.save(thisUser);
        return "redirect:/user";
    }
}