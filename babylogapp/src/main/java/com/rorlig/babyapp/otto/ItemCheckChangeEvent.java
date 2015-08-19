package com.rorlig.babyapp.otto;

/**
 * Created by gaurav
 */
public class ItemCheckChangeEvent {

    private final int position;

    public ItemCheckChangeEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
