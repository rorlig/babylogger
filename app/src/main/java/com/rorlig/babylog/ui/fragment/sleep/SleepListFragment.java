package com.rorlig.babylog.ui.fragment.sleep;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.j256.ormlite.stmt.PreparedQuery;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.BaseDao;
import com.rorlig.babylog.dao.SleepDao;
import com.rorlig.babylog.db.BabyLoggerORMUtils;
import com.rorlig.babylog.otto.SleepLogCreated;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.other.AddItemTypes;
import com.rorlig.babylog.otto.events.stats.StatsItemEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.adapter.DateSectionizer;
import com.rorlig.babylog.ui.adapter.SleepAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//import android.widget.Button;

/**
 * @author gaurav gupta
 * history of sleep logs
 */
public class SleepListFragment extends InjectableFragment
        implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<List<SleepDao>> {

    @ForActivity
    @Inject
    Context context;

//    @InjectView(R.id.currentDate)
//    TextView currentDate;
//
//    @InjectView(R.id.currentTime)
//    TextView currentTime;



//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;

    @InjectView(R.id.sleep_list)
    ListView sleepListView;

    @InjectView(R.id.empty_view)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;

//    @InjectView(R.id.diaper_bar_chart)
//    BarChart barChart;





    @InjectView(R.id.add_sleep_item)
    FloatingActionButton btnAddSleep;


    private BabyLoggerORMUtils babyORMLiteUtils;
    private List<SleepDao> sleepList;
    private SleepAdapter sleepAdapter;
    private SimpleSectionAdapter<BaseDao> sectionAdapter;
    private int LOADER_ID=2;

    @OnClick(R.id.add_sleep_item)
    public void onSleepAddItemClicked(){
//        scopedBus.post(new AddDiaperChangeEvent());

        scopedBus.post(new AddItemEvent(AddItemTypes.SLEEP_LOG));
    }

    @OnClick(R.id.add_item)
    public void onAddItem(){
//        scopedBus.post(new AddDiaperChangeEvent());

        scopedBus.post(new AddItemEvent(AddItemTypes.SLEEP_LOG));
    }
//
//    @OnClick(R.id.add_diaper_item)
//    public void onDiaperChangeBtnClicked(){
////        scopedBus.post(new AddDiaperChangeEvent());
//        Log.d(TAG, "add diaper item clicked");
//        scopedBus.post(new AddItemEvent(AddItemTypes.DIAPER_CHANGE));
//    }

//    Typeface typeface;

    private String TAG = "SleepListFragment";

    private EventListener eventListener = new EventListener();

    PreparedQuery<SleepDao> queryBuilder;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

//        sleepListView.setEmptyView(emptyView);

//        btnDiaperChange.setTypeface(typeface);

//        errorText.setTypeface(typeface);

        scopedBus.post(new FragmentCreated("Diaper Change List"));



        babyORMLiteUtils = new BabyLoggerORMUtils(getActivity());
        try {
            queryBuilder = babyORMLiteUtils.getSleepDao().queryBuilder().orderBy("date", false).prepare();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sleepList = new ArrayList<SleepDao>();
        Log.d(TAG, "number of diaper changes " + sleepList.size());
        sleepAdapter = new SleepAdapter(getActivity(), R.layout.list_item_diaper_change, sleepList);
        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
                sleepAdapter, R.layout.section_header_gray, R.id.title,
                new DateSectionizer());
        sleepListView.setAdapter(sectionAdapter);
        sleepListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        try {
            sleepList =  babyORMLiteUtils.getSleepList();
//            setData(diaperChangeDaoList, DiaperChangeStatsType.WEEKLY);

        } catch (SQLException e) {
            e.printStackTrace();
        }




    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sleep_list, null);
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
        getLoaderManager().restartLoader(LOADER_ID, null, this);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_diaper_change, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
//            case R.id.action_add:
//                scopedBus.post(new AddItemEvent(AddItemTypes.DIAPER_CHANGE));
//                return true;
            case R.id.action_stats:
                scopedBus.post(new StatsItemEvent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public Loader<List<SleepDao>> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "create Loader");

        return new SleepLoader(getActivity());

    }

    @Override
    public void onLoadFinished(Loader<List<SleepDao>> loader, List<SleepDao> data) {

        Log.d(TAG, "loader finished");

        if (data.size()>0) {
            emptyView.setVisibility(View.GONE);
//            barChart.setVisibility(View.VISIBLE);

            sleepListView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            sleepListView.setVisibility(View.GONE);

        }

        sleepList = data;

        sleepAdapter = new SleepAdapter(getActivity(), R.layout.list_item_sleep, sleepList);

//        diaperChangeAdapter.update(diaperChangeDaoList);

        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
                sleepAdapter, R.layout.section_header_gray, R.id.title,
                new DateSectionizer());

        sleepListView.setAdapter(sectionAdapter);

    }

    @Override
    public void onLoaderReset(Loader<List<SleepDao>> loader) {

    }

//    @Override
//    public void onLoadFinished(Loader<List<DiaperChangeDao>> listLoader, List<DiaperChangeDao> diaperChangeDaoList) {
//        Log.d(TAG, "number of diaper changes " + diaperChangeDaoList.size());
//        Log.d(TAG, "loader finished");
//
//        if (diaperChangeDaoList.size()>0) {
//            emptyView.setVisibility(View.GONE);
////            barChart.setVisibility(View.VISIBLE);
//
//            sleepListView.setVisibility(View.VISIBLE);
//        } else {
//            emptyView.setVisibility(View.VISIBLE);
//            sleepListView.setVisibility(View.GONE);
////            barChart.setVisibility(View.GONE);
//
//        }
//        sleepList = diaperChangeDaoList;
//
//        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, sleepList);
//
////        diaperChangeAdapter.update(diaperChangeDaoList);
//
//        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
//                diaperChangeAdapter, R.layout.section_header, R.id.title,
//                new DateSectionizer());
//
//        sleepListView.setAdapter(sectionAdapter);
//        //        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, diaperChangeDaoList);
////        sectionAdapter = new SimpleSectionAdapter<DiaperChangeDao>(context,
////                diaperChangeAdapter, R.layout.section_header, R.id.title,
////                new DiaperChangeSectionizer());
////        sleepListView.setAdapter(diaperChangeAdapter);
////        sectionAdapter = new SimpleSectionAdapter<DiaperChangeDao>(context,
////                diaperChangeAdapter, R.layout.section_header, R.id.title,
////                new DiaperChangeSectionizer());
////        sleepListView.setAdapter(diaperChangeAdapter);
//
////        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, sleepList);
////        sectionAdapter = new SimpleSectionAdapter<DiaperChangeDao>(context,
////                diaperChangeAdapter, R.layout.section_header, R.id.title,
////                new DiaperChangeSectionizer());
////        sleepListView.setAdapter(sectionAdapter);
////        sleepListView.setOnItemClickListener(this);
//    }
//
//
//    @Override
//    public void onLoaderReset(Loader<List<DiaperChangeDao>> listLoader) {
//
//    }


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

    private class EventListener {
        private EventListener(){
        }

        @Subscribe
        public void onSleepEventCreated(SleepLogCreated event) {
            Log.d(TAG, "onSleepEventCreated");
            getLoaderManager().restartLoader(LOADER_ID, null, SleepListFragment.this);


//            finish();
        }




    }
}
