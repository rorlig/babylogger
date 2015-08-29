package com.rorlig.babyapp.ui.fragment.sleep;

import android.content.Context;
import android.os.Bundle;
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
import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.dao.BaseDao;
import com.rorlig.babyapp.dao.SleepDao;
import com.rorlig.babyapp.db.BabyLoggerORMUtils;
import com.rorlig.babyapp.otto.SleepItemClicked;
import com.rorlig.babyapp.otto.SleepLogCreated;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.stats.StatsItemEvent;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.Sleep;
import com.rorlig.babyapp.ui.adapter.parse.SleepAdapter;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

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
public class SleepListFragment extends BaseInjectableListFragment
        implements AdapterView.OnItemClickListener
//        , LoaderManager.LoaderCallbacks<List<SleepDao>>
    {

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.sleep_list)
    ListView sleepListView;

    @InjectView(R.id.empty_view)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;

    @InjectView(R.id.add_sleep_item)
    FloatingActionButton btnAddSleep;


    private BabyLoggerORMUtils babyORMLiteUtils;
    private List<ParseObject> sleepList;
    private SleepAdapter sleepAdapter;
    private SimpleSectionAdapter<BaseDao> sectionAdapter;
    private int LOADER_ID=2;


    private String TAG = "SleepListFragment";

    private EventListener eventListener = new EventListener();

    PreparedQuery<SleepDao> queryBuilder;

    public SleepListFragment() {
        super("Sleep");
    }


        @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        scopedBus.post(new FragmentCreated("Diaper Change List"));

        updateListView();







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
        Log.d(TAG, "item clicked at position " + position + " id " + id);
        Sleep sleep = (Sleep) sleepListView.getItemAtPosition(position);
        Log.d(TAG, "diaperchange dao " + sleep);
        scopedBus.post(new SleepItemClicked(sleep));
    }

//    @Override
//    public Loader<List<SleepDao>> onCreateLoader(int i, Bundle bundle) {
//        Log.d(TAG, "create Loader");
//
//        return new SleepLoader(getActivity());
//
//    }

//    @Override
//    public void onLoadFinished(Loader<List<SleepDao>> loader, List<SleepDao> data) {
//
//        Log.d(TAG, "loader finished");
//
//        if (data.size()>0) {
//            emptyView.setVisibility(View.GONE);
////            barChart.setVisibility(View.VISIBLE);
//
//            sleepListView.setVisibility(View.VISIBLE);
//        } else {
//            emptyView.setVisibility(View.VISIBLE);
//            sleepListView.setVisibility(View.GONE);
//
//        }
//
//        sleepList = data;
//
//        sleepAdapter = new SleepAdapter(getActivity(), R.layout.list_item_sleep, sleepList);
//
////        diaperChangeAdapter.update(diaperChangeDaoList);
//
//        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
//                sleepAdapter, R.layout.section_header_gray, R.id.title,
//                new DateSectionizer());
//
//        sleepListView.setAdapter(sectionAdapter);
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<List<SleepDao>> loader) {
//
//    }


    @Override
    protected void setListResults(List<ParseObject> objects) {
        sleepList = objects;

        sleepAdapter = new SleepAdapter(getActivity(),
                R.layout.list_item_diaper_change, sleepList);
        sleepAdapter.update(sleepList);
        sleepListView.setAdapter(sleepAdapter);
        if (sleepList.size() > 0) {
            sleepListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            sleepListView.setOnItemClickListener(this);
        } else {
            sleepListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }
    }

    @OnClick(R.id.add_sleep_item)
    public void onSleepAddItemClicked(){
        scopedBus.post(new AddItemEvent(AddItemTypes.SLEEP_LOG));
    }

    @OnClick(R.id.add_item)
    public void onAddItem(){
        scopedBus.post(new AddItemEvent(AddItemTypes.SLEEP_LOG));
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

    private class EventListener {
        private EventListener(){
        }

        @Subscribe
        public void onSleepEventCreated(SleepLogCreated event) {
            Log.d(TAG, "onSleepEventCreated");
            updateListView();
//            getLoaderManager().restartLoader(LOADER_ID, null, SleepListFragment.this);
        }




    }
}
