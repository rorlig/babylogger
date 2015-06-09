package com.rorlig.babylog.ui.fragment.diaper;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.model.diaper.DiaperChangeColorType;
import com.rorlig.babylog.model.diaper.DiaperChangeEnum;
import com.rorlig.babylog.model.diaper.DiaperChangeTextureType;
import com.rorlig.babylog.model.diaper.DiaperIncident;
import com.rorlig.babylog.otto.DiaperLogCreatedEvent;
import com.rorlig.babylog.otto.events.DateSetEvent;
import com.rorlig.babylog.otto.events.TimeSetEvent;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.rorlig.babylog.ui.fragment.datetime.DatePickerFragment;
import com.rorlig.babylog.ui.fragment.datetime.TimePickerFragment;
import com.rorlig.babylog.ui.widget.DateTimeHeaderFragment;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/14/14.
 * fragment class to track of diaper events....
 */
public class DiaperChangeActivity2 extends InjectableActivity {

    @ForActivity
    @Inject
    Context context;

//    @InjectView(R.id.currentDate)
//    TextView currentDate;
//
//    @InjectView(R.id.currentTime)
//    TextView currentTime;

    @InjectView(R.id.saveBtn)
    Button saveBtn;

    @InjectView(R.id.notes)
    EditText notes;

    @InjectView(R.id.diaper_incident_type)
    RadioGroup diaperIncidentType;

    @InjectView(R.id.checkDiaperLeak)
    RadioButton checkDiaperLeak;

    @InjectView(R.id.checkNoDiaper)
    RadioButton checkNoDiaper;

    @InjectView(R.id.poopTypeLayout)
    RelativeLayout poopTypeLayout;

    @InjectView(R.id.poopColorLayout)
    RelativeLayout poopColorLayout;

    @InjectView(R.id.poopDensity)
    SeekBar poopDensitySeekBar;

    @InjectView(R.id.poopTypeLabel)
    TextView poopTypeLabel;


    @InjectView(R.id.diaper_change_type)
    RadioGroup diaperChangeType;

    @InjectView(R.id.poopColorRadioGroup)
    RadioGroup diaperChangeColor;

    DateTimeHeaderFragment dateTimeHeader;

    private String[] poopDensityLabels = new String[]{"Loose", "Chunky", "Hard","Very Hard"};


//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;


    Typeface typeface;

    private String TAG = "DiaperChangeActivity2";

    private EventListener eventListener = new EventListener();

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;
    private Calendar currentDateLong;




//    @Override
//    public void onActivityCreated(Bundle paramBundle) {
//        super.onActivityCreated(paramBundle);
//
//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");
//
//        ActionBar actionBar = getActivity().getActionBar();
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
//
//        poopDensitySeekBar.setMax(99);
//        poopDensitySeekBar.setProgress(0); // Set it to zero so it will start at the left-most edge
//        poopDensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.d(TAG, "progress" + progress);
//                int value = progress / 25;
//                poopTypeLabel.setText(poopDensityLabels[value]);
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//
//        });
//
//
//        diaperChangeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.diaper_wet) {
//                    //hide the texture...
//                    poopTypeLayout.setVisibility(View.GONE);
//                    poopColorLayout.setVisibility(View.GONE);
//                } else {
//                    //show the texture....
//                    poopTypeLayout.setVisibility(View.VISIBLE);
//                    poopColorLayout.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//
//         dateTimeHeader = (DateTimeHeaderFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.header);
//
//
//
//
//
////        getActivity().getActionBar().setDisplayHomeAsUpEnabled(fa);
//
////        scopedBus.post(new UpNavigationEvent);
//    }

//     @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Time today = new Time(Time.getCurrentTimezone());
//        today.setToNow();
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
////        currentDate.setText(sdf.format(new Date()));
////        sdf = new SimpleDateFormat("hh:mm aa");
////        currentTime.setText(sdf.format(new Date()));
//
////        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
//    }

    /*
    * Register to events...
    */
    @Override
    public void onResume(){


        super.onResume();
        Log.d(TAG, "onResume");
        scopedBus.register(eventListener);
    }

    /*
     * Unregister from events ...
     */
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
        scopedBus.unregister(eventListener);

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

    @OnClick(R.id.saveBtn)
    public void onSaveBtnClicked(){
        Log.d(TAG, "save btn clicked");
        Dao<DiaperChangeDao, Integer> diaperChangeDao;
        DiaperChangeDao daoObject;
        DiaperChangeTextureType diaperChangeTexture;
        DiaperIncident diaperIncident = getDiaperIncident();
        DiaperChangeColorType diaperChangeColorType = getDiaperColor();
        Long time = dateTimeHeader.getEventTime();
        try {
            diaperChangeDao = babyLoggerORMLiteHelper.getDiaperChangeDao();
            switch (diaperChangeType.getCheckedRadioButtonId()) {
                case R.id.diaper_wet:
                    daoObject = new DiaperChangeDao(DiaperChangeEnum.WET, null, null, diaperIncident, notes.getText().toString(), time );
                    break;
                case R.id.diaper_both:

                    diaperChangeTexture = getDiaperChangeTexture();
                    daoObject = new DiaperChangeDao(DiaperChangeEnum.BOTH, diaperChangeTexture, diaperChangeColorType,
                                                    diaperIncident, notes.getText().toString(), time );
                    break;
               default:
                    diaperChangeTexture = getDiaperChangeTexture();
                    daoObject = new DiaperChangeDao(DiaperChangeEnum.POOP, diaperChangeTexture, diaperChangeColorType,
                                                    diaperIncident, notes.getText().toString(), time );

                    break;
            }
            diaperChangeDao.create(daoObject);
            Log.d(TAG, "created objected " + daoObject);
            scopedBus.post(new DiaperLogCreatedEvent());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private DiaperChangeColorType getDiaperColor() {
        switch (diaperChangeColor.getCheckedRadioButtonId()) {
            case R.id.poopcolor1:
                return DiaperChangeColorType.COLOR_1;
            case R.id.poopcolor2:
                return DiaperChangeColorType.COLOR_2;
            case R.id.poopcolor3:
                return DiaperChangeColorType.COLOR_3;

            case R.id.poopcolor4:
                return DiaperChangeColorType.COLOR_4;

            case R.id.poopcolor5:
                return DiaperChangeColorType.COLOR_5;

            case R.id.poopcolor6:
                return DiaperChangeColorType.COLOR_6;

            case R.id.poopcolor7:
                return DiaperChangeColorType.COLOR_7;

            case R.id.poopcolor8:
                return DiaperChangeColorType.COLOR_8;

        }
        return  DiaperChangeColorType.NOT_SPECIFIED;
    }



    private DiaperIncident getDiaperIncident() {
        switch (diaperIncidentType.getCheckedRadioButtonId()) {
            case R.id.checkDiaperLeak:
                return DiaperIncident.DIAPER_LEAK;

            case R.id.checkNoDiaper:
                return DiaperIncident.NO_DIAPER;

            default:
                return DiaperIncident.NONE;
        }
    }

    private DiaperChangeTextureType getDiaperChangeTexture() {
        if (poopDensitySeekBar.getProgress()<25) return DiaperChangeTextureType.LOOSE;
        else if (poopDensitySeekBar.getProgress()<50) return DiaperChangeTextureType.SEEDY;
        else if (poopDensitySeekBar.getProgress()<75) return DiaperChangeTextureType.CHUNKY;
        else return DiaperChangeTextureType.HARD;

    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datepicker");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_diaper_change);
        poopDensitySeekBar.setMax(99);
        poopDensitySeekBar.setProgress(0); // Set it to zero so it will start at the left-most edge
        poopDensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "progress" + progress);
                int value = progress / 25;
                poopTypeLabel.setText(poopDensityLabels[value]);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });


        diaperChangeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.diaper_wet) {
                    //hide the texture...
                    poopTypeLayout.setVisibility(View.GONE);
                    poopColorLayout.setVisibility(View.GONE);
                } else {
                    //show the texture....
                    poopTypeLayout.setVisibility(View.VISIBLE);
                    poopColorLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        dateTimeHeader = (DateTimeHeaderFragment) getSupportFragmentManager().findFragmentById(R.id.header);

//        setHasOptionsMenu(true);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main, menu);
//    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view =  inflater.inflate(R.layout.fragment_diaper_change, null);
//        ButterKnife.inject(this, view);
//        return view;
//    }


    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void onDateChanged(DateSetEvent dateSetEvent){
            Log.d(TAG, "dateSetEvent " + dateSetEvent);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
//            currentDateLong = dateSetEvent.getCalendar();
//            currentDate.setText(sdf.format(dateSetEvent.getCalendar().getTime()));
        }

        @Subscribe
        public void onTimeChanged(TimeSetEvent timeSetEvent){
            Log.d(TAG, "timeSetEvent " + timeSetEvent);
//            currentTime.setText(timeSetEvent.getHourOfDay() + ":" + timeSetEvent.getMinute() + " "  + (timeSetEvent.getHourOfDay()>11?"PM":"AM"));
        }
    }
}
