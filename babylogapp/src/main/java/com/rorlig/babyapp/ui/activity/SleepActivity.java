package com.rorlig.babyapp.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.SleepItemClicked;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.stats.StatsItemEvent;
import com.rorlig.babyapp.scheduler.TypeFaceManager;
import com.rorlig.babyapp.ui.fragment.sleep.SleepFragment;
import com.rorlig.babyapp.ui.fragment.sleep.SleepListFragment;
import com.rorlig.babyapp.ui.fragment.sleep.SleepStatsFragment;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

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
public class SleepActivity extends InjectableActivity {

    //todo still figure out left + right toggle speeds...


    @Inject
    ActionBar actionBar;

    @Inject
    Gson gson;

    @Inject
    TypeFaceManager typeFaceManager;




    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String TAG  = "SleepActivity";

    private EventListener eventListener = new EventListener();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sleep);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary_gray));





        String intentString = getIntent().getStringExtra("intent");

        Log.d(TAG, " " +  intentString);

//        if (intentString!=null && intentString.equals("growth_activity")){
        if (savedInstanceState==null)
            showFragment(SleepListFragment.class, "sleep_fragment", false);

//        } else  {
//            showFragment(Slee.class, "growth_list_fragment", false);
//
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

            case R.id.action_licenses:
                break;
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

        /*
         * sleep item created...
         */
        @Subscribe
        public void onSleepEventCreated(ItemCreatedOrChanged event) {
            Log.d(TAG, "onSleepEventCreated");
//            showFragment(SleepListFragment.class, "sleep_list_fragment", false);
            getSupportFragmentManager().popBackStackImmediate();
            closeSoftKeyBoard();
//            finish();
        }

        @Subscribe
        public void onAddItemEvent(AddItemEvent event){
            showFragment(SleepFragment.class, "sleep_fragment", true);
        }
        @Subscribe
        public void onStatsItemEvent(StatsItemEvent event) {
            showFragment(SleepStatsFragment.class, "stats_fragment", true);
        }

        @Subscribe
        public void onSleepItemClicked(SleepItemClicked event) {
            Log.d(TAG, "onSleepItemClicked");
            SleepFragment fragment = new SleepFragment();
            Bundle args = new Bundle();
            args.putString("sleep_id", event.getSleepDao().getObjectId());
            args.putString("uuid", event.getSleepDao().getUuidString());
            fragment.setArguments(args);

            Log.d(TAG, "adding to back stack ");

//            Fragment localFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack("main_screen_stack")
                    .commit();

//            showFragment(DiaperChangeFragment.class, "diaper_change", true);
        }

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
