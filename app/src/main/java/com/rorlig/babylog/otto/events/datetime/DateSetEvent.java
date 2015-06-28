package com.rorlig.babylog.otto.events.datetime;

import java.util.Calendar;

/**
 * Created by gaurav
 */
public class DateSetEvent {

    private final Calendar calendar;

    private String label;


    public DateSetEvent(Calendar calendar) {
        this.calendar = calendar;
    }

    public DateSetEvent(Calendar calendar, String label) {
        this.calendar = calendar;
        this.label = label;

    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "DateSetEvent{" +
                "calendar=" + calendar +
                ", label='" + label + '\'' +
                '}';
    }
}
