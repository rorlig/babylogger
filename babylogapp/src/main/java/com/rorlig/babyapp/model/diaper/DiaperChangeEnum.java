package com.rorlig.babyapp.model.diaper;

/**
 * @author
 * Enum to hold type of diaper change event
 */
public enum DiaperChangeEnum {
    POOP("Poop"), WET ("Wet"), BOTH ("Both");


    private final String value;

    DiaperChangeEnum(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value.toString();
    }
}
