package com.rorlig.babylog.ui.fragment.sleep;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.datetime.DatePickerFragment;
import com.rorlig.babylog.ui.fragment.datetime.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Sleep"));

        dateStartHourTextView = (TextView) dateRangeStart.findViewById(R.id.hour);
        dateStartMinuteTextView = (TextView) dateRangeStart.findViewById(R.id.minute);
        dateStartSecondTextView = (TextView) dateRangeStart.findViewById(R.id.second);


        dateEndHourTextView = (TextView) dateRangeEnd.findViewById(R.id.hour);
        dateEndMinuteTextView = (TextView) dateRangeEnd.findViewById(R.id.minute);
        dateEndSecondTextView = (TextView) dateRangeEnd.findViewById(R.id.second);



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


    private class EventListener {
        public EventListener() {

        }
    }


    private void showTimePickerDialog(String label) {
        DialogFragment newFragment = new TimePickerFragment();

        Bundle args = new Bundle();

        //add label to indicate which date is being set...
        args.putString("label", label);

        //if the dialog is for the start date make sure the max date < end_date
        if (label.equals("start")) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
            String endDateString = dateEndHourTextView.getText().toString() + "-" + dateEndMinuteTextView.getText().toString() + "-" + dateEndSecondTextView.getText().toString();

            try {
                Log.d(TAG, "endDateString: " + endDateString);
                Date endDate = sdf.parse(endDateString);
                Log.d(TAG, "endDate " + endDate);
                args.putLong("max_start_date", endDate.getTime());
//                args.putLong("current_date", getStartTime().getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
//            args.putLong("current_date", getEndTime().getTime());
        }
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "datepicker");
    }
}
