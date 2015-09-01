package com.rorlig.babyapp.ui.fragment.sleep;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.db.BabyLoggerORMLiteHelper;
import com.rorlig.babyapp.otto.SleepStatsEvent;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author gaurav gupta
 * diaper stats fragment
 */
public class SleepStatsFragment extends InjectableFragment implements RadioGroup.OnCheckedChangeListener {
//        implements RadioGroup.OnCheckedChangeListener {

    @ForActivity
    @Inject
    Context context;
    private EventListener eventListener = new EventListener();

    public enum SleepStatsType {
        WEEKLY, MONTHLY, sleepStatsType, YEARLY
    }

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    protected String[] mDays = new String[] {
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };


    @InjectView(R.id.sleep_stats_time)
    RadioGroup sleepChangeRadioGroup;

    @InjectView(R.id.sleep_bar_chart)
    BarChart barChart;

    private String TAG = "SleepStatsFragment";




    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;

//    private BabyLoggerORMUtils babyORMLiteUtils;
    private List<String[]> sleepDaoList;
//    private List<String[]> diaperChangeDaoList;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Stats Fragment"));
//        babyORMLiteUtils = new BabyLoggerORMUtils(getActivity());

//        barChart.setOnChartValueSelectedListener(this);
//s
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setHorizontalScrollBarEnabled(true);

//        barChart.setBackgroundColor(getResources().getColor(R.color.primary_purple));
//        barChart.setGridBackgroundColor(getResources().getColor(R.color.primary_purple));
        barChart.getXAxis().setTextColor(getResources().getColor(R.color.primary_gray_dark));
//        barChart.().setTextColor(getResources().getColor(R.color.white));
        barChart.getLegend().setTextColor(getResources().getColor(R.color.primary_gray_dark));


        Log.d(TAG, "get the data by day");
        SleepStatsUtility.getSleepDaysofWeek();
//            sleepDaoList =  babyORMLiteUtils.getSleepByDayofWeek();
//            setData(SleepStatsType.WEEKLY);
        //
        sleepChangeRadioGroup.setOnCheckedChangeListener(this);


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sleep_stats, null);
        ButterKnife.inject(this, view);
        return view;
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.sleep_stats_weekly:
//                    sleepDaoList =  babyORMLiteUtils.getSleepByDayofWeek();
                SleepStatsUtility.getSleepDaysofWeek();

                barChart.setMaxVisibleValueCount(7);
//                    barChart.getXAxis().setLabelsToSkip(0);
                setData(SleepStatsType.WEEKLY);

                break;
            case R.id.sleep_stats_monthly:
                SleepStatsUtility.getSleepByWeekofMonth();

//                    sleepDaoList =  babyORMLiteUtils.getSleepByWeekofMonth();
                barChart.setMaxVisibleValueCount(5);
//                    barChart.getXAxis().setLabelsToSkip(0);

                setData(SleepStatsType.MONTHLY);
                break;
            default:
//                    sleepDaoList =  babyORMLiteUtils.getSleepByMonthofYear();
                SleepStatsUtility.getSleepByMonthofYear();

                barChart.setMaxVisibleValueCount(12);
//                    barChart.getXAxis().setLabelsToSkip(0);

                setData(SleepStatsType.YEARLY);
        }

    }



    private String getDateRangeForWeek(int weekNumber){
        Log.d(TAG, "weekNumber " + weekNumber);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        DateTime weekStartDate = new DateTime().withWeekOfWeekyear(weekNumber);
        DateTime weekEndDate = new DateTime().withWeekOfWeekyear(weekNumber + 1);
        String returnString = weekStartDate.toString(DateTimeFormat.forPattern("dd MMM"))
                                + " to "
                                + weekEndDate.toString(DateTimeFormat.forPattern("dd MMM"));
        Log.d(TAG, "returnString : " + returnString);
//        new DateTime().
        return returnString;


    }

    private void setData(SleepStatsType sleepStatsType) {

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        int i = 0;
        List<String[]> fullList = getFullList(sleepStatsType);

        for (String[] sleepValue: fullList) {
            Float value = Float.parseFloat(sleepValue[1])/60;
            String xValue = sleepValue[0];
            switch (sleepStatsType) {
                case WEEKLY:
                    break;
                case MONTHLY:
                    xValue = sleepValue[0];
                    break;
                case YEARLY:
                    xValue = sleepValue[0];


                    break;
            }
            xVals.add(xValue);

            yVals.add(new BarEntry(value, i));
            i++;
        }
        barChart.animateY(1000);
        BarDataSet set1 = new BarDataSet(yVals, sleepStatsType.toString());
        set1.setBarSpacePercent(35f);
        set1.setColor(getResources().getColor(R.color.primary_gray));
        set1.setValueTextColor(getResources().getColor(R.color.primary_gray));
        set1.setValueTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        set1.setC
        set1.setHighLightColor(getResources().getColor(R.color.primary_gray));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextColor(getResources().getColor(R.color.primary_gray));
        barChart.setData(data);
//        barChart.setZ
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    private List<String[]> getFullList(SleepStatsFragment.SleepStatsType sleepStatsType) {

        switch (sleepStatsType) {
            case WEEKLY:
                return getFullListDayofWeek();
            case MONTHLY:
                return getFullListWeekofMonth();
            case YEARLY:
                return getFullListMonthofDay();
        }
        return null;

    }

    private List<String[]> getFullListMonthofDay() {

        List<String[]> returnList = new ArrayList<String[]>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        startTime.add(Calendar.DATE, -(7 * 52 + 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.MINUTE, 0);
        endTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        endTime.add(Calendar.DATE, 2);

        for (Date date = startTime.getTime(); startTime.before(endTime);
             startTime.add(Calendar.MONTH, 1), date = startTime.getTime()) {
            // Do your job here with `date`.
            boolean found = false;
            String[] temp  = new String[2];

            String formattedDate = sdf.format(startTime.getTime());
            Log.d(TAG, formattedDate);
            for (String[] diaperChangeDao: sleepDaoList) {

                Log.d(TAG, diaperChangeDao[0]);

                if (diaperChangeDao[0].equals(formattedDate)) {
                    Log.d(TAG, "found ");
                    //                        sdf.parse(diaperChangeDao[0]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    temp[0] = mMonths[Integer.parseInt(diaperChangeDao[0].substring(5))-1];

                    temp[1] = diaperChangeDao[1];
                    found=true;
                }
            }
            if (!found) {
//                        sdf.parse(diaperChangeDao[0]);
                Log.d(TAG, " not found");
                //                    sdf.parse(sdf.format(date));
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                temp[0] = mMonths[Integer.parseInt(formattedDate.substring(5))-1];
                temp[1]="0";

            }
            returnList.add(temp);


        }
        return returnList;
    }


    private List<String[]> getFullListWeekofMonth() {
        List<String[]> returnList = new ArrayList<String[]>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        startTime.add(Calendar.DATE, -(7*4+1));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.MINUTE, 0);
        endTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        endTime.add(Calendar.DATE, 2);
        for (Date date = startTime.getTime(); startTime.before(endTime);
             startTime.add(Calendar.DATE, 7), date = startTime.getTime()) {
            // Do your job here with `date`.
            boolean found = false;
            String[] temp  = new String[2];

//           Log.d(TAG, "week no" + startTime.get(Calendar.WEEK_OF_YEAR));
            int weekNo = startTime.get(Calendar.WEEK_OF_YEAR);

            for (String[] diaperChangeDao: sleepDaoList) {

                if (diaperChangeDao[0].equals(String.valueOf(weekNo))) {
                    Log.d(TAG, "found ");
                    //                        sdf.parse(diaperChangeDao[0]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    temp[0] = getDateRangeForWeek(weekNo);
                    temp[1] = diaperChangeDao[1];
                    found=true;
                }
            }
            if (!found) {
//                        sdf.parse(diaperChangeDao[0]);
                Log.d(TAG, " not found");
                //                    sdf.parse(sdf.format(date));
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                temp[0] = getDateRangeForWeek(weekNo);
                temp[1]="0";

            }
            returnList.add(temp);

//                    Log.d(TAG, " date " + sdf.format(date));
        }


        printList(returnList);
        return returnList;
    }

    private List<String[]> getFullListDayofWeek() {

        List<String[]> returnList = new ArrayList<String[]>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        startTime.add(Calendar.DATE, -8);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.MINUTE, 0);
        endTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        endTime.add(Calendar.DATE, 1);
        Calendar calendar = Calendar.getInstance();

        for (Date date = startTime.getTime(); startTime.before(endTime);
             startTime.add(Calendar.DATE, 1), date = startTime.getTime()) {
            // Do your job here with `date`.
            boolean found = false;
            String[] temp  = new String[2];
            Log.d(TAG, "date " + sdf.format(date));
            for (String[] sleepDao: sleepDaoList) {
                Log.d(TAG, sleepDao[0]);
                if (sleepDao[0].equals(sdf.format(date))) {
//                    Log.d(TAG, "found " + sdf.format(date));
                    try {
                        sdf.parse(sleepDao[0]);
                        calendar.setTime(date);
                        temp[0] = mDays[calendar.get(Calendar.DAY_OF_WEEK)-1];
                        temp[1] = sleepDao[1];
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    temp = diaperChangeDao;
                    found=true;
                }
            }
            if (!found) {
//                        sdf.parse(diaperChangeDao[0]);
//                Log.d(TAG, " not found");
                try {
                    sdf.parse(sdf.format(date));
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    temp[0] = mDays[c.get(Calendar.DAY_OF_WEEK)-1];
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                temp[1]="0";

            }

            returnList.add(temp);

//                    Log.d(TAG, " date " + sdf.format(date));
        }


        printList(returnList);

        return returnList;

        }

    private void printList(List<String[]> temp) {
        for (String[] t1: temp) {
//            for (String t: t1) {
                Log.d(TAG, " t0  " + t1[0] + " t1 " + t1[1]);
//            }
        }
    }

    private class EventListener {
        @Subscribe
        public void onSleepStatsEvent(SleepStatsEvent event) {
            sleepDaoList = event.getList();
            setData(event.getSleepStatsType());
        }
    }
}
