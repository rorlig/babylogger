package com.rorlig.babylog.otto.events;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by gaurav
 */
public class DateSetEvent {

    private final Calendar calendar;


    public DateSetEvent(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
