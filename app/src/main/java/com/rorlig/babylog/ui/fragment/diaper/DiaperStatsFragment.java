package com.rorlig.babylog.ui.fragment.diaper;

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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.j256.ormlite.dao.GenericRawResults;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.dao.GrowthDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.db.BabyLoggerORMUtils;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.growth.GrowthStatTab;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author gaurav gupta
 * diaper stats fragment
 */
public class DiaperStatsFragment extends InjectableFragment implements RadioGroup.OnCheckedChangeListener {
//        implements RadioGroup.OnCheckedChangeListener {

    @ForActivity
    @Inject
    Context context;



    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

//    @InjectView(R.id.growth_stats_line_chart)
//    LineChart lineChart;

    @InjectView(R.id.diaper_change_stats_time)
    RadioGroup diaperChangeRadioGroup;

    @InjectView(R.id.diaper_bar_chart)
    BarChart barChart;

    private String TAG = "DiaperStatsFragment";




    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;

    private BabyLoggerORMUtils babyORMLiteUtils;
    private List<String[]> diaperChangeDaoList;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Growth Fragment"));
        babyORMLiteUtils = new BabyLoggerORMUtils(getActivity());

//        barChart.setOnChartValueSelectedListener(this);
//s
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(7);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);


        try {
            Log.d(TAG, "get the data by day");
            diaperChangeDaoList =  babyORMLiteUtils.getDiaperChangeByDay();
            //setData
//            setData(12, 60);
            setData(diaperChangeDaoList, DiaperChangeStatsType.DAY);
        } catch (SQLException   e) {
            e.printStackTrace();
        }

        diaperChangeRadioGroup.setOnCheckedChangeListener(this);


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diaper_change_stats, null);
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

        try {
            switch (checkedId) {
                case R.id.diaper_change_stats_week:
                    diaperChangeDaoList =  babyORMLiteUtils.getDiaperChangeByWeek();
                    setData(diaperChangeDaoList, DiaperChangeStatsType.WEEK);
                    break;
                case R.id.diaper_change_stats_month:
                    diaperChangeDaoList =  babyORMLiteUtils.getDiaperChangeByMonth();
                    setData(diaperChangeDaoList, DiaperChangeStatsType.MONTH);
                    break;
                default:
                    diaperChangeDaoList =  babyORMLiteUtils.getDiaperChangeByDay();
                    setData(diaperChangeDaoList, DiaperChangeStatsType.DAY);
            }

        } catch (SQLException sqlException) {
            Log.e(TAG, "Exception: " + sqlException);
        }

    }


    private class EventListener {
        public EventListener() {

        }
    }



    private void setData(List<String[]> diaperChangeDaoList, DiaperChangeStatsType diaperChangeStatsType) {



//        Log.d(TAG, "diaperChangeDaoList size " + diaperChangeDaoList.size());///
//
//        Log.d(TAG, "creating diaper change graph");

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        int i = 0;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM");

        for (String[] diaperChangeResult: diaperChangeDaoList) {
            Log.d(TAG, "iterating the result set");
            Log.d(TAG, " i " + i + "  date " + diaperChangeResult[0] + " count " + diaperChangeResult[1]);
            Integer value = Integer.parseInt(diaperChangeResult[1]);
            xVals.add(diaperChangeResult[0]);
            yVals.add(new BarEntry(value, i));
            i++;
        }

        BarDataSet set1 = new BarDataSet(yVals, diaperChangeStatsType.toString());
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);


        barChart.setData(data);

        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }
}
