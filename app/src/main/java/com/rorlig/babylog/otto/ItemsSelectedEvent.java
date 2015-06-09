package com.rorlig.babylog.otto;

import com.rorlig.babylog.model.ItemModel;

import java.util.ArrayList;

/**
 * Created by rorlig on 5/31/15.
 */
public class ItemsSelectedEvent {
    private final ArrayList<ItemModel> logListItem;
    private final String name;
    private final String dob;


    public ItemsSelectedEvent(ArrayList<ItemModel> logListItem, String name, String dob) {
        this.logListItem = logListItem;
        this.name = name;
        this.dob = dob;
    }

    public ArrayList<ItemModel> getLogListItem() {
        return logListItem;
    }


    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }
}
