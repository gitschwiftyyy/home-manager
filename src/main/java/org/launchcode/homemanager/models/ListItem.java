package org.launchcode.homemanager.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by schwifty on 11/6/17.
 */
@Entity
public class ListItem {

    private String content;

    @Id
    @GeneratedValue
    private int id;


    public ListItem(String content) {
        this.content = content;
    }

    public ListItem() {}



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

}
