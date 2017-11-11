package org.launchcode.homemanager.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by schwifty on 11/10/17.
 */
@Entity
public class Message {

    private String message;

    @Id
    @GeneratedValue
    private int id;

    public Message(String message) {
        this.message = message;
    }

    public Message() {}

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
