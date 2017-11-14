package org.launchcode.homemanager.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by schwifty on 11/13/17.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String password;
    private String email;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
