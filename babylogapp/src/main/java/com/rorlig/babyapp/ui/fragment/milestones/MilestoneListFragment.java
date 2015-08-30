package com.rorlig.babyapp.ui.fragment.milestones;

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

import com.gc.materialdesign.views.Button;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.j256.ormlite.dao.Dao;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.dao.BaseDao;
import com.rorlig.babyapp.dao.GrowthDao;
import com.rorlig.babyapp.dao.MilestonesDao;
import com.rorlig.babyapp.db.BabyLoggerORMLiteHelper;
import com.rorlig.babyapp.otto.MilestoneItemClicked;
import com.rorlig.babyapp.otto.MilestoneItemCreated;
import com.rorlig.babyapp.otto.events.diaper.DiaperLogCreatedEvent;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.ui.adapter.MilestonesItemAdapter;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//import android.widget.Button;

/**
 * @author gaurav gupta
 * history of growth items
 */
public class MilestoneListFragment extends BaseInjectableListFragment implements LoaderManager.LoaderCallbacks<List<MilestonesDao>>, AdapterView.OnItemClickListener {

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.itemList)
    ListView listView;

    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;



    @InjectView(R.id.add_item)
    Button btnAddItem;

    @InjectView(R.id.add_milestone_item)
    FloatingActionButton btnAddMilestoneItem;

    private int LOADER_ID = 4;
    private List<GrowthDao> growthList;
    private SimpleSectionAdapter<BaseDao> sectionAdapter;
    private String[] itemNames;
    private MilestonesItemAdapter milestonesAdapter;

    private List<MilestonesDao> milestoneData;

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;
    private Dao<MilestonesDao, Integer> milestoneDaoHelper;

    public MilestoneListFragment() {
        super("Milestone");
    }


    @OnClick(R.id.add_item)
    public void onMilestoneAddItemClicked(){
        scopedBus.post(new AddItemEvent(AddItemTypes.MILESTONE));
    }


    private String TAG = "MilestoneListFragment";

    private EventListener eventListener = new EventListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        itemNames = getResources().getStringArray(R.array.milestones);

        listView.setEmptyView(emptyView);

        scopedBus.post(new FragmentCreated("Milestones"));


//        listView.setOnItemClickListener(this);


        getLoaderManager().initLoader(LOADER_ID, null, this);


//        getActivity().getActionBar().setTitle("Diaper Change List");


    }

    @Override
    protected void setListResults(List<ParseObject> objects) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "itemClicked " + position);
        MilestonesCompletedFragment milestonesCompletedFragment = MilestonesCompletedFragment.newInstance(position);

//        Log.d(TAG, "iten at position " + position + " clicked");
        MilestonesDao milestonesDao = (MilestonesDao) milestoneData.get(position);
//        Log.d(TAG, "growth dao " + growthDao);
        scopedBus.post(new MilestoneItemClicked(milestonesDao));
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_milestone_list, null);
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
        scopedBus.register(eventListener);
//        getLoaderManager().restartLoader(LOADER_ID, null, this);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<MilestonesDao>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "create Loader");
        return new MilestonesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<MilestonesDao>> loader, List<MilestonesDao> data) {

        Log.d(TAG, "number of milestones " + data.size());
        Log.d(TAG, "loader finished");

        if (data.size()>0) {
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        milestoneData = data;
        Log.d(TAG, "number of milestones changes " + milestoneData.size());


        milestonesAdapter = new MilestonesItemAdapter(getActivity(), R.layout.list_item_diaper_change, milestoneData);


        listView.setAdapter(milestonesAdapter);

//        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
//                milestonesAdapter,
//                R.layout.section_header_green,
//                R.id.title,
//                new DateSectionizer());
//
//        listView.setAdapter(sectionAdapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onLoaderReset(Loader<List<MilestonesDao>> loader) {

    }

    @OnClick(R.id.add_milestone_item)
    public void onAddMilestoneItem(){
        scopedBus.post(new AddItemEvent(AddItemTypes.MILESTONE));
    }


    private class EventListener {
        public EventListener() {

        }


        @Subscribe
        public void onMilestoneItemCreated(MilestoneItemCreated event) {
            getLoaderManager().restartLoader(LOADER_ID, null, MilestoneListFragment.this);
        }

        @Subscribe
        public void onDiaperLogCreatedEvent(DiaperLogCreatedEvent event) {
            Log.d(TAG, "onDiaperLogCreatedEvent");

        }
//        @Subscribe
//        public void onMileStoneSaved(MilestoneSaveEvent event){
//            Log.d(TAG, "milestonesaved event " + event);
//            Calendar c = Calendar.getInstance();
//            c.set(Calendar.YEAR, event.getYear());
//            c.set(Calendar.MONTH, event.getMonth()-1);
//            c.set(Calendar.DAY_OF_MONTH, event.getDay());
////            Date date = new Date(event.getYear(), event.getMonth()-1, event.getDay());
//            setCompleted(event.getPosition(), c.getTime(), true);
//        }
//
//
//
//        @Subscribe
//        public void onMileStoneCancel(MilestoneCancelEvent event){
//            Log.d(TAG, "onMileStoneCancel");
//        }
//
//        @Subscribe
//        public void onMileStoneReset(MilestoneResetEvent event) {
//            Log.d(TAG, "onMileStoneReset" + event.getPosition());
//            setCompleted(event.getPosition(), false);
//
//        }
//
//
//        private void setCompleted(int position, boolean value) {
//            MilestonesDao milestoneItem = milestoneData.get(position);
////            milestoneItem.setCompleted(value);
//
//
//            try {
//                milestoneDaoHelper = babyLoggerORMLiteHelper.getMilestonesDao();
//                milestoneDaoHelper.update(milestoneItem);
//                milestonesAdapter.notifyDataSetChanged();
//
//            } catch (SQLException e) {
//                Log.d(TAG, "update item");
//                e.printStackTrace();
//            }
//        }
//
//        private void setCompleted(int position, Date date, boolean value) {
//
//            Log.d(TAG, "date " + date);
//            MilestonesDao milestoneItem = milestoneData.get(position);
////            milestoneItem.setCompleted(value);
////            milestoneItem.setCompletionDate(date);
//
//
//            try {
//                milestoneDaoHelper = babyLoggerORMLiteHelper.getMilestonesDao();
//                milestoneDaoHelper.update(milestoneItem);
//                milestonesAdapter.notifyDataSetChanged();
//
//            } catch (SQLException e) {
//                Log.d(TAG, "update item");
//                e.printStackTrace();
//            }
//        }

    }
}
