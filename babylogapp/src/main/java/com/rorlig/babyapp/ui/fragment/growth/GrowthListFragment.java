package com.rorlig.babyapp.ui.fragment.growth;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.rorlig.babyapp.otto.GrowthItemClicked;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.stats.StatsItemEvent;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.BabyLogBaseParseObject;
import com.rorlig.babyapp.parse_dao.Growth;
import com.rorlig.babyapp.ui.adapter.DateSectionizer;
import com.rorlig.babyapp.ui.adapter.parse.GrowthAdapter;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
import com.rorlig.babyapp.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//import android.widget.Button;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of growth items
 */
public class GrowthListFragment extends BaseInjectableListFragment implements  AdapterView.OnItemClickListener {

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

    @InjectView(R.id.add_growth_item)
    FloatingActionButton btnAddGrowthItem;

//    @InjectView(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout swipeRefreshLayout;

    private int LOADER_ID = 4;
    private List<ParseObject> growthList;
    private SimpleSectionAdapter<BabyLogBaseParseObject> sectionAdapter;
    private GrowthAdapter growthAdapter;

//    Typeface typeface;

    private String TAG = "GrowthListFragment";

    private EventListener eventListener = new EventListener();

    public GrowthListFragment() {
        super("Growth");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_growth, menu);
    }


    @OnClick(R.id.add_item)
    public void onDiaperChangeClicked(){
        scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
    }

    @OnClick(R.id.add_growth_item)
    public void onAddGrowthItemClicked(){
        scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
    }


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        listView.setEmptyView(emptyView);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                AppUtils.invalidateParseCache("Growth", getActivity());
//                populateFromNetwork(null);
//            }
//        });

        scopedBus.post(new FragmentCreated("Growth"));
        updateListView();
    }

    @Override
    protected void setListResults(List<ParseObject> objects) {
        super.setListResults(objects);
        growthList = objects;

        growthAdapter = new GrowthAdapter(getActivity(),
                R.layout.list_item_diaper_change, growthList);
        growthAdapter.update(growthList);

        sectionAdapter = new SimpleSectionAdapter<BabyLogBaseParseObject>(context,
                growthAdapter, R.layout.section_header_green, R.id.title,
                new DateSectionizer());
        listView.setAdapter(sectionAdapter);
        if (growthList.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            listView.setOnItemClickListener(this);
        } else {
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }

//        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_growth_list, null);
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
//            case R.id.action_add:
//                scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
//                return true;
            case R.id.action_stats:
                scopedBus.post(new StatsItemEvent());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public Loader<List<GrowthDao>> onCreateLoader(int id, Bundle args) {
//        Log.d(TAG, "create Loader");
//        return new GrowthLoader(getActivity());
//    }
//
//    @Override
//    public void onLoadFinished(Loader<List<GrowthDao>> loader, List<GrowthDao> data) {
//
//        Log.d(TAG, "number of diaper changes " + data.size());
//        Log.d(TAG, "loader finished");
//
//        if (data.size()>0) {
//            emptyView.setVisibility(View.GONE);
//            listView.setVisibility(View.VISIBLE);
//        } else {
//            emptyView.setVisibility(View.VISIBLE);
//            listView.setVisibility(View.GONE);
//        }
//        growthList = data;
//
//
//
//        growthAdapter = new GrowthAdapter(getActivity(), R.layout.list_item_diaper_change, growthList);
//
////        diaperChangeAdapter.update(diaperChangeDaoList);
//
//        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
//                growthAdapter,
//                R.layout.section_header_green,
//                R.id.title,
//                new DateSectionizer());
//
//        listView.setAdapter(sectionAdapter);
//        listView.setOnItemClickListener(this);
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<List<GrowthDao>> loader) {
//
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "iten at position " + position + " clicked");
        Growth growth = (Growth) listView.getItemAtPosition(position);
//        Log.d(TAG, "growth dao " + growthDao);
        scopedBus.post(new GrowthItemClicked(growth));
    }


    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void onGrowthItemCreated(ItemCreatedOrChanged event) {
            updateListView();
//            getLoaderManager().restartLoader(LOADER_ID, null, GrowthListFragment.this);
//            showFragment(GrowthListFragment.class, "growth_list_fragment",false);
        }



    }
}
