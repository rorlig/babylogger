package com.rorlig.babylog.model.diaper;

/**
 * @author gaurav gupta
 * Incident Type
 */
public enum DiaperIncident {
    NONE ("None"), NO_DIAPER("No Diaper"), DIAPER_LEAK ("Diaper Leak");

    private final String value;

    DiaperIncident(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
