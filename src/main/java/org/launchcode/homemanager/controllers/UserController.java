package org.launchcode.homemanager.controllers;

import org.launchcode.homemanager.models.User;
import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "user")
public class UserController {

    public static User loggedInUser;

    @Autowired
    private UserDao userDao;



    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLogin(Model model,
                               HttpServletResponse response) {
        model.addAttribute("title", "Login");

        return "user/login";
    }


    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model,
                               HttpServletResponse response) {

        User thisUser = null;
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
                thisUser = userDao.findOne(userId);
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
            UserController.setLoggedInUser(thisUser);
            return "redirect:/";

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
                                  Model model,
                                  HttpServletResponse response) {

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

            return "redirect:/";
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
    @RequestMapping(value = "", method = RequestMethod.GET)
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
    @RequestMapping(value = "username", method = RequestMethod.GET)
    public String displayUpdateUsername (Model model,
                                         @CookieValue(value = "loggedInCookie", required = false) String loggedInUserString) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserString));

        model.addAttribute("title", "Change Username");
        model.addAttribute("currentUsername", thisUser.getName());

        return  "user/username";
    }

    @RequestMapping(value = "username", method = RequestMethod.POST, params = {"updateUsername"})
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

        thisUser.setName(newUsername);
        userDao.save(thisUser);
        return "redirect:/user";
    }

    @RequestMapping(value = "password", method = RequestMethod.GET)
    public String displayUpdatePassword (Model model) {

        model.addAttribute("title", "Change Password");
        model.addAttribute("passwordCheck", "true");

        return "user/password";
    }

    @RequestMapping(value = "password", method = RequestMethod.POST, params = {"passwordCheck"})
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

    @RequestMapping(value = "password", method = RequestMethod.POST, params = {"updatePassword"})
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

    @RequestMapping(value = "email", method = RequestMethod.GET)
    public String displayUpdateEmail (Model model,
                                      @CookieValue(value = "loggedInCookie", required = false) String loggedInUserString) {
        User thisUser = userDao.findOne(Integer.parseInt(loggedInUserString));

        model.addAttribute("title", "Change Email");
        model.addAttribute("currentEmail", thisUser.getEmail());

        return "user/email";
    }
    @RequestMapping(value = "email", method = RequestMethod.POST, params = {"updateEmail"})
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

    

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        UserController.loggedInUser = loggedInUser;
    }

}