package org.launchcode.homemanager.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by schwifty on 11/13/17.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Message> messages = new ArrayList<>();

    private String name;
    private String passwordHash;
    private String email;

    public User(String name, String passwordHash, String email) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public User(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
