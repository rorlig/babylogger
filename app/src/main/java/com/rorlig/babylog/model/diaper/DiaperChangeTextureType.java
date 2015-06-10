package com.rorlig.babylog.model.diaper;

/**
 * Created by rorlig on 8/10/14.
 */
public enum DiaperChangeTextureType {
    LOOSE("Loose"), SEEDY("Seedy"), CHUNKY("Chunky"), HARD("Hard");

    private final String value;

    DiaperChangeTextureType(String value) {
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
