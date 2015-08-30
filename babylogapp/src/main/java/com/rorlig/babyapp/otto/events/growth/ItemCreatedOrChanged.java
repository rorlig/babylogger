package com.rorlig.babyapp.otto.events.growth;

/**
 * Created by rorlig on 6/9/15.
 */
public class ItemCreatedOrChanged {

    private final String parseClassName;

    public ItemCreatedOrChanged(String parseClassName){
        this.parseClassName = parseClassName;
    }

    public String getParseClassName() {
        return parseClassName;
    }
}
