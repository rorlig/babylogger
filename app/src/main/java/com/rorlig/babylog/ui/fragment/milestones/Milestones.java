package com.rorlig.babylog.ui.fragment.milestones;

/**
 * Created by rorlig on 6/11/15.
 */
public class Milestones {
    private String mileStoneTitle;
    private boolean completed;

    public Milestones(String mileStoneTitle, boolean completed) {
        this.mileStoneTitle = mileStoneTitle;
        this.completed = completed;
    }

    public String getMileStoneTitle() {
        return mileStoneTitle;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
