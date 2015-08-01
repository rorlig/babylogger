package com.rorlig.babylog.ui.fragment.diaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.j256.ormlite.dao.Dao;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.model.diaper.DiaperChangeColorType;
import com.rorlig.babylog.model.diaper.DiaperChangeEnum;
import com.rorlig.babylog.model.diaper.DiaperChangeTextureType;
import com.rorlig.babylog.model.diaper.DiaperIncident;
import com.rorlig.babylog.otto.events.datetime.DateSetEvent;
import com.rorlig.babylog.otto.events.datetime.TimeSetEvent;
import com.rorlig.babylog.otto.events.diaper.DiaperLogCreatedEvent;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.datetime.DatePickerFragment;
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

import static com.rorlig.babylog.model.diaper.DiaperChangeColorType.*;

/**
 * Created by rorlig on 7/14/14.
 * fragment class to track of diaper events....
 */
/**
 * Created by rorlig on 7/14/14.
 * fragment class to track of diaper events....
 */
public class DiaperChangeFragment extends InjectableFragment {

    @ForActivity
    @Inject
    Context context;

//    @InjectView(R.id.currentDate)
//    TextView currentDate;
//
//    @InjectView(R.id.currentTime)
//    TextView currentTime;

    @InjectView(R.id.save_btn)
    Button saveBtn;

    @InjectView(R.id.two_button_layout)
    LinearLayout editDeleteBtn;

    @InjectView(R.id.notes)
    EditText notes;

    @InjectView(R.id.diaper_incident_type)
    RadioGroup diaperIncidentType;

    @InjectView(R.id.check_diaper_leak)
    RadioButton checkDiaperLeak;

    @InjectView(R.id.check_no_diaper)
    RadioButton checkNoDiaper;

    @InjectView(R.id.poop_type_layout)
    RelativeLayout poopTypeLayout;

    @InjectView(R.id.poop_color_layout)
    RelativeLayout poopColorLayout;

    @InjectView(R.id.poop_density)
    SeekBar poopDensitySeekBar;



    @InjectView(R.id.poop_type_label)
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


//    Typeface typeface;

    private String TAG = "DiaperChangeFragment";

    private EventListener eventListener = new EventListener();

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;
    private Calendar currentDateLong;
    private int id=-1;

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);



//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

//        ActionBar actionBar = getActivity().getActionBar();
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

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


        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.PURPLE);




        if (getArguments()!=null) {
            Log.d(TAG, "arguments are not null");
            id = getArguments().getInt("diaper_change_id");
            initViews(id);
        }





//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(fa);

//        scopedBus.post(new UpNavigationEvent);
    }

    private void initViews(int id) {
        Log.d(TAG, "initViews "   + id);
        try {
            DiaperChangeDao diaperChangeDao = babyLoggerORMLiteHelper.getDiaperChangeDao().queryForId(id);
            Log.d(TAG, diaperChangeDao.toString());

            setDiaperChangeType(diaperChangeDao);

            if (diaperChangeDao.getDiaperChangeEventType()!=DiaperChangeEnum.WET) {
                poopTypeLayout.setVisibility(View.VISIBLE);
                poopColorLayout.setVisibility(View.VISIBLE);
                setDiaperChangePoopColor(diaperChangeDao);
                setDiaperChangePoopType(diaperChangeDao);
                setPoopTexture(diaperChangeDao);
            }

            setDiaperIncidentType(diaperChangeDao);
            notes.setText(diaperChangeDao.getDiaperChangeNotes());
            setDateTimeHeader(diaperChangeDao);

//            saveBtn.setText("Edit");

            editDeleteBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setDateTimeHeader(DiaperChangeDao diaperChangeDao) {
        Log.d(TAG, "setDateTimeHeader");
        dateTimeHeader.setDateTime(diaperChangeDao.getDate());
    }

    private void setDiaperChangePoopType(DiaperChangeDao diaperChangeDao) {
        switch (diaperChangeDao.getPoopTexture()) {

        }
    }

    private void setDiaperChangePoopColor(DiaperChangeDao diaperChangeDao) {


        switch (diaperChangeDao.getPoopColor()) {
            case COLOR_1:
                diaperChangeColor.check(R.id.poopcolor1);
                break;
            case COLOR_2:
                diaperChangeColor.check(R.id.poopcolor2);
                break;
            case COLOR_3:
                diaperChangeColor.check(R.id.poopcolor3);
                break;
            case COLOR_4:
                diaperChangeColor.check(R.id.poopcolor4);
                break;
            case COLOR_5:
                diaperChangeColor.check(R.id.poopcolor5);
                break;
            case COLOR_6:
                diaperChangeColor.check(R.id.poopcolor6);
                break;
            case COLOR_7:
                diaperChangeColor.check(R.id.poopcolor7);
                break;
            case COLOR_8:
                diaperChangeColor.check(R.id.poopcolor8);
                break;
            default:
                diaperChangeColor.clearCheck();




        }
    }

    private void setDiaperChangeType(DiaperChangeDao diaperChangeDao) {
        switch (diaperChangeDao.getDiaperChangeEventType()) {
            case POOP:
                diaperChangeType.check(R.id.diaper_pop);
                break;
            case WET:
                diaperChangeType.check(R.id.diaper_wet);
                break;
            case BOTH:
                diaperChangeType.check(R.id.diaper_both);
                break;
        }
    }


    private void setDiaperIncidentType(DiaperChangeDao diaperChangeDao) {
        switch (diaperChangeDao.getDiaperChangeIncidentType()) {
            case NONE:
                diaperIncidentType.check(R.id.check_no_incident);
                break;
            case NO_DIAPER:
                diaperIncidentType.check(R.id.check_no_diaper);
                break;
            case DIAPER_LEAK:
                diaperIncidentType.check(R.id.check_diaper_leak);
        }
    }

    private void setPoopTexture(DiaperChangeDao diaperChangeDao) {
        switch (diaperChangeDao.getPoopTexture()) {
            case LOOSE:
                poopDensitySeekBar.setProgress(0);
                break;
            case SEEDY:
                poopDensitySeekBar.setProgress(25);
                break;
            case CHUNKY:
                poopDensitySeekBar.setProgress(50);
                break;
            case HARD:
                poopDensitySeekBar.setProgress(75);
                break;
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    @OnClick(R.id.save_btn)
    public void onSaveBtnClicked(){
        Log.d(TAG, "save btn clicked");
        createOrEdit();


    }

    @OnClick(R.id.edit_btn)
      public void onEditBtnClicked(){
        Log.d(TAG, "edit btn clicked");
        createOrEdit();
    }

    @OnClick(R.id.delete_btn)
    public void onDeleteBtnClicked(){
        Log.d(TAG, "delete btn clicked");
        DiaperChangeDao daoObject;
        try {

            daoObject = createDiaperChangeDao();
            Dao<DiaperChangeDao, Integer> diaperChangeDao = babyLoggerORMLiteHelper.getDiaperChangeDao();

            if (id!=-1) {
                Log.d(TAG, "updating it");
                daoObject.setId(id);
                diaperChangeDao.delete(daoObject);
            }
            scopedBus.post(new DiaperLogCreatedEvent());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createOrEdit() {
        Dao<DiaperChangeDao, Integer> diaperChangeDao;
        DiaperChangeDao daoObject;
        try {

            daoObject = createDiaperChangeDao();
            diaperChangeDao = babyLoggerORMLiteHelper.getDiaperChangeDao();

            if (id!=-1) {
                Log.d(TAG, "updating it");
                daoObject.setId(id);
                diaperChangeDao.update(daoObject);
            } else {
                Log.d(TAG, "creating it");

                diaperChangeDao.create(daoObject);
            }

            Log.d(TAG, "created objected " + daoObject);
            scopedBus.post(new DiaperLogCreatedEvent());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private DiaperChangeDao createDiaperChangeDao() {
        DiaperChangeDao daoObject = null;
        switch (diaperChangeType.getCheckedRadioButtonId()) {
            case R.id.diaper_wet:
                 daoObject = new DiaperChangeDao(DiaperChangeEnum.WET, null, null, getDiaperIncident(), notes.getText().toString(), dateTimeHeader.getEventTime());
                break;
            case R.id.diaper_both:

                daoObject = new DiaperChangeDao(DiaperChangeEnum.BOTH, getDiaperChangeTexture(), getDiaperColor(),
                        getDiaperIncident(), notes.getText().toString(),dateTimeHeader.getEventTime() );
                break;
            default:
                daoObject = new DiaperChangeDao(DiaperChangeEnum.POOP, getDiaperChangeTexture(), getDiaperColor(),
                        getDiaperIncident(), notes.getText().toString(),dateTimeHeader.getEventTime() );
                break;
        }
        return daoObject;
    }

    private DiaperChangeColorType getDiaperColor() {
        switch (diaperChangeColor.getCheckedRadioButtonId()) {
            case R.id.poopcolor1:
                return COLOR_1;
            case R.id.poopcolor2:
                return COLOR_2;
            case R.id.poopcolor3:
                return COLOR_3;

            case R.id.poopcolor4:
                return COLOR_4;

            case R.id.poopcolor5:
                return COLOR_5;

            case R.id.poopcolor6:
                return COLOR_6;

            case R.id.poopcolor7:
                return COLOR_7;

            case R.id.poopcolor8:
                return COLOR_8;

        }
        return  NOT_SPECIFIED;
    }



    private DiaperIncident getDiaperIncident() {
        switch (diaperIncidentType.getCheckedRadioButtonId()) {
            case R.id.check_diaper_leak:
                return DiaperIncident.DIAPER_LEAK;

            case R.id.check_no_diaper:
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
        newFragment.show(getFragmentManager(), "timePicker");
    }


    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datepicker");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diaper_change, null);
        ButterKnife.inject(this, view);
        return view;
    }


    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void onDateChanged(DateSetEvent dateSetEvent){
            Log.d(TAG, "dateSetEvent " + dateSetEvent);
//            currentDateLong = dateSetEvent.getCalendar();
//            currentDate.setText(sdf.format(dateSetEvent.getCalendar().getDate()));
        }

        @Subscribe
        public void onTimeChanged(TimeSetEvent timeSetEvent){
            Log.d(TAG, "timeSetEvent " + timeSetEvent);
//            currentTime.setText(timeSetEvent.getHourOfDay() + ":" + timeSetEvent.getMinute() + " "  + (timeSetEvent.getHourOfDay()>11?"PM":"AM"));
        }
    }
}
