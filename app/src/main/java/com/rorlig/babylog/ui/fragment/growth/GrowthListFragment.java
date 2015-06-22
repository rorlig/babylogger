package com.rorlig.babylog.ui.fragment.growth;

import android.content.Context;
import android.graphics.Typeface;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.BaseDao;
import com.rorlig.babylog.dao.GrowthDao;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.other.AddItemTypes;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.adapter.DiaperChangeSectionizer;
import com.rorlig.babylog.ui.adapter.GrowthAdapter;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

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
public class GrowthListFragment extends InjectableFragment implements LoaderManager.LoaderCallbacks<List<GrowthDao>>{

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

    @InjectView(R.id.itemList)
    ListView listView;

    @InjectView(R.id.emptyView)
    RelativeLayout emptyView;

    @InjectView(R.id.errorText)
    TextView errorText;



    @InjectView(R.id.add_item)
    Button btnAddItem;
    private int LOADER_ID = 4;
    private List<GrowthDao> growthList;
    private SimpleSectionAdapter<BaseDao> sectionAdapter;
    private GrowthAdapter growthAdapter;

    @OnClick(R.id.add_item)
    public void onDiaperChangeClicked(){
//        scopedBus.post(new AddDiaperChangeEvent());

        scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
    }


    Typeface typeface;

    private String TAG = "GrowthListFragment";

    private EventListener eventListener = new EventListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_item, menu);
    }

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        typeface=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/proximanova_light.ttf");

        listView.setEmptyView(emptyView);
//
//        btnAddItem.setTypeface(typeface);
//
//        errorText.setTypeface(typeface);

        scopedBus.post(new FragmentCreated("Growth"));

        getLoaderManager().initLoader(LOADER_ID, null, this);


//        getActivity().getActionBar().setTitle("Diaper Change List");


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
        getLoaderManager().restartLoader(LOADER_ID, null, this);

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
            case R.id.action_add:
                scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<GrowthDao>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "create Loader");
        return new GrowthLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<GrowthDao>> loader, List<GrowthDao> data) {

        Log.d(TAG, "number of diaper changes " + data.size());
        Log.d(TAG, "loader finished");

        if (data.size()>0) {
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        growthList = data;

        growthAdapter = new GrowthAdapter(getActivity(), R.layout.list_item_diaper_change, growthList);

//        diaperChangeAdapter.update(diaperChangeDaoList);

        sectionAdapter = new SimpleSectionAdapter<BaseDao>(context,
                growthAdapter,
                R.layout.section_header,
                R.id.title,
                new DiaperChangeSectionizer());

        listView.setAdapter(sectionAdapter);

    }

    @Override
    public void onLoaderReset(Loader<List<GrowthDao>> loader) {

    }


    private class EventListener {
        public EventListener() {

        }


    }
}
