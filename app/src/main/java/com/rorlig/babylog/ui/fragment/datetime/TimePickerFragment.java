package com.rorlig.babylog.ui.fragment.datetime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.rorlig.babylog.otto.events.TimeSetEvent;
import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;

import java.util.Calendar;

/**
 * Created by rorlig on 7/22/14.
 */
public class TimePickerFragment extends InjectableDialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        scopedBus.post(new TimeSetEvent(hourOfDay, minute));
    }
}