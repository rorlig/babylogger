package com.rorlig.babyapp.ui.fragment.datetime;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.rorlig.babyapp.otto.TimeSetEventError;
import com.rorlig.babyapp.otto.events.datetime.TimeSetEvent;
import com.rorlig.babyapp.ui.fragment.InjectableDialogFragment;
import com.rorlig.babyapp.ui.widget.RangeTimePickerDialog;

import java.util.Calendar;

/**
 * Created by rorlig on 7/22/14.
 */
public class TimePickerFragment extends InjectableDialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private String TAG = "TimePickerFragment";
    private String label;
    private int minHour = 0;
    private int minMinute = 0;
    private int maxHour = 23;
    private int maxMinute=59;
    private int currentHour = 0;
    private int currentMinute =0;
    private RangeTimePickerDialog timePickerDialog;


    @NonNull
    @Override
    public RangeTimePickerDialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        currentHour = hour;
        currentMinute = minute;

        if (getArguments()!=null) {
//            startMaxDate = getArguments().getLong("max_start_date");


             label = getArguments().getString("label", "");

            Log.d(TAG, " label " + label);

            hour = getArguments().getInt("hour")!=0? getArguments().getInt("hour"): hour;
            minute =  getArguments().getInt("minute")!=0? getArguments().getInt("minute"): minute;

            if (label.equals("start")) {
                Log.d(TAG, "hour " + getArguments().getInt("hour"));
                maxHour = getArguments().getInt("max_hour")!=0? getArguments().getInt("max_hour"): hour;
                maxMinute =  getArguments().getInt("max_minute")!=0? getArguments().getInt("max_minute"): minute;
            }

//            setMax(hour, minute);

        } else {

        }

        timePickerDialog = new RangeTimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
//        timePickerDialog.
        return timePickerDialog;

        // Create a new instance of TimePickerDialog and return it

    }

    private void setMax(int hour, int minute) {
        maxHour = hour;
        maxMinute = minute;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        Log.d(TAG, " onTimeChanged " + hourOfDay);
        boolean validTime = true;
        if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)){
            validTime = false;
        }

        if (hourOfDay  > maxHour || (hourOfDay == maxHour && minute > maxMinute)){
            validTime = false;
        }

        if (validTime) {
            currentHour = hourOfDay;
            currentMinute = minute;
        }

        timePickerDialog.updateTime(currentHour, currentMinute);
        timePickerDialog.updateDialogTitle(view, currentHour, currentMinute);

        if (validTime) {
            scopedBus.post(new TimeSetEvent(hourOfDay, minute, label));
        } else {
            scopedBus.post(new TimeSetEventError());
        }
    }

    private void updateTime(int currentHour, int currentMinute) {
    }
}
