package com.rorlig.babyapp.ui.fragment.sleep;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.mobsandgeeks.adapters.SimpleSectionAdapter;
import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.otto.SleepItemClicked;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.other.AddItemTypes;
import com.rorlig.babyapp.otto.events.stats.StatsItemEvent;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.BabyLogBaseParseObject;
import com.rorlig.babyapp.parse_dao.Sleep;
import com.rorlig.babyapp.ui.adapter.parse.MilestonesItemAdapter2;
import com.rorlig.babyapp.ui.adapter.parse.SleepAdapter;
import com.rorlig.babyapp.ui.adapter.parse.SleepAdapter2;
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
public class SleepListFragment extends BaseInjectableListFragment {

    @ForActivity
    @Inject
    Context context;



    @InjectView(R.id.add_sleep_item)
    FloatingActionButton btnAddSleep;




    private String TAG = "SleepListFragment";



//    @InjectView(R.id.swipe_refresh_layout)
//    protected SwipeRefreshLayout swipeRefreshLayout;

    public SleepListFragment() {
        super("Sleep");
    }


        @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
    }




        @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            baseParseAdapter2 = new SleepAdapter2(parseObjectList);
            ultimateRecyclerView.setAdapter(baseParseAdapter2);
            ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        currentTime.setText(today.hour + ":" + today.minute + ":" + today.second);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sleep_list_2, null);
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
    protected void setListResults(List<ParseObject> objects) {
        super.setListResults(objects);
        ultimateRecyclerView.setAdapter(baseParseAdapter2);

    }

    @OnClick(R.id.add_sleep_item)
    public void onSleepAddItemClicked(){
        scopedBus.post(new AddItemEvent(AddItemTypes.SLEEP_LOG));
    }

    @OnClick(R.id.add_item)
    public void onAddItem(){
        scopedBus.post(new AddItemEvent(AddItemTypes.SLEEP_LOG));
    }



}
