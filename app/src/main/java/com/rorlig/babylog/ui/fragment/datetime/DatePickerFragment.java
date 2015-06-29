package com.rorlig.babylog.ui.fragment.datetime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;

import com.rorlig.babylog.otto.events.datetime.DateSetEvent;
import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rorlig on 7/22/14.
 */
public class DatePickerFragment extends InjectableDialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private String TAG = "DatePickerFragment";
    private String label;
    private long startMaxDate;
    private long currentDate;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        if (getArguments()!=null) {
            startMaxDate = getArguments().getLong("max_start_date");
            currentDate = getArguments().getLong("current_date");
            c.setTimeInMillis(currentDate);
            label = getArguments().getString("label", "");

            if (label.equals("end")) {
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            } else if (label.equals("start")) {
                datePickerDialog.getDatePicker().setMaxDate(startMaxDate);
            }

        }







        return  datePickerDialog;
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.d(TAG, "onDateSet");
        final Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        scopedBus.post(new DateSetEvent(c, label));
        //; year, month, day));
    }
}
