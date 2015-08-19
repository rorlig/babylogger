package com.rorlig.babyapp.ui.fragment.diaper;

/**
 * @author gaurav gupta
 */
public enum DiaperChangeStatsType {
    WEEKLY("Week"), MONTHLY("Month"), YEARLY("Year");

    private final String value;

    DiaperChangeStatsType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
