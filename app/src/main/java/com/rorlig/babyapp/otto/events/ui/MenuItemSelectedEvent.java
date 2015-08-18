package com.rorlig.babyapp.otto.events.ui;

/**
 * Created by gaurav
 */
public class MenuItemSelectedEvent {

    private final int menuId;

    public MenuItemSelectedEvent(int menuId) {
        this.menuId = menuId;
    }

    public int getMenuId() {
        return menuId;
    }

}
