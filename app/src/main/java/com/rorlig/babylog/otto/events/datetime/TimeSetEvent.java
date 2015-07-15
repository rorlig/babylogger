package com.rorlig.babylog.otto.events.datetime;

/**
 * Created by gaurav
 */
public class TimeSetEvent {


    private final int hourOfDay;
    private final int minute;

    private final String label;


    public TimeSetEvent(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.label="";
    }


    public TimeSetEvent(int hourOfDay, int minute, String label) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.label = label;
    }


    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "TimeSetEvent{" +
                "hourOfDay=" + hourOfDay +
                ", minute=" + minute +
                ", label='" + label + '\'' +
                '}';
    }
}
