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
        ArrayList<String> users = new ArrayList<>();
        for (User user : userDao.findAll()) {
            users.add(user.getEmail());
        }
        String[] userEmails = users.toArray(new String[users.size()]);

        return userEmails;
    }

    private String[] findOthers (User inputUser) {
        ArrayList<String> others = new ArrayList<>();
        for (User user : userDao.findAll()) {
            if (!user.getEmail().equals(inputUser.getEmail())) {
                others.add(user.getEmail());
            }
        }
        String[] userEmails = others.toArray(new String[others.size()]);
        return userEmails;
    }

    public void sendToAll(String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(findEmails());
        email.setSubject(subject);
        email.setText(text);

        mailSender.send(email);
    }

    public void sendToOne(User user, String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        String to = user.getEmail();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);

        mailSender.send(email);
    }

    public void sendToOthers(User user, String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(findOthers(user));
        email.setSubject(subject);
        email.setText(text);

        mailSender.send(email);
    }
}