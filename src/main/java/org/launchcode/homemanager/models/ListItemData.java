package org.launchcode.homemanager.models;

import java.util.ArrayList;

/**
 * Created by schwifty on 11/6/17.
 */
public class ListItemData {

    static ArrayList<ListItem> shoppingList = new ArrayList<>();

    public static void add(ListItem newListItem) {
        shoppingList.add(newListItem);
    }

    public static ArrayList<ListItem> getAll() {
        return shoppingList;
    }

    public static ListItem getById(int id) {
        ListItem theListItem = null;

        for (ListItem listItem : shoppingList) {
            if (listItem.getId() == id) {
                theListItem = listItem;
            }
        }

        return theListItem;
    }
}
