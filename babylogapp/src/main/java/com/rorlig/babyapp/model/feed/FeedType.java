package com.rorlig.babyapp.model.feed;

/**
 * @author gaurav gupta
 * Feed Type
 */
public enum FeedType {

    BOTTLE ("bottle"), BREAST ("breast");

    private final String value;

    FeedType(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        return getValue();
    }
}
