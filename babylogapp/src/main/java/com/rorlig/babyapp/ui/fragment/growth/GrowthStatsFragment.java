package com.rorlig.babyapp.ui.fragment.growth;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.common.collect.Lists;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.Growth;
import com.rorlig.babyapp.parse_dao.HeadCircumferenceToAge;
import com.rorlig.babyapp.parse_dao.HeightToAge;
import com.rorlig.babyapp.parse_dao.WeightToAge;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
import com.rorlig.babyapp.utils.AppConstants;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by gaurav
 * Growth element..
 */
public class GrowthStatsFragment extends BaseInjectableListFragment implements RadioGroup.OnCheckedChangeListener {

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.growth_stats_line_chart)
    LineChart lineChart;

    @InjectView(R.id.growth_stats_radio_button)
    RadioGroup growthStatsRadioGroup;


    private String TAG = "GrowthStatsFragment";

//    private EventListener eventListener = new EventListener();
//    private boolean heightEmpty = true;
//    private boolean weightEmpty = true;
//    private boolean headMeasureEmpty = true;


//    @Inject
//    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;

    //your childs growth list
    private List<ParseObject> growthList = new ArrayList<>();

    //who/cdc weight list
    private List<ParseObject> weightList = new ArrayList<>();

    //who/cdc height list
    private List<ParseObject> heightList = new ArrayList<>();

    private List<ParseObject> headMeasurementList = new ArrayList<>();


    public GrowthStatsFragment() {
        super("Growth");
    }


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        Log.d(TAG, "onActivityCreated");

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Growth Fragment"));





    }





    private void getWeight() {

         if (weightList!=null && weightList.size()!=0) {
             setWeightData();

         } else {
             final ParseQuery<ParseObject> weightQuery = ParseQuery.getQuery("WeightToAge");
             //        preferences.getString()
             //do by baby sex ...
             weightQuery.whereEqualTo("Sex", 1);
             weightQuery.whereLessThanOrEqualTo("AgeMonths", 24);
             weightQuery.orderByAscending("AgeMonths");
             weightQuery.fromLocalDatastore();
             weightQuery.findInBackground(new FindCallback<ParseObject>() {
                 @Override
                 public void done(List<ParseObject> objects, ParseException e) {
                     Log.d(TAG, " weight  " + objects + " e " + e);
                     if (objects != null) {
                         weightList = objects;
                         setWeightData();
                     }
                 }

             });
         }

    }

    private void getHeight() {
        if (heightList!=null && heightList.size()!=0) {
            setHeightData();
        } else {
            final ParseQuery<ParseObject> heightQuery = ParseQuery.getQuery("HeightToAge");
//        preferences.getString()
            //do by baby sex ...
            heightQuery.whereEqualTo("Sex", 1);
            heightQuery.whereLessThanOrEqualTo("AgeMonths", 24);
            heightQuery.orderByAscending("AgeMonths");
            heightQuery.fromLocalDatastore();
            heightQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    Log.d(TAG, " weight  " + objects + " e " + e);
                    if (objects != null) {
                        heightList = objects;
                        setHeightData();
                    }

                }
            });
        }

    }

    private void getHeadMeasurement() {
        if (headMeasurementList!=null && headMeasurementList.size()!=0) {
            setHeadMeasurementData();
        } else {
            final ParseQuery<ParseObject> headCircumferenceToAge = ParseQuery.getQuery("HeadCircumferenceToAge");
//        preferences.getString()
            //do by baby sex ...
            headCircumferenceToAge.whereEqualTo("Sex", 1);
            headCircumferenceToAge.whereLessThanOrEqualTo("AgeMonths", 24);
            headCircumferenceToAge.orderByAscending("AgeMonths");
            headCircumferenceToAge.fromLocalDatastore();
            headCircumferenceToAge.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    Log.d(TAG, " weight  " + objects + " e " + e);
                    if (objects != null) {
                        headMeasurementList = objects;
                        setHeadMeasurementData();
                    }

                }
            });
        }
    }




    @Override
    protected void setListResults(List<ParseObject> objects) {
        Log.d(TAG, "get List Results");
        growthList = Lists.reverse(objects);
        //setData
//        setData(growthList, GrowthStatTab.WEIGHT);
//        setWeightData();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        if (savedInstanceState!=null) {
            String checkedRadioBtn = savedInstanceState.getString(AppConstants.GROWTH_STATS);
            initViews(checkedRadioBtn);
        } else {
            initViews(AppConstants.GROWTH_STATS_WEIGHT);
        }




    }

    private void initViews(String checkedRadioBtn) {

        // no description text
        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable value highlighting
        lineChart.setHighlightEnabled(true);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        // lineChart.setScaleXEnabled(true);
        // lineChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.primary_dark_green));
        lineChart.getLegend().setLabels(new String[]{"A ", " B"});


        //set the listener to the radiogroup...
        growthStatsRadioGroup.setOnCheckedChangeListener(this);


        //gets the growth data
        populateLocalStore(false);


        if (checkedRadioBtn.equals(AppConstants.GROWTH_STATS_HEIGHT)) {
            //getHeight
            getHeight();
        } else if (checkedRadioBtn.equals(AppConstants.GROWTH_STATS_WEIGHT)) {
            //getWeight
            getWeight();

        } else {
            //getHeadMeasurement
            getHeadMeasurement();
        }






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_growth_stats, null);
        ButterKnife.inject(this, view);
        Log.d(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d(TAG, "onCheckedChanged");
        switch (checkedId) {
            case R.id.growth_stats_height:
                getHeight();
//                setData(growthList, GrowthStatTab.HEIGHT);
                break;
            case R.id.growth_stats_weight:
                getHeight();
//                setData(growthList, GrowthStatTab.WEIGHT);
                break;
            case R.id.growth_stats_head_measurement:
                getHeadMeasurement();
//                setData(growthList, GrowthStatTab.HEAD_MEASUREMENT);
                break;
        }
    }



//    private class EventListener {
//        public EventListener() {
//
//        }
//    }


    private void setData(List<ParseObject> growthList, GrowthStatTab growthStatTab) {

//        //todo range...
//        //todo shift on basis of height/weight/hm and the labels...
//
//        Log.d(TAG, "growthList size " + growthList.size());///
//
//        ArrayList<String> xVals = new ArrayList<String>();
//        ArrayList<Entry> yVals = new ArrayList<Entry>();
//
//        int i = 0;
//
////        set1.set
//
////        set1.setDrawFilled(true);
//        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
//        // Color.GRAY, Color.WHITE, Shader.TileMode.MIRROR));
//
//        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
//        dataSets.add(set1); // add the datasets
//
//
//
//        // create a data object with the datasets
//        LineData data = new LineData(xVals, dataSets);
//
//
//        // set data
//        lineChart.animateX(500, Easing.EasingOption.EaseInCirc);
//
//
////        lineChart.setColor
//        lineChart.setData(data);
//        lineChart.notifyDataSetChanged();
//        lineChart.invalidate();
    }

    /*
     * Sets the growth data
     */
    private void setWeightData() {

        //todo range...
        //todo shift on basis of height/weight/hm and the labels...

        Log.d(TAG, "weightlist size " + weightList.size());///

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> p10 = new ArrayList<Entry>();
        ArrayList<Entry> p50 = new ArrayList<Entry>();
        ArrayList<Entry> p90 = new ArrayList<Entry>();
        ArrayList<Entry> p95 = new ArrayList<Entry>();

        //map of the weight value and the x-coordinate index
        Map<String, Integer> xIndexMap = new HashMap<>();

        int i =0;
        //add the weightlist as coordinates

        for (ParseObject parseObject: weightList) {
            WeightToAge weightToAge = (WeightToAge) parseObject;
            Log.d(TAG, "adding growth item to xVals and map " + weightToAge.getAgeToMonth());
//            insert(xVals, String.valueOf(weightToAge.getAgeToMonth()));
            xVals.add(String.valueOf(weightToAge.getAgeToMonth()));
            xIndexMap.put(String.valueOf(weightToAge.getAgeToMonth()), i);
            i++;
        }
        //add the growth entries of your child to x coordinats
        if (growthList!=null) {
            for (ParseObject parseObject: growthList) {
                Growth growth = (Growth) parseObject;
                String weekDiff = getDiff(growth.getLogCreationDate(), getDob());
                if (xIndexMap.get(weekDiff)==null) {
                    Log.d(TAG, "adding growth item to xVals and map " + weekDiff);
                    xVals.add(String.valueOf(weekDiff));
                    xIndexMap.put(weekDiff, i);
                    i++;
                } else {
                    Log.d(TAG, "already in the map " + weekDiff +  " value " + xIndexMap.get(weekDiff));

                }


            }
        }


        // now sort the xcordinates based on the value...
        Collections.sort(xVals, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                Double leftValue = Double.parseDouble(lhs);
                Double rightValue = Double.parseDouble(rhs);
                return Double.compare(leftValue, rightValue);
//                return lhs.compareTo(rhs);
            }
        });

        //clear the map and rebuild..
        xIndexMap.clear();

        i = 0;
        for (String val : xVals) {
            Log.d(TAG, "val " + val);
            xIndexMap.put(val, i);
            ++i;
        }

        //build the 10, 50 , 90, 95 % maps...
        for (ParseObject parseObject: weightList) {
            WeightToAge weightToAge = (WeightToAge) parseObject;
            p10.add(new Entry((float) (weightToAge.getP10()*2.2),
                    xIndexMap.get(String.valueOf(weightToAge.getAgeToMonth()))));
            p50.add(new Entry((float) (weightToAge.getP50() * 2.2),
                    xIndexMap.get(String.valueOf(weightToAge.getAgeToMonth()))));

            p90.add(new Entry((float) (weightToAge.getP90() * 2.2),
                    xIndexMap.get(String.valueOf(weightToAge.getAgeToMonth()))));

//            p95.add(new Entry((float)(weightToAge.getP95()*2.2),
//                    xIndexMap.get(String.valueOf(weightToAge.getAgeToMonth()))));
        }


        //build your childs growth y-values
        List<Entry> yourChild = new ArrayList<>();
        for (ParseObject parseObject: growthList) {
            Growth growth = (Growth) parseObject;
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM ''yy");
            float val = (float) growth.getWeight().doubleValue();
            String weekDiff = getDiff(growth.getLogCreationDate(), getDob());
            Log.d(TAG, "weekDiff: " + weekDiff + " map " + xIndexMap.get(weekDiff));
//            xVals.add(weekDiff);
//            float val = (float) growthDao.getWeight().doubleValue();
            yourChild.add(new Entry(val, xIndexMap.get(weekDiff)));
//            i++;
        }




        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(p10, "10%");
        LineDataSet set2 = new LineDataSet(p50, "50%");
        LineDataSet set3 = new LineDataSet(p90, "90%");
//        LineDataSet set4 = new LineDataSet(p95, "95%");
        LineDataSet set5 = new LineDataSet(yourChild, preferences.getString("name","Your Child"));




        //set colors get feedback on the colors...: - todo
        set1.setColor(getActivity().getResources().getColor(R.color.red_brown));
        set1.setCircleColor(getResources().getColor(R.color.red_brown));
        set2.setColor(getActivity().getResources().getColor(R.color.orange_red));
        set2.setCircleColor(getResources().getColor(R.color.orange_red));
        set3.setColor(getActivity().getResources().getColor(R.color.blue_eyes));
        set3.setCircleColor(getResources().getColor(R.color.blue_eyes));
//        set4.setColor(getActivity().getResources().getColor(R.color.dark_blue));
//        set4.setCircleColor(getResources().getColor(R.color.dark_blue));

        //special colors your data...
        set5.setColor(getActivity().getResources().getColor(R.color.primary_green));
        set5.setCircleColor(getResources().getColor(R.color.primary_dark_green));
        set5.setLineWidth(1f);
        set5.setCircleSize(3f);
        set5.setDrawCircleHole(false);
        set5.setValueTextSize(9f);
        set5.setFillAlpha(65);
        set5.setFillColor(Color.BLACK);
        set5.setDrawCubic(true);

        //add the data sets to the chart...
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);
        dataSets.add(set3);
//        dataSets.add(set4);
        dataSets.add(set5);


        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);


        // set data
        lineChart.animateX(500, Easing.EasingOption.EaseInCirc);


//        lineChart.setColor
        lineChart.setData(data);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();


    }



    /*
    * Sets the growth data
    */
    private void setHeightData() {

        //todo range...
        //todo shift on basis of height/weight/hm and the labels...

//        Log.d(TAG, "heightlist size " + heightList.size());///

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> p10 = new ArrayList<Entry>();
        ArrayList<Entry> p50 = new ArrayList<Entry>();
        ArrayList<Entry> p90 = new ArrayList<Entry>();
        ArrayList<Entry> p95 = new ArrayList<Entry>();

        //map of the weight value and the x-coordinate index
        Map<String, Integer> xIndexMap = new HashMap<>();

        int i =0;
        //add the weightlist as coordinates
        for (ParseObject parseObject: heightList) {
            HeightToAge heightToAge = (HeightToAge) parseObject;
            Log.d(TAG, "adding growth item to xVals and map " + heightToAge.getAgeToMonth());
//            insert(xVals, String.valueOf(weightToAge.getAgeToMonth()));
            xVals.add(String.valueOf(heightToAge.getAgeToMonth()));
            xIndexMap.put(String.valueOf(heightToAge.getAgeToMonth()), i);
            i++;
        }
        //add the growth entries of your child to x coordinats
        for (ParseObject parseObject: growthList) {
            Growth growth = (Growth) parseObject;
            String weekDiff = getDiff(growth.getLogCreationDate(), getDob());
            if (xIndexMap.get(weekDiff)==null) {
                Log.d(TAG, "adding growth item to xVals and map " + weekDiff);
                xVals.add(String.valueOf(weekDiff));
                xIndexMap.put(weekDiff, i);
                i++;
            } else {
                Log.d(TAG, "already in the map " + weekDiff +  " value " + xIndexMap.get(weekDiff));

            }


        }

        // now sort the xcordinates based on the value...
        Collections.sort(xVals, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                Double leftValue = Double.parseDouble(lhs);
                Double rightValue = Double.parseDouble(rhs);
                return Double.compare(leftValue, rightValue);
//                return lhs.compareTo(rhs);
            }
        });

        //clear the map and rebuild..
        xIndexMap.clear();

        i = 0;
        for (String val : xVals) {
            Log.d(TAG, "val " + val);
            xIndexMap.put(val, i);
            ++i;
        }

        //build the 10, 50 , 90, 95 % maps...
        for (ParseObject parseObject: heightList) {
            HeightToAge heightToAge = (HeightToAge) parseObject;
            p10.add(new Entry((float) (heightToAge.getP10()/2.54),
                    xIndexMap.get(String.valueOf(heightToAge.getAgeToMonth()))));
            p50.add(new Entry((float) (heightToAge.getP50() /2.54),
                    xIndexMap.get(String.valueOf(heightToAge.getAgeToMonth()))));

            p90.add(new Entry((float) (heightToAge.getP90() / 2.54),
                    xIndexMap.get(String.valueOf(heightToAge.getAgeToMonth()))));

//            p95.add(new Entry((float)(heightToAge.getP95()/2.54),
//                    xIndexMap.get(String.valueOf(heightToAge.getAgeToMonth()))));
        }


        //build your childs growth y-values
        List<Entry> yourChild = new ArrayList<>();
        for (ParseObject parseObject: growthList) {
            Growth growth = (Growth) parseObject;
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM ''yy");
            float val = (float) growth.getHeight().doubleValue();
            String weekDiff = getDiff(growth.getLogCreationDate(), getDob());
            Log.d(TAG, "weekDiff: " + weekDiff + " map " + xIndexMap.get(weekDiff));
//            xVals.add(weekDiff);
//            float val = (float) growthDao.getWeight().doubleValue();
            yourChild.add(new Entry(val, xIndexMap.get(weekDiff)));
//            i++;
        }




        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(p10, "10%");
        LineDataSet set2 = new LineDataSet(p50, "50%");
        LineDataSet set3 = new LineDataSet(p90, "90%");
//        LineDataSet set4 = new LineDataSet(p95, "95%");
        LineDataSet set5 = new LineDataSet(yourChild, preferences.getString("name","Your Child"));




        //set colors get feedback on the colors...: - todo
        set1.setColor(getActivity().getResources().getColor(R.color.red_brown));
        set1.setCircleColor(getResources().getColor(R.color.red_brown));
        set2.setColor(getActivity().getResources().getColor(R.color.orange_red));
        set2.setCircleColor(getResources().getColor(R.color.orange_red));
        set3.setColor(getActivity().getResources().getColor(R.color.blue_eyes));
        set3.setCircleColor(getResources().getColor(R.color.blue_eyes));
//        set4.setColor(getActivity().getResources().getColor(R.color.dark_blue));
//        set4.setCircleColor(getResources().getColor(R.color.dark_blue));

        //special colors your data...
        set5.setColor(getActivity().getResources().getColor(R.color.primary_green));
        set5.setCircleColor(getResources().getColor(R.color.primary_dark_green));
        set5.setLineWidth(1f);
        set5.setCircleSize(3f);
        set5.setDrawCircleHole(false);
        set5.setValueTextSize(9f);
        set5.setFillAlpha(65);
        set5.setFillColor(Color.BLACK);
        set5.setDrawCubic(true);

        //add the data sets to the chart...
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);
        dataSets.add(set3);
//        dataSets.add(set4);
        dataSets.add(set5);


        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);


        // set data
        lineChart.animateX(500, Easing.EasingOption.EaseInCirc);


//        lineChart.setColor
        lineChart.setData(data);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();


    }

    private void setHeadMeasurementData() {

        //todo range...
        //todo shift on basis of height/weight/hm and the labels...

//        Log.d(TAG, "heightlist size " + heightList.size());///

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> p10 = new ArrayList<Entry>();
        ArrayList<Entry> p50 = new ArrayList<Entry>();
        ArrayList<Entry> p90 = new ArrayList<Entry>();
        ArrayList<Entry> p95 = new ArrayList<Entry>();

        //map of the weight value and the x-coordinate index
        Map<String, Integer> xIndexMap = new HashMap<>();

        int i =0;
        //add the weightlist as coordinates
        for (ParseObject parseObject: headMeasurementList) {
            HeadCircumferenceToAge headCircumferenceToAge = (HeadCircumferenceToAge) parseObject;
            Log.d(TAG, "adding growth item to xVals and map " + headCircumferenceToAge.getAgeToMonth());
//            insert(xVals, String.valueOf(weightToAge.getAgeToMonth()));
            xVals.add(String.valueOf(headCircumferenceToAge.getAgeToMonth()));
            xIndexMap.put(String.valueOf(headCircumferenceToAge.getAgeToMonth()), i);
            i++;
        }
        //add the growth entries of your child to x coordinates
        for (ParseObject parseObject: growthList) {
            Growth growth = (Growth) parseObject;
            String weekDiff = getDiff(growth.getLogCreationDate(), getDob());
            if (xIndexMap.get(weekDiff)==null) {
                Log.d(TAG, "adding growth item to xVals and map " + weekDiff);
                xVals.add(String.valueOf(weekDiff));
                xIndexMap.put(weekDiff, i);
                i++;
            } else {
                Log.d(TAG, "already in the map " + weekDiff +  " value " + xIndexMap.get(weekDiff));

            }


        }

        // now sort the xcordinates based on the value...
        Collections.sort(xVals, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                Double leftValue = Double.parseDouble(lhs);
                Double rightValue = Double.parseDouble(rhs);
                return Double.compare(leftValue, rightValue);
//                return lhs.compareTo(rhs);
            }
        });

        //clear the map and rebuild..
        xIndexMap.clear();

        i = 0;
        for (String val : xVals) {
            Log.d(TAG, "val " + val);
            xIndexMap.put(val, i);
            ++i;
        }

        //build the 10, 50 , 90, 95 % maps...
        for (ParseObject parseObject: headMeasurementList) {
            HeadCircumferenceToAge headCircumferenceToAge = (HeadCircumferenceToAge) parseObject;
            p10.add(new Entry((float) (headCircumferenceToAge.getP10()/2.54),
                    xIndexMap.get(String.valueOf(headCircumferenceToAge.getAgeToMonth()))));
            p50.add(new Entry((float) (headCircumferenceToAge.getP50() /2.54),
                    xIndexMap.get(String.valueOf(headCircumferenceToAge.getAgeToMonth()))));

            p90.add(new Entry((float) (headCircumferenceToAge.getP90() / 2.54),
                    xIndexMap.get(String.valueOf(headCircumferenceToAge.getAgeToMonth()))));

//            p95.add(new Entry((float)(headCircumferenceToAge.getP95()/2.54),
//                    xIndexMap.get(String.valueOf(headCircumferenceToAge.getAgeToMonth()))));
        }


        //build your childs growth y-values
        List<Entry> yourChild = new ArrayList<>();
        for (ParseObject parseObject: growthList) {
            Growth growth = (Growth) parseObject;
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM ''yy");
            float val = (float) growth.getHeadMeasurement().doubleValue();
            String weekDiff = getDiff(growth.getLogCreationDate(), getDob());
            Log.d(TAG, "weekDiff: " + weekDiff + " map " + xIndexMap.get(weekDiff));
//            xVals.add(weekDiff);
//            float val = (float) growthDao.getWeight().doubleValue();
            yourChild.add(new Entry(val, xIndexMap.get(weekDiff)));
//            i++;
        }




        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(p10, "10%");
        LineDataSet set2 = new LineDataSet(p50, "50%");
        LineDataSet set3 = new LineDataSet(p90, "90%");
//        LineDataSet set4 = new LineDataSet(p95, "95%");
        LineDataSet set5 = new LineDataSet(yourChild, preferences.getString("name","Your Child"));




        //set colors get feedback on the colors...: - todo
        set1.setColor(getActivity().getResources().getColor(R.color.red_brown));
        set1.setCircleColor(getResources().getColor(R.color.red_brown));
        set2.setColor(getActivity().getResources().getColor(R.color.orange_red));
        set2.setCircleColor(getResources().getColor(R.color.orange_red));
        set3.setColor(getActivity().getResources().getColor(R.color.blue_eyes));
        set3.setCircleColor(getResources().getColor(R.color.blue_eyes));
//        set4.setColor(getActivity().getResources().getColor(R.color.dark_blue));
//        set4.setCircleColor(getResources().getColor(R.color.dark_blue));

        //special colors your data...
        set5.setColor(getActivity().getResources().getColor(R.color.primary_green));
        set5.setCircleColor(getResources().getColor(R.color.primary_dark_green));
        set5.setLineWidth(1f);
        set5.setCircleSize(3f);
        set5.setDrawCircleHole(false);
        set5.setValueTextSize(9f);
        set5.setFillAlpha(65);
        set5.setFillColor(Color.BLACK);
        set5.setDrawCubic(true);

        //add the data sets to the chart...
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);
        dataSets.add(set3);
//        dataSets.add(set4);
        dataSets.add(set5);


        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);


        // set data
        lineChart.animateX(500, Easing.EasingOption.EaseInCirc);


//        lineChart.setColor
        lineChart.setData(data);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

    }

//    public LineDataSet getBabyGrowthData(GrowthStatTab growthStatTab){
//        ArrayList<String> xVals = new ArrayList<String>();
//
//        List<Entry> yVals = new ArrayList<>();
//        int i =0;
//        for (ParseObject parseObject: growthList) {
////            Log.d(TAG, growthDao.toString());
//            Growth growth = (Growth) parseObject;
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM ''yy");
//            float val;
//            switch (growthStatTab) {
//                case WEIGHT:
//                    val =  (float) growth.getWeight().doubleValue();
//                    break;
//                case HEIGHT:
//                    val = (float) growth.getHeight().doubleValue();
//                    break;
//                default:
//                    val = (float) growth.getHeadMeasurement().doubleValue();
//                    break;
//            }
//            String weekDiff = getDiff(growth.getLogCreationDate(), getDob());
//            Log.d(TAG, weekDiff);
//            xVals.add(simpleDateFormat.format(growth.getLogCreationDate()));
////            float val = (float) growthDao.getWeight().doubleValue();
//            yVals.add(new Entry(val, i));
//            i++;
//        }
//
//
//
//        // create a dataset and give it a type
//        LineDataSet set1 = new LineDataSet(yVals, growthStatTab.toString());
//        // set1.setFillAlpha(110);
//        // set1.setFillColor(Color.RED);
//
//        // set the line to be drawn like this "- - - - - -"
////        set1.enableDashedLine(10f, 5f, 0f);
//        set1.setColor(getActivity().getResources().getColor(R.color.primary_green));
//        set1.setCircleColor(getResources().getColor(R.color.primary_dark_green));
//        set1.setLineWidth(1f);
//        set1.setCircleSize(3f);
//        set1.setDrawCircleHole(false);
//        set1.setValueTextSize(9f);
//        set1.setFillAlpha(65);
//        set1.setFillColor(Color.BLACK);
//        set1.setDrawCubic(true);
//
//        return set1;
//    }

    /*
     * Returns the difference in months between two dates
     * @param logCreationDate - the date at which the entry was created
     * @param dob - the date of birth of the user.
     */
    private String getDiff(Date logCreationDate, Date dob) {
        DateTime logTime = new DateTime(logCreationDate);
        DateTime dobTime = new DateTime(dob);
        Weeks weeks = Weeks.weeksBetween(dobTime, logTime);

        Log.d(TAG, "diff " + weeks.getWeeks());
//        float weekDiff = (float)(logCreationDate.getTime() - dob.getTime())/(float)(1000*60*60*24*7*4);
//        Log.d(TAG, "weekDiff " + weekDiff);

        return String.valueOf(((double)weeks.getWeeks())/4.0);
    }

    private Date getDob(){
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy,MM,dd");
        try {
            String dob = preferences.getString("dob", "");
            Log.d(TAG, "dob " + dob);
            Date date =  simpleDateFormat.parse(dob);

            Log.d(TAG, "date" + date);
            return date;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        switch (growthStatsRadioGroup.getCheckedRadioButtonId()) {
            case R.id.growth_stats_weight:
                outState.putString(AppConstants.GROWTH_STATS, AppConstants.GROWTH_STATS_WEIGHT);
                break;
            case R.id.growth_stats_head_measurement:
                outState.putString(AppConstants.GROWTH_STATS, AppConstants.GROWTH_STATS_HEADMEASUREMENT);
                break;
            case R.id.growth_stats_height:
                outState.putString(AppConstants.GROWTH_STATS, AppConstants.GROWTH_STATS_HEIGHT);
                break;
        }
//        outState.putParcelableArrayList("Weight", (ArrayList<? extends Parcelable>) weightList);

    }


}
