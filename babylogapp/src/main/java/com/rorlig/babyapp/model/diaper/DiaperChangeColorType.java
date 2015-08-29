package com.rorlig.babyapp.model.diaper;

/**
 * @author gaurav gupta
 * Color of Poop
 */
public enum  DiaperChangeColorType {
    COLOR_1("COLOR_1"),
    COLOR_2("COLOR_2"),
    COLOR_3("COLOR_3"),
    COLOR_4("COLOR_4"),
    COLOR_5("COLOR_5"),
    COLOR_6("COLOR_6"),
    COLOR_7("COLOR_7"), COLOR_8("COLOR_8"),
    NOT_SPECIFIED("NOT_SPECIFIED");

    private final String value;

    DiaperChangeColorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
