package org.launchcode.homemanager.models;

import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;

public class MailService {

    @Autowired
    private UserDao userDao;

    private SimpleMailMessage email;

    private String fromEmail = "home.manager.test.1@gmail.com";

    private String toEmail;

    private Iterable users = userDao.findAll();
}