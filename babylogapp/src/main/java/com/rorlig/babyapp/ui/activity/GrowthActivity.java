package com.rorlig.babyapp.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.GrowthItemClicked;
import com.rorlig.babyapp.otto.ItemDeleted;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.stats.StatsItemEvent;
import com.rorlig.babyapp.scheduler.TypeFaceManager;
import com.rorlig.babyapp.ui.fragment.growth.GrowthFragment;
import com.rorlig.babyapp.ui.fragment.growth.GrowthListFragment;
import com.rorlig.babyapp.ui.fragment.growth.GrowthStatsFragment;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.InjectView;

//import com.rorlig.babylog.common.AppConstants;
//import com.rorlig.babylog.otto.events.filter.DistanceFilterChanged;
//import com.rorlig.babylog.otto.events.filter.FilterChangedEvent;
//import com.rorlig.babylog.otto.events.filter.LocationChangedEvent;
//import com.rorlig.babylog.otto.events.ui.EventSelectedEvent;
//import com.rorlig.babylog.otto.events.ui.MenuItemSelectedEvent;
//import com.rorlig.babylog.otto.events.ui.TimeFilterChanged;
//import com.rorlig.babylog.ui.fragment.ConnectionsFragment;
//import com.rorlig.babylog.ui.fragment.LoggingFragment;
//import com.rorlig.babylog.ui.fragment.MessagesFragment;
//import com.rorlig.babylog.ui.fragment.MyEventsFragment;

/**
 * Created by admin on 12/15/13.
 */
public class GrowthActivity extends InjectableActivity {



    @Inject
    ActionBar actionBar;

    @Inject
    Gson gson;

    @Inject
    TypeFaceManager typeFaceManager;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.title)
    TextView titleTextView;




    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String TAG  = "GrowthActivity";

    private EventListener eventListener = new EventListener();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setBackgroundColor(getResources().getColor(R.color.primary_green));
//        titleTextView.setText(getTitle());





        String intentString = getIntent().getStringExtra("intent");

        Log.d(TAG, " " +  intentString);

//        if (intentString!=null && intentString.equals("growth_activity")){
//            showFragment(GrowthFragment.class, "growth_fragment", false);
//
//        } else  {
        if (savedInstanceState==null)
            showFragment(GrowthListFragment.class, "growth_list_fragment", false);

//        }

//        showFragment(GrowthListFragment.class, "growth_fragment", false);
//        } else {
//
//        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the option menu items..
//        getMenuInflater().inflate(R.menu.menu_add_item, menu);

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;



        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onStart(){
        super.onStart();
        scopedBus.register(eventListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        scopedBus.unregister(eventListener);
    }

    private void startActivity(Class<?> paramClass,String paramString){
        Intent intent = new Intent(this, paramClass);
        startActivity(intent);
    }

    private class EventListener {
        private EventListener(){
        }

        @Subscribe
        public void onItemAddedEvent(AddItemEvent addItemEvent){
            Log.d(TAG, "onItemAddedEvent");
            switch (addItemEvent.getItemType()) {
                case GROWTH_LOG:
                    showFragment(GrowthFragment.class, "growth_fragment", true);
                    break;
            }
        }

        @Subscribe
        public void onGrowthItemCreated(ItemCreatedOrChanged event) {
            getSupportFragmentManager().popBackStack();
            closeSoftKeyBoard();
//            showFragment(GrowthListFragment.class, "growth_list_fragment",false);
        }

        @Subscribe
        public void onStatsItemEvent(StatsItemEvent statsItemEvent) {
            showFragment(GrowthStatsFragment.class, "growth_stats_fragment", true);
        }

        @Subscribe
        public void onGrowthItemClicked(GrowthItemClicked event) {
            Log.d(TAG, "onGrowthItemClicked");
            GrowthFragment fragment = new GrowthFragment();
            Bundle args = new Bundle();
            args.putString("id", event.getGrowthDao().getObjectId());
            args.putString("uuid" , event.getGrowthDao().getUuidString());
            args.putInt("position", event.getPosition());

            fragment.setArguments(args);
            Log.d(TAG, "adding to back stack ");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack("main_screen_stack")
                    .commit();

        }

        @Subscribe
        public void onItemDeletedEvent(ItemDeleted event) {
            Log.d(TAG, "onItemDeletedEvent");
            closeSoftKeyBoard();
            getSupportFragmentManager().popBackStackImmediate();
        }
//
//        @Subscribe
//        public void updateActionBar(UpdateActionBarEvent event){
//            Log.d(TAG, "updating action bar");
//            profileImageIcon.setImageDrawable(event.getDrawable());
//        }



    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving event list");
    }



}
