package com.rorlig.babylog.ui.fragment.datetime;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.rorlig.babylog.otto.TimeSetEventError;
import com.rorlig.babylog.otto.events.datetime.TimeSetEvent;
import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;
import com.rorlig.babylog.ui.widget.CustomTimePickerDialog;
import com.rorlig.babylog.ui.widget.RangeTimePickerDialog;

import java.util.Calendar;

/**
 * Created by rorlig on 7/22/14.
 */
public class CustomTimePickerFragment extends InjectableDialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private String TAG = "TimePickerFragment";
    private String label;
    private int minHour = 0;
    private int minMinute = 0;
    private int maxHour = 23;
    private int maxMinute=59;
    private int currentHour = 0;
    private int currentMinute =0;
    private CustomTimePickerDialog timePickerDialog;


    @NonNull
    @Override
    public CustomTimePickerDialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        currentHour = hour;
        currentMinute = minute;



        timePickerDialog = new CustomTimePickerDialog(getActivity(), this, 0, 0, true);
//        timePickerDialog.
        return timePickerDialog;

        // Create a new instance of TimePickerDialog and return it

    }



    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        Log.d(TAG, " onTimeChanged hour " + hourOfDay  + " minute " + minute);
        scopedBus.post(new TimeSetEvent(hourOfDay, minute));
//        boolean validTime = true;
//        if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)){
//            validTime = false;
//        }
//
//        if (hourOfDay  > maxHour || (hourOfDay == maxHour && minute > maxMinute)){
//            validTime = false;
//        }
//
//        if (validTime) {
//            currentHour = hourOfDay;
//            currentMinute = minute;
//        }
//
//        timePickerDialog.updateTime(currentHour, currentMinute);
//        timePickerDialog.updateDialogTitle(view, currentHour, currentMinute);
//
//        if (validTime) {
//            scopedBus.post(new TimeSetEvent(hourOfDay, minute, label));
//        } else {
//            scopedBus.post(new TimeSetEventError());
//        }
    }

    private void updateTime(int currentHour, int currentMinute) {
    }
}
