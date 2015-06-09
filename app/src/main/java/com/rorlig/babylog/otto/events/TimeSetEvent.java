package com.rorlig.babylog.otto.events;

/**
 * Created by gaurav
 */
public class TimeSetEvent {


    private final int hourOfDay;
    private final int minute;

    public TimeSetEvent(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

}
