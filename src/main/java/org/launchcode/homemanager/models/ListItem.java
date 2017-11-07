package org.launchcode.homemanager.models;

/**
 * Created by schwifty on 11/6/17.
 */
public class ListItem {

    private String content;
    private int id;
    private static int nextId = 1;

    public ListItem(String content) {
        this();
        this.content = content;
    }

    public ListItem() {
        this.id = nextId;
        nextId++;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
