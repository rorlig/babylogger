package com.rorlig.babyapp.ui.fragment.growth;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.ItemDeleted;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.stats.StatsItemEvent;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.ui.adapter.parse.GrowthAdapter2;
import com.rorlig.babyapp.ui.fragment.BaseInjectableListFragment;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

//import android.widget.Button;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of growth items
 */
public class GrowthListFragment extends BaseInjectableListFragment {




    private String TAG = "GrowthListFragment";

//    private EventListener eventListener = new EventListener();

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
    public void onAddGrowthItem(){
        scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
    }

    @OnClick(R.id.add_growth_item)
    public void onAddGrowthItemClicked(){
        scopedBus.post(new AddItemEvent(AddItemTypes.GROWTH_LOG));
    }


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        scopedBus.post(new FragmentCreated("Growth"));
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseParseAdapter2 = new GrowthAdapter2(parseObjectList);
        ultimateRecyclerView.setAdapter(baseParseAdapter2);
        ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ultimateRecyclerView.enableLoadmore();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_growth_list_2, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    protected void setListResults(List<ParseObject> objects) {
        Log.d(TAG, "setListResults");
        super.setListResults(objects);
        ultimateRecyclerView.setAdapter(baseParseAdapter2);
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




}
