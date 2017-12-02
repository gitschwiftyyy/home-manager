package org.launchcode.homemanager.models;

import javax.persistence.*;

/**
 * Created by schwifty on 11/10/17.
 */
@Entity
public class Message {

    private String message;

    @ManyToOne
    private User author;

    @Id
    @GeneratedValue
    private int id;

    public Message(String message) {
        this.message = message;
    }

    public Message() {}

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }



}
