package com.rorlig.babylog.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
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
    private int dateTimeColor;

    public enum DateTimeColor {
        GREEN, BLUE, GRAY, PURPLE
    }



    @InjectView(R.id.currentDate)
    TextView currentDate;

    @InjectView(R.id.currentTime)
    TextView currentTime;


    @ForActivity
    @Inject
    Context context;

    private String TAG = "DateTimeHeader";






    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        currentDate.setText(sdf.format(new Date()));
        sdf = new SimpleDateFormat("hh:mm aa");
        currentTime.setText(sdf.format(new Date()));

        TypedArray a  = getParentFragment().getActivity().obtainStyledAttributes(R.styleable.DateTimeHeaderFragment);

//        int color = a.getColor(R.styleable.DateTimeHeaderFragment_color_text, getResources().getColor(R.color.text_color_main));
//        int my_integer = a.getColor(R.styleable.DateTimeHeaderFragment_my_int, -1);
//        Log.d(TAG, "My Integer " + my_integer);
//        Log.d(TAG, " color choosen " + color);
        a.recycle();

    }


    private Spannable getSpannable(CharSequence charSequence, int color) {
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        };
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        Spannable spannable =  Spannable.Factory.getInstance().newSpannable(charSequence);
        spannable.setSpan(cs, 0 , charSequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(foregroundColorSpan, 0, charSequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
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
        int dateTimeColor = 0;
        switch (color) {
            case GREEN:
                dateTimeColor = getResources().getColor(R.color.primary_green);
            break;
            case BLUE:
                dateTimeColor = getResources().getColor(R.color.primary_blue);
                break;
            case PURPLE:
                dateTimeColor = getResources().getColor(R.color.primary_purple);
                break;
            case GRAY:
                dateTimeColor = getResources().getColor(R.color.primary_gray_dark);
                break;


        }

        this.dateTimeColor = dateTimeColor;


        setSpans(dateTimeColor);


    }

    private void setSpans(int dateTimeColor) {
        currentDate.setText(getSpannable(currentDate.getText().toString(), dateTimeColor));
        currentTime.setText(getSpannable(currentTime.getText().toString(), dateTimeColor));
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
            setSpans(dateTimeColor);
        }

        @Subscribe
        public void onTimeChanged(TimeSetEvent timeSetEvent){
            Log.d(TAG, "timeSetEvent " + timeSetEvent);
            currentTime.setText(timeSetEvent.getHourOfDay()%12 + ":" + timeSetEvent.getMinute() + " " + (timeSetEvent.getHourOfDay() > 11 ? "PM" : "AM"));
            setSpans(dateTimeColor);
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
            if(!z.inDaylightTime(new Date(dayInMillis+timeInMillis))){
                Log.d(TAG, "in dst " + z.getDSTSavings());
                offset = offset - z.getDSTSavings();
            }
            Log.d(TAG, "offset " + offset);
            return new Date(dayInMillis+timeInMillis+offset);


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

    }

    /**
     * set the date time for the header...
     * @param date
     */
    public void setDateTime(Date date) {
        Log.d(TAG, "date " + date);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");
        currentDate.setText(dateFormat.format(date));
        currentTime.setText(timeFormat.format(date));
        setSpans(dateTimeColor);
    }
}
