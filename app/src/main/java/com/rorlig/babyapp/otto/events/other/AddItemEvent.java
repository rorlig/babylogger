package com.rorlig.babyapp.otto.events.other;

/**
 * Created by gaurav
 */
public class AddItemEvent {

    private final AddItemTypes itemType;

    public AddItemEvent (AddItemTypes itemType) {
        this.itemType = itemType;
    }

    public AddItemTypes getItemType() {
        return itemType;
    }
}
