package com.rorlig.babyapp.ui.fragment.growth;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.db.BabyLoggerORMUtils;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.Growth;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    private BabyLoggerORMUtils babyORMLiteUtils;
    private List<ParseObject> growthList;

    public GrowthStatsFragment() {
        super("Growth");
    }


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Growth Fragment"));


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


        //set the listener to the radiogroup...
        growthStatsRadioGroup.setOnCheckedChangeListener(this);

        updateListView();


    }

    @Override
    protected void setListResults(List<ParseObject> objects) {
        growthList =  objects;
        //setData
        setData(growthList, GrowthStatTab.WEIGHT);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_growth_stats, null);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d(TAG, "onCheckedChanged");
        switch (checkedId) {
            case R.id.growth_stats_height:
                setData(growthList, GrowthStatTab.HEIGHT);
                break;
            case R.id.growth_stats_weight:
                setData(growthList, GrowthStatTab.WEIGHT);
                break;
            case R.id.growth_stats_head_measurement:
                setData(growthList, GrowthStatTab.HEAD_MEASUREMENT);
                break;
        }
    }

//    private class EventListener {
//        public EventListener() {
//
//        }
//    }


    private void setData(List<ParseObject> growthList, GrowthStatTab growthStatTab) {

        //todo range...
        //todo shift on basis of height/weight/hm and the labels...

        Log.d(TAG, "growthList size " + growthList.size());///

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        int i = 0;
        for (ParseObject parseObject: growthList) {
//            Log.d(TAG, growthDao.toString());
            Growth growth = (Growth) parseObject;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM ''yy");
            float val;
            switch (growthStatTab) {
                case WEIGHT:
                    val =  (float) growth.getWeight().doubleValue();
                    break;
                case HEIGHT:
                    val = (float) growth.getHeight().doubleValue();
                    break;
                default:
                    val = (float) growth.getHeadMeasurement().doubleValue();
                    break;
            }
            xVals.add(simpleDateFormat.format(growth.getLogCreationDate()));
//            float val = (float) growthDao.getWeight().doubleValue();
            yVals.add(new Entry(val, i));
            i++;
        }


//        for (int i = 0; i < growthList.size(); i++) {
//
//            float mult = (20 + 1);
//            float val = (float) (Math.random() * mult) + 3;// + (float)
//            // ((mult *
//            // 0.1) / 10);
//            yVals.add(new Entry(val, i));
//        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, growthStatTab.toString());
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
//        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(getActivity().getResources().getColor(R.color.primary_green));
        set1.setCircleColor(getResources().getColor(R.color.primary_dark_green));
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
        set1.setDrawCubic(true);
//        set1.set

//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.GRAY, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets



        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);


        // set data
        lineChart.animateX(500,  Easing.EasingOption.EaseInCirc);


//        lineChart.setColor
        lineChart.setData(data);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
}
