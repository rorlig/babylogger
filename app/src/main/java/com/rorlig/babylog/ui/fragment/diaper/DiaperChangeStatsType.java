package com.rorlig.babylog.ui.fragment.diaper;

/**
 * @author gaurav gupta
 */
public enum DiaperChangeStatsType {
    DAY("Day"), WEEK("Week"), MONTH("Month");

    private final String value;

    DiaperChangeStatsType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
