package com.rorlig.babyapp.otto;

/**
 * @author gaurav gupta
 */
public class TimersStartEvent {

    private String tag;

    public TimersStartEvent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
