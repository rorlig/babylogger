package com.rorlig.babyapp.ui.fragment.milestones;

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

import com.gc.materialdesign.views.Button;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.otto.MilestoneItemClicked;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.Milestones;
import com.rorlig.babyapp.ui.adapter.parse.MilestonesItemAdapter;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
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
public class MilestoneListFragment extends BaseInjectableListFragment
        implements  AdapterView.OnItemClickListener {

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
    private MilestonesItemAdapter milestonesAdapter;

    private List<ParseObject> milestoneData;

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


        listView.setEmptyView(emptyView);

        scopedBus.post(new FragmentCreated("Milestones"));

        updateListView();

    }

    @Override
    protected void setListResults(List<ParseObject> objects) {
        super.setListResults(objects);
        if (objects.size()>0) {
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        milestoneData = objects;
        Log.d(TAG, "number of milestones changes " + milestoneData.size());


        milestonesAdapter = new MilestonesItemAdapter(getActivity(), R.layout.list_item_diaper_change, milestoneData);


        listView.setAdapter(milestonesAdapter);

//        swipeRefreshLayout.setRefreshing(false);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "itemClicked " + position);
        MilestonesCompletedFragment milestonesCompletedFragment = MilestonesCompletedFragment.newInstance(position);

//        Log.d(TAG, "iten at position " + position + " clicked");
        com.rorlig.babyapp.parse_dao.Milestones milestonesDao = (Milestones) milestoneData.get(position);
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
    public void onStart() {


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



    @OnClick(R.id.add_milestone_item)
    public void onAddMilestoneItem(){
        scopedBus.post(new AddItemEvent(AddItemTypes.MILESTONE));
    }


    private class EventListener {
        public EventListener() {

        }


        @Subscribe
        public void onMilestoneItemCreated(ItemCreatedOrChanged event) {
            updateListView();
//            getLoaderManager().restartLoader(LOADER_ID, null, MilestoneListFragment.this);
        }



    }
}
