package com.rorlig.babylog.ui.fragment.sleep;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.TimeSetEventError;
import com.rorlig.babylog.otto.events.datetime.DateSetEvent;
import com.rorlig.babylog.otto.events.datetime.TimeSetEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.datetime.TimePickerFragment;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/14/14.
 */
public class SleepFragment extends InjectableFragment {

    @ForActivity
    @Inject
    Context context;

    private String TAG = "SleepFragment";

    @InjectView(R.id.sleep_start_time)
    RelativeLayout dateRangeStart;

    @InjectView(R.id.sleep_end_time)
    RelativeLayout dateRangeEnd;


    TextView dateStartHourTextView;


    TextView dateStartMinuteTextView;

    TextView dateStartSecondTextView;


    TextView dateEndHourTextView;


    TextView dateEndMinuteTextView;

    TextView dateEndSecondTextView;

    private EventListener eventListener = new EventListener();
    private String START_HOUR = "start_hour";
    private String START_MINUTE = "start_minute";

    private String END_HOUR = "end_hour";

    private String END_MINUTE = "end_minute";


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        scopedBus.post(new FragmentCreated("Sleep"));

        dateStartHourTextView = (TextView) dateRangeStart.findViewById(R.id.hour);
        dateStartMinuteTextView = (TextView) dateRangeStart.findViewById(R.id.minute);

        dateEndHourTextView = (TextView) dateRangeEnd.findViewById(R.id.hour);
        dateEndMinuteTextView = (TextView) dateRangeEnd.findViewById(R.id.minute);

        if (paramBundle!=null) {
            dateStartHourTextView.setText(paramBundle.getString(START_HOUR));
            dateStartMinuteTextView.setText(paramBundle.getString(START_MINUTE));
            dateEndHourTextView.setText(paramBundle.getString(END_HOUR));
            dateEndMinuteTextView.setText(paramBundle.getString(END_MINUTE));
        } else {
            init();
        }


    }



    @OnClick(R.id.sleep_start_time)
    public void onDateRangeStart(){
        Log.d(TAG, "onDateRangeStart");
        showTimePickerDialog("start");
    }

    @OnClick(R.id.sleep_end_time)
    public void onDateRangeEnd(){
        Log.d(TAG, "onDateRangeEnd");
        showTimePickerDialog("end");
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sleep, null);
        ButterKnife.inject(this, view);
        return view;
    }

    /*
     * Register to events...
     */
    @Override
    public void onStart(){


        super.onStart();
        Log.d(TAG, "onStart");
        scopedBus.register(eventListener);
    }

    /*
     * Unregister from events ...
     */
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
        scopedBus.unregister(eventListener);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(START_HOUR, dateStartHourTextView.getText().toString());
        outState.putString(START_MINUTE, dateStartMinuteTextView.getText().toString());
        outState.putString(END_HOUR, dateEndHourTextView.getText().toString());
        outState.putString(END_MINUTE, dateEndMinuteTextView.getText().toString());

    }

    /*
     * Initializes the hour and minute sleep times...
     * @param null
     * @return null
     */
    private void init(){

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        dateStartHourTextView.setText(String.format("%02d", hour));
        dateStartMinuteTextView.setText(String.format("%02d", minute));

        dateEndHourTextView.setText(String.format("%02d", hour));
        dateEndMinuteTextView.setText(String.format("%02d", minute));
        
        
        

    }


    private void showTimePickerDialog(String label) {
        Log.d(TAG, " label " + label);
        DialogFragment newFragment = new TimePickerFragment();

        Bundle args = new Bundle();

        //add label to indicate which date is being set...
        args.putString("label", label);

        //if the dialog is for the start date make sure the max date < end_date
        if (label.equals("start")) {
            args.putInt("hour", Integer.parseInt(String.valueOf(dateStartHourTextView.getText())));
            args.putInt("minute", Integer.parseInt(String.valueOf(dateStartMinuteTextView.getText())));
            args.putInt("max_hour", Integer.parseInt(String.valueOf(dateEndHourTextView.getText())));
            args.putInt("max_minute", Integer.parseInt(String.valueOf(dateEndMinuteTextView.getText())));
        } else {
            args.putInt("hour", Integer.parseInt(String.valueOf(dateEndHourTextView.getText())));
            args.putInt("minute", Integer.parseInt(String.valueOf(dateEndMinuteTextView.getText()))); 
        }
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "datepicker");
    }

    private class EventListener {
        public EventListener() {

        }



        @Subscribe
        public void onTimeChanged(TimeSetEvent timeSetEvent){
            Log.d(TAG, "timeSetEvent " + timeSetEvent.toString());
            if (timeSetEvent.getLabel().equals("start")) {
                dateStartHourTextView.setText(String.format("%02d", timeSetEvent.getHourOfDay()));
                dateStartMinuteTextView.setText(String.format("%02d", timeSetEvent.getMinute()));
            } else {
                dateEndHourTextView.setText(String.format("%02d", timeSetEvent.getHourOfDay()));
                dateEndMinuteTextView.setText(String.format("%02d", timeSetEvent.getHourOfDay()));
            }

        }

        @Subscribe
        public void onTimeChangeError(TimeSetEventError event) {
            Toast.makeText(getActivity(), "Start time cannot be greater than end time", Toast.LENGTH_SHORT).show();
        }
    }
}
