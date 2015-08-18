package com.rorlig.babyapp.ui.fragment.growth;

/**
 * @author gaurav gupta
 */
public enum  GrowthStatTab {
    WEIGHT("Weight"), HEIGHT("Height"), HEAD_MEASUREMENT("Head Measurement");

    private String value;

    GrowthStatTab(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
