package com.rorlig.babylog.otto.events.ui;

/**
 * Created by gaurav
 */
public class ActionSelectItem {
    private int id;
    public ActionSelectItem(int position) {
        id = position;
    }

    public int getId() {
        return id;
    }
}
