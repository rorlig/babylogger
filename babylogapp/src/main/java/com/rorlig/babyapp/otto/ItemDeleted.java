package com.rorlig.babyapp.otto;

/**
 * Created by Gaurav on 9/16/15.
 * Event capturing the deletion of an object
 */
public class ItemDeleted {
    private final String parseClassName;
    private final int position;

    public ItemDeleted(String parseClassName, int position) {
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
