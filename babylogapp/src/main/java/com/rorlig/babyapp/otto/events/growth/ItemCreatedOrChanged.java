package com.rorlig.babyapp.otto.events.growth;

/**
 * Created by rorlig on 6/9/15.
 */
public class ItemCreatedOrChanged {

    private final String parseClassName;
    private final int position;

    public ItemCreatedOrChanged(String parseClassName) {
        this.parseClassName = parseClassName;
        position = -1;
    }

    public ItemCreatedOrChanged(String parseClassName, int position) {
        this.parseClassName = parseClassName;

        this.position = position;
    }

    public String getParseClassName() {
        return parseClassName;
    }

    public int getPosition() {
        return position;
    }
}

