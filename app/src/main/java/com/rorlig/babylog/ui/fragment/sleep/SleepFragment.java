package com.rorlig.babylog.ui.fragment.sleep;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
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

import com.gc.materialdesign.views.Button;
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
import com.rorlig.babylog.ui.widget.DateTimeHeaderFragment;
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



//    @InjectView(R.id.sleep_start_time)
//    RelativeLayout dateRangeStart;

    @InjectView(R.id.sleep_hours)
    TextView sleepHours;

    @InjectView(R.id.sleep_minutes)
    TextView sleepMinutes;

    @InjectView(R.id.save_btn)
    Button saveBtn;



    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;

    private DateTimeHeaderFragment dateTimeHeader;
    private boolean minuteEmpty = true;
    private boolean hourEmpty = true;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        scopedBus.post(new FragmentCreated("Sleep"));


        if (paramBundle!=null) {
//            dateStartHourTextView.setText(paramBundle.getString(START_HOUR));
//            dateStartMinuteTextView.setText(paramBundle.getString(START_MINUTE));
//            durationHourTextView.setText(paramBundle.getString(END_HOUR));
//            durationMinuteTextView.setText(paramBundle.getString(END_MINUTE));
        } else {
            init();
        }

        saveBtn.setEnabled(false);

        setUpTextWatchers();

//        setSpans();

        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.GRAY);
    }


//    private Spannable getSpannable(CharSequence charSequence) {
//        ClickableSpan cs = new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//
//            }
//        };
//        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getActivity().getResources().getColor(R.color.primary_gray));
//       Spannable spannable =  Spannable.Factory.getInstance().newSpannable(charSequence);
//       spannable.setSpan(cs, 0 , charSequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//       spannable.setSpan(foregroundColorSpan, 0 , charSequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        return spannable;
//    }


//    @OnClick(R.id.sleep_start_time)
//    public void onDateRangeStart(){
//        Log.d(TAG, "onDateRangeStart");
//        showTimePickerDialog("start");
//    }
//
//    @OnClick(R.id.sleep_duration)
//    public void onDurationClicked(){
//        Log.d(TAG, "onDurationClicked");
//        showDurationDialog();
//        //show custom dialog...
////        showTimePickerDialog("end");
//    }

    @OnClick(R.id.save_btn)
    public void onSaveBtnClicked() {
        Log.d(TAG, "saveBtn clicked");
        Log.d(TAG, "save btn clicked");
        Dao<SleepDao, Integer> sleepDao;
        SleepDao daoObject = null;
        Calendar c = Calendar.getInstance();


        try {
            sleepDao = babyLoggerORMLiteHelper.getSleepDao();
            daoObject = new SleepDao(c.getTime(), getDuration(), c.getTime());
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
        View view =  inflater.inflate(R.layout.fragment_sleep_new, null);
        ButterKnife.inject(this, view);
        return view;
    }

//    /*
//     * Register to events...
//     */
//    @Override
//    public void onStart(){
//
//
//        super.onStart();
//        Log.d(TAG, "onStart");
//        scopedBus.register(eventListener);
//    }
//
//    /*
//     * Unregister from events ...
//     */
//    @Override
//    public void onStop(){
//        super.onStop();
//        Log.d(TAG, "onStop");
//        scopedBus.unregister(eventListener);
//
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString(START_HOUR, dateStartHourTextView.getText().toString());
//        outState.putString(START_MINUTE, dateStartMinuteTextView.getText().toString());
//        outState.putString(END_HOUR, durationHourTextView.getText().toString());
//        outState.putString(END_MINUTE, durationMinuteTextView.getText().toString());

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


//        dateStartHourTextView.setText(String.format("%02d", hour));
//        dateStartMinuteTextView.setText(String.format("%02d", minute));
//
//        durationHourTextView.setText(String.format("%02d", 0));
////        durationMinuteTextView.setText("hello");
//        durationMinuteTextView.setText(String.format("%02d", 0));




    }


//    private void showTimePickerDialog(String label) {
//        Log.d(TAG, " label " + label);
//        DialogFragment newFragment = new TimePickerFragment();
//
//        Bundle args = new Bundle();
//
//        //add label to indicate which date is being set...
//        args.putString("label", label);
//
//        //if the dialog is for the start date make sure the max date < end_date
//        if (label.equals("start")) {
//            args.putInt("hour", Integer.parseInt(String.valueOf(dateStartHourTextView.getText())));
//            args.putInt("minute", Integer.parseInt(String.valueOf(dateStartMinuteTextView.getText())));
//            args.putInt("max_hour", Integer.parseInt(String.valueOf(durationHourTextView.getText())));
//            args.putInt("max_minute", Integer.parseInt(String.valueOf(durationMinuteTextView.getText())));
//        } else {
//            args.putInt("hour", Integer.parseInt(String.valueOf(durationHourTextView.getText())));
//            args.putInt("minute", Integer.parseInt(String.valueOf(durationMinuteTextView.getText())));
//        }
//        newFragment.setArguments(args);
//        newFragment.show(getFragmentManager(), "datepicker");
//    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }


//    private Date getStartTime() {
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sleepHours.getText().toString()));
//        c.set(Calendar.MINUTE, Integer.parseInt(sleepMinutes.getText().toString()));
//        return c.getTime();
//
//
//    }
//
    private Long getDuration() {
        int hour = Integer.parseInt(sleepHours.getText().toString());
        int minute = Integer.parseInt(sleepMinutes.getText().toString());
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



    private void setUpTextWatchers() {

        sleepMinutes.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = sleepMinutes.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = sleepMinutes.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);
//
//                if ((str.length() == 2 && len < str.length())) {
//
//                    Log.d(TAG, "appending .");
//                    //checking length  for backspace.
//                    sleepMinutes.append(".");
//                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
//                }

                if (str.length() > 0) {
//                    saveBtn.setEnabled(true);
                    minuteEmpty = false;
//                    setSaveEnabled();

                } else {
//                    saveBtn.setEnabled(false);
                    minuteEmpty = true;

                }
                setSaveEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });

        sleepHours.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = sleepHours.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = sleepHours.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);


                if (str.length()>0) {
                    hourEmpty = false;

                } else {
//                    saveBtn.setEnabled(false);
                    hourEmpty = true;

                }

                setSaveEnabled();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });




    }

    private void setSaveEnabled() {
            if (!minuteEmpty || !hourEmpty) {
                saveBtn.setEnabled(true);
            } else {
                saveBtn.setEnabled(false);
            }
    }


}
