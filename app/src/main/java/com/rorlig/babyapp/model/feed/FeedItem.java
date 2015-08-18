package com.rorlig.babyapp.model.feed;

/**
 * @author gaurav gupta
 * FeedItem type...
 */
public enum FeedItem {

    BREAST_MILK("breast milk"), FORMULA ("formula"), WATER("water"), JUICE("juice");

    private final String value;

    FeedItem(String value) {
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
