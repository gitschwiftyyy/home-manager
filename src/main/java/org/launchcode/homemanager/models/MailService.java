package org.launchcode.homemanager.models;

import org.launchcode.homemanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MailService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MailSender mailSender;


    private String[] findEmails () {
        ArrayList<String> users = new ArrayList();
        for (User user : userDao.findAll()) {
            users.add(user.getEmail());
        }
        String[] userEmails = users.toArray(new String[users.size()]);

        return userEmails;
    }

    public void send(String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        String[] toEmails = findEmails();
        email.setTo(toEmails);
        email.setSubject(subject);
        email.setText(text);

        mailSender.send(email);
    }

    public void send(User user, String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        String to = user.getEmail();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);

        mailSender.send(email);
    }
}