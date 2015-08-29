package com.rorlig.babyapp.ui.fragment.diaper;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
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

import com.gc.materialdesign.views.Button;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.j256.ormlite.stmt.PreparedQuery;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.dao.BaseDao;
import com.rorlig.babyapp.dao.DiaperChangeDao;
import com.rorlig.babyapp.db.BabyLoggerORMUtils;
import com.rorlig.babyapp.otto.DiaperChangeItemClickedEvent;
import com.rorlig.babyapp.otto.events.diaper.DiaperLogCreatedEvent;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.stats.StatsItemEvent;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.DiaperChange;
import com.rorlig.babyapp.ui.adapter.parse.DiaperChangeAdapter;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//import android.widget.Button;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of diaper changes
 */
public class DiaperChangeListFragment extends InjectableFragment implements AdapterView.OnItemClickListener {

    @ForActivity
    @Inject
    Context context;


    @InjectView(R.id.diaperchangelist)
    ListView diaperChangeListView;

    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;




    @InjectView(R.id.add_item)
    Button btnDiaperChange;

    @InjectView(R.id.add_diaper_item)
    FloatingActionButton btnAddDiaperChange;


    private BabyLoggerORMUtils babyORMLiteUtils;
    private List<ParseObject> diaperChangeList;
    private DiaperChangeAdapter diaperChangeAdapter;
    private SimpleSectionAdapter<BaseDao> sectionAdapter;
    private int LOADER_ID=2;
    private List<String[]> diaperChangeDaoList;


    Typeface typeface;

    private String TAG = "DiaperChangeListFragment";

    private EventListener eventListener = new EventListener();

    PreparedQuery<DiaperChangeDao> queryBuilder;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

//        diaperChangeListView.setEmptyView(emptyView);

//        btnDiaperChange.setTypeface(typeface);

//        errorText.setTypeface(typeface);

        scopedBus.post(new FragmentCreated("Diaper Change List"));

//        if (diaperChangeList!=null && di)

        updateListView();


//        mainAdapter = new ParseQueryAdapter<ParseObject>(this, "Todo");


//        babyORMLiteUtils = new BabyLoggerORMUtils(getActivity());
//        try {		mainAdapter = new ParseQueryAdapter<ParseObject>(this, "Todo");

//            queryBuilder = babyORMLiteUtils.getDiaperChange().queryBuilder().orderBy("date", false).prepare();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        diaperChangeList = new ArrayList<DiaperChange>();
//        Log.d(TAG, "number of diaper changes " + diaperChangeList.size());

//        diaperChangeListView.setOnItemClickListener(this);
//
//        getLoaderManager().initLoader(LOADER_ID, null, this);
//
//        try {
//            diaperChangeDaoList =  babyORMLiteUtils.getDiaperChangeByWeekofMonth();
//            setData(diaperChangeDaoList, DiaperChangeStatsType.WEEKLY);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }




    }

    private void updateListView() {
       populateLocalStore();

    }

    private void populateLocalStore() {
        Log.d(TAG, "populateLocalStore");

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Diaper");
        query.fromLocalDatastore();
        query.orderByDescending("createdAt");
        query.findInBackground(
                new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        Log.d(TAG, "got list from the cache");
                        if (e == null) {
                            Log.d(TAG, "number of items " + objects.size());
                            if (objects.size() == 0) {
                                populateFromNetwork(objects);
                            } else {
                                setListResults(objects);

                            }
                        } else {
                            Log.d(TAG, "exception " + e);
                        }
                    }
                }


        );

    }


    private void populateFromNetwork(final List<ParseObject> data) {
        Log.d(TAG, "populateFromNetwork");
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Diaper");
        query.orderByDescending("createdAt");
        query.findInBackground(
                new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        Log.d(TAG, "got list from the network");
                        if (e == null) {
                            Log.d(TAG, "number of items " + objects.size());
//                            if(objects.size()==0) {
//                                populateFromNetwork();
//                            } else {

                            ParseObject.unpinAllInBackground("Diapers", data, new DeleteCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Log.d(TAG, "deleted diapers pin " + e);
                                }

                            });
                            ParseObject.pinAllInBackground("Diapers", objects);
                            setListResults(objects);
                        } else {
                            Log.d(TAG, "exception " + e);
                        }
                    }
                }
        );
    }

    private void setListResults(List<ParseObject> objects) {
        diaperChangeList = objects;

        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(),
                R.layout.list_item_diaper_change, diaperChangeList);
        diaperChangeAdapter.update(diaperChangeList);
        diaperChangeListView.setAdapter(diaperChangeAdapter);
        if (diaperChangeList.size() > 0) {
            diaperChangeListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            diaperChangeListView.setOnItemClickListener(this);
        } else {
            diaperChangeListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }
    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diaperchage_list, null);
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
//        getLoaderManager().restartLoader(LOADER_ID, null, this);
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
        Log.d(TAG, "item clicked at position " + position + " id " + id + " size " + diaperChangeListView.getAdapter().getCount());
        DiaperChange diaperChange = (DiaperChange) diaperChangeListView.getAdapter().getItem(position);
        Log.d(TAG, "diaperchange dao " + diaperChange);
        scopedBus.post(new DiaperChangeItemClickedEvent(diaperChange));
    }

//    @Override
//    public Loader<List<DiaperChangeDao>> onCreateLoader(int i, Bundle bundle) {
//        Log.d(TAG, "create Loader");
//
//        return new DiaperLoader(getActivity());
//
//    }
//
//    @Override
//    public void onLoadFinished(Loader<List<DiaperChangeDao>> listLoader, List<DiaperChangeDao> diaperChangeDaoList) {
//        Log.d(TAG, "number of diaper changes " + diaperChangeDaoList.size());
//        Log.d(TAG, "loader finished");
//
//        if (diaperChangeDaoList.size()>0) {
//            emptyView.setVisibility(View.GONE);
////            barChart.setVisibility(View.VISIBLE);
//
//            diaperChangeListView.setVisibility(View.VISIBLE);
//        } else {
//            emptyView.setVisibility(View.VISIBLE);
//            diaperChangeListView.setVisibility(View.GONE);
////            barChart.setVisibility(View.GONE);
//
//        }
//        diaperChangeList = diaperChangeDaoList;
//
//        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, diaperChangeList);
//
////        diaperChangeAdapter.update(diaperChangeDaoList);
//
//        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
//                diaperChangeAdapter, R.layout.section_header, R.id.title,
//                new DateSectionizer());
//
//        diaperChangeListView.setAdapter(sectionAdapter);
//        diaperChangeListView(this);
//
////        diaperChangeListView.setOnLongClickListener(new OnLongClickListener() {
////
////            @Override
////            public boolean onLongClick(View v) {
////                if (mActionMode != null) {
////                    return false;
////                }
////                mActionMode = getActivity().startActionMode(mActionModeCallback);
////                v.setSelected(true);
////                return true;
////
////            }
////        });
////        registerForContextMenu(diaperChangeListView);
//        //        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, diaperChangeDaoList);
////        sectionAdapter = new SimpleSectionAdapter<DiaperChangeDao>(context,
////                diaperChangeAdapter, R.layout.section_header, R.id.title,
////                new DiaperChangeSectionizer());
////        diaperChangeListView.setAdapter(diaperChangeAdapter);
////        sectionAdapter = new SimpleSectionAdapter<DiaperChangeDao>(context,
////                diaperChangeAdapter, R.layout.section_header, R.id.title,
////                new DiaperChangeSectionizer());
////        diaperChangeListView.setAdapter(diaperChangeAdapter);
//
////        diaperChangeAdapter = new DiaperChangeAdapter(getActivity(), R.layout.list_item_diaper_change, diaperChangeList);
////        sectionAdapter = new SimpleSectionAdapter<DiaperChangeDao>(context,
////                diaperChangeAdapter, R.layout.section_header, R.id.title,
////                new DiaperChangeSectionizer());
////        diaperChangeListView.setAdapter(sectionAdapter);
//        diaperChangeListView.(this);
//    }
//
//
//    @Override
//    public void onLoaderReset(Loader<List<DiaperChangeDao>> listLoader) {
//
//    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        Log.d(TAG, "onCreateContextMenu");
//        menu.add(0, v.getId(), 0, "Action 1");
//
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "onContextItemSelected");

        return super.onContextItemSelected(item);
    }

    // -- Button clicks ...
    @OnClick(R.id.add_item)
    public void onDiaperChangeClicked(){
//        scopedBus.post(new AddDiaperChangeEvent());
        addDiaperChange();
    }


    @OnClick(R.id.add_diaper_item)
    public void onDiaperChangeBtnClicked(){
//        scopedBus.post(new AddDiaperChangeEvent());
        addDiaperChange();
    }

    private void addDiaperChange(){
        scopedBus.post(new AddItemEvent(AddItemTypes.DIAPER_CHANGE));
    }

    // -- chart data
    private void setData(List<String[]> diaperChangeDaoList,
                         DiaperChangeStatsType diaperChangeStatsType) {
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        int i = 0;
        for (String[] diaperChangeResult: diaperChangeDaoList) {
            Log.d(TAG, "iterating the result set");
            Log.d(TAG, " i " + i + "  date " + diaperChangeResult[0] + " count " + diaperChangeResult[1]);

            Integer value = Integer.parseInt(diaperChangeResult[1]);
            String xValue = diaperChangeResult[0];
            switch (diaperChangeStatsType) {
                case WEEKLY:
                    break;
                case MONTHLY:
                    xValue = getDateRangeForWeek(Integer.parseInt(diaperChangeResult[0]));
                    break;

            }
            xVals.add(xValue);

            yVals.add(new BarEntry(value, i));
            i++;
        }

        BarDataSet set1 = new BarDataSet(yVals, diaperChangeStatsType.toString());
        set1.setBarSpacePercent(35f);
        set1.setColor(getResources().getColor(R.color.primary_purple));
        set1.setHighLightColor(getResources().getColor(R.color.primary_dark_purple));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
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

    // class to handle event clicks
    private class EventListener {
        private EventListener(){
        }

        @Subscribe
        public void onDiaperLogCreatedEvent(DiaperLogCreatedEvent event) {
            Log.d(TAG, "onDiaperLogCreatedEvent");
            updateListView();
        }




    }
}
