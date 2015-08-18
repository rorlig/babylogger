package com.rorlig.babyapp.otto;

/**
 * @author gaurav gupta
 */
public class RightEvent {
    private final String hours;
    private final String minutes;
    private final String seconds;

    public RightEvent(String hours, String minutes, String seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public String getHours() {
        return hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public String getSeconds() {
        return seconds;
    }
}
