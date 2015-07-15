package com.rorlig.babylog.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.events.datetime.DateSetEvent;
import com.rorlig.babylog.otto.events.datetime.TimeSetEvent;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.datetime.DatePickerFragment;
import com.rorlig.babylog.ui.fragment.datetime.TimePickerFragment;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 5/27/15.
 */
public class DateTimeHeaderFragment extends InjectableFragment {
    public enum DateTimeColor {
        GREEN, BLUE, PURPLE
    }



    @InjectView(R.id.currentDate)
    TextView currentDate;

    @InjectView(R.id.currentTime)
    TextView currentTime;


    @ForActivity
    @Inject
    Context context;

    private String TAG = "DateTimeHeader";




//    public DateTimeHeader(Context context) {
//        super(context);
//
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflater.inflate(R.layout.header_date_time, this, true);
//
//        currentDate = (TextView) v.findViewById(R.id.currentDate);
//
//        currentTime  = (TextView) v.findViewById(R.id.currentTime);
//
//        Time today = new Time(Time.getCurrentTimezone());
//        today.setToNow();
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
//        currentDate.setText(sdf.format(new Date()));
//        sdf = new SimpleDateFormat("hh:mm aa");
//        currentTime.setText(sdf.format(new Date()));
//
////        currentDate.setOnClickListener(new OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                showDatePickerDialog();
////
////            }
////        });
//    }
//
//    public void showDatePickerDialog() {
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getFragmentManager(), "datepicker");
//    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        currentDate.setText(sdf.format(new Date()));
        sdf = new SimpleDateFormat("hh:mm aa");
        currentTime.setText(sdf.format(new Date()));

        TypedArray a  = getParentFragment().getActivity().obtainStyledAttributes(R.styleable.DateTimeHeaderFragment);

        int color = a.getColor(R.styleable.DateTimeHeaderFragment_color_text, getResources().getColor(R.color.text_color_main));
        int my_integer = a.getColor(R.styleable.DateTimeHeaderFragment_my_int, -1);
        Log.d(TAG, "My Integer " + my_integer);
        Log.d(TAG, " color choosen " + color);
        currentDate.setTextColor(color);
        currentTime.setTextColor(color);

        a.recycle();

//        getActivity().ge

//        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.header_date_time, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.currentTime)
    public void onCurrentTimeClick(){
        Log.d(TAG, "current time clicked");
        showTimePickerDialog();
    }

    @OnClick(R.id.currentDate)
    public void onCurrentDateClick(){
        Log.d(TAG, "current date clicked");
        showDatePickerDialog();
    }


    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datepicker");
    }





    private EventListener eventListener = new EventListener();



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




    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.d(TAG, "onDateSet");
        final Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        scopedBus.post(new DateSetEvent(c));
        //; year, month, day));
    }

    public void setColor(DateTimeColor color) {
//        Log.d(TAG, " color " + Integer.toString(color, 16));
        switch (color) {
            case GREEN:
                currentDate.setTextColor(getResources().getColor(R.color.primary_green));
                currentTime.setTextColor(getResources().getColor(R.color.primary_green));
            break;
            case BLUE:
                currentDate.setTextColor(getResources().getColor(R.color.primary_blue));
                currentTime.setTextColor(getResources().getColor(R.color.primary_blue));
                break;

            case PURPLE:
                currentDate.setTextColor(getResources().getColor(R.color.primary_purple));
                currentTime.setTextColor(getResources().getColor(R.color.primary_purple));
                break;


        }

    }


    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void onDateChanged(DateSetEvent dateSetEvent){
            Log.d(TAG, "dateSetEvent " + dateSetEvent);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
//            currentDateLong = dateSetEvent.getCalendar();
            currentDate.setText(sdf.format(dateSetEvent.getCalendar().getTime()));
        }

        @Subscribe
        public void onTimeChanged(TimeSetEvent timeSetEvent){
            Log.d(TAG, "timeSetEvent " + timeSetEvent);
            currentTime.setText(timeSetEvent.getHourOfDay() + ":" + timeSetEvent.getMinute() + " " + (timeSetEvent.getHourOfDay() > 11 ? "PM" : "AM"));
        }
    }

    public Date getEventTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
//            currentDateLong = dateSetEvent.getCalendar();
        try {
            long dayInMillis  = sdf.parse(currentDate.getText().toString()).getTime();
            sdf = new SimpleDateFormat("hh:mm aa");
            long timeInMillis = sdf.parse(currentTime.getText().toString()).getTime();
            TimeZone z = TimeZone.getDefault();
            int offset = z.getRawOffset();
            Log.d(TAG, " rawoffset " + offset);
            if(z.useDaylightTime()){
                Log.d(TAG, "in dst " + z.getDSTSavings());
                offset = offset + z.getDSTSavings();
            }
            Log.d(TAG, "offset " + offset);
            return new Date(dayInMillis+timeInMillis+offset);

//            if (TimeZone.getDefault().inDaylightTime(new Date())){
//                return new Date(dayInMillis + timeInMillis + curr.get(Calendar.ZONE_OFFSET) + curr.get(Calendar.DST_OFFSET)).getDate();
//            } else {
//                return new Date(dayInMillis + timeInMillis + curr.get(Calendar.ZONE_OFFSET)).getDate();
//            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parse attributes during inflation from a view hierarchy into the
     * arguments we handle.
     */
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        Log.v(TAG,"onInflate called " + getParentFragment());

//        TypedArray a = getParentFragment().onobtainStyledAttributes(attrs, R.styleable.DateTimeHeaderFragment);

//        int color = a.getColor(R.styleable.DateTimeHeaderFragment_color_text, getResources().getColor(R.color.text_color_main));
////        Log.d(TAG)
//        currentDate.setTextColor(color);
//        currentTime.setTextColor(color);
//
//        a.recycle();
    }

}
