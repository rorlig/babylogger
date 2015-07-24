package com.rorlig.babylog.ui.fragment.sleep;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.SleepDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.otto.SleepLogCreated;
import com.rorlig.babylog.otto.TimeSetEventError;
import com.rorlig.babylog.otto.events.datetime.TimeSetEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.datetime.CustomTimePickerFragment;
import com.rorlig.babylog.ui.fragment.datetime.TimePickerFragment;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by rorlig on 7/14/14.
 */
public class SleepFragment extends InjectableFragment implements TimePickerDialog.OnTimeSetListener {

    @ForActivity
    @Inject
    Context context;

    private String TAG = "SleepFragment";

    @InjectView(R.id.sleep_start_time)
    RelativeLayout dateRangeStart;

    @InjectView(R.id.sleep_duration)
    RelativeLayout sleepDurationLayout;


    TextView dateStartHourTextView;


    TextView dateStartMinuteTextView;

    TextView dateStartSecondTextView;


    TextView durationHourTextView;


    TextView durationMinuteTextView;

    TextView dateEndSecondTextView;

    private EventListener eventListener = new EventListener();
    private String START_HOUR = "start_hour";
    private String START_MINUTE = "start_minute";

    private String END_HOUR = "end_hour";

    private String END_MINUTE = "end_minute";

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        scopedBus.post(new FragmentCreated("Sleep"));

        dateStartHourTextView = (TextView) dateRangeStart.findViewById(R.id.hour);
        dateStartMinuteTextView = (TextView) dateRangeStart.findViewById(R.id.minute);

        durationHourTextView = (TextView) sleepDurationLayout.findViewById(R.id.hour);
        durationMinuteTextView = (TextView) sleepDurationLayout.findViewById(R.id.minute);

        if (paramBundle!=null) {
            dateStartHourTextView.setText(paramBundle.getString(START_HOUR));
            dateStartMinuteTextView.setText(paramBundle.getString(START_MINUTE));
            durationHourTextView.setText(paramBundle.getString(END_HOUR));
            durationMinuteTextView.setText(paramBundle.getString(END_MINUTE));
        } else {
            init();
        }


    }



    @OnClick(R.id.sleep_start_time)
    public void onDateRangeStart(){
        Log.d(TAG, "onDateRangeStart");
        showTimePickerDialog("start");
    }

    @OnClick(R.id.sleep_duration)
    public void onDurationClicked(){
        Log.d(TAG, "onDurationClicked");
        showDurationDialog();
        //show custom dialog...
//        showTimePickerDialog("end");
    }

    @OnClick(R.id.btn_save)
    public void onSaveBtnClicked() {
        Log.d(TAG, "saveBtn clicked");
        Log.d(TAG, "save btn clicked");
        Dao<SleepDao, Integer> sleepDao;
        SleepDao daoObject;
        Calendar c = Calendar.getInstance();


        try {
            sleepDao = babyLoggerORMLiteHelper.getSleepDao();
            daoObject = new SleepDao(getStartTime(),getDuration(), c.getTime());
            sleepDao.create(daoObject);
            Log.d(TAG, "created objected " + daoObject);
            scopedBus.post(new SleepLogCreated());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //show the duration dialog...
    private void showDurationDialog() {
        DialogFragment newFragment = new CustomTimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
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
        outState.putString(END_HOUR, durationHourTextView.getText().toString());
        outState.putString(END_MINUTE, durationMinuteTextView.getText().toString());

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

        durationHourTextView.setText(String.format("%02d", 0));
//        durationMinuteTextView.setText("hello");
        durationMinuteTextView.setText(String.format("%02d", 0));




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
            args.putInt("max_hour", Integer.parseInt(String.valueOf(durationHourTextView.getText())));
            args.putInt("max_minute", Integer.parseInt(String.valueOf(durationMinuteTextView.getText())));
        } else {
            args.putInt("hour", Integer.parseInt(String.valueOf(durationHourTextView.getText())));
            args.putInt("minute", Integer.parseInt(String.valueOf(durationMinuteTextView.getText())));
        }
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "datepicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }


    private Date getStartTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, Integer.parseInt(dateStartHourTextView.getText().toString()));
        c.set(Calendar.MINUTE, Integer.parseInt(dateStartMinuteTextView.getText().toString()) - 1);
        return c.getTime();


    }

    private Long getDuration() {
        int hour = Integer.parseInt(durationHourTextView.getText().toString());
        int minute = Integer.parseInt(durationMinuteTextView.getText().toString());
        return Long.valueOf(hour * 60 + minute);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
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
                Log.d(TAG, "durartion set hour " + timeSetEvent.getHourOfDay() + " minute " + timeSetEvent.getMinute());
                durationHourTextView.setText(String.format("%02d", timeSetEvent.getHourOfDay()));
                durationMinuteTextView.setText(String.format("%02d", timeSetEvent.getMinute()));
            }

        }

        @Subscribe
        public void onTimeChangeError(TimeSetEventError event) {
            Toast.makeText(getActivity(), "Start time cannot be greater than end time", Toast.LENGTH_SHORT).show();
        }
    }



}
