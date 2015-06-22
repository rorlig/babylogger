package com.rorlig.babylog.model.diaper;

/**
* @author gaurav gupta
 * Poop Texture
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
