package com.rorlig.babylog.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.events.diaper.DiaperLogCreatedEvent;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.stats.StatsItemEvent;
import com.rorlig.babylog.scheduler.TypeFaceManager;
import com.rorlig.babylog.ui.fragment.diaper.DiaperChangeFragment;
import com.rorlig.babylog.ui.fragment.diaper.DiaperChangeListFragment;
import com.rorlig.babylog.ui.fragment.diaper.DiaperStatsFragment;
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
public class DiaperChangeActivity extends InjectableActivity {

    //todo still figure out left + right toggle speeds...


    @Inject
    ActionBar actionBar;

    @Inject
    Gson gson;

    @Inject
    TypeFaceManager typeFaceManager;




    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String TAG  = "DiaperChangeActivity";

    private EventListener eventListener = new EventListener();


    /*
   * Define a request code to send to Google Play services
   * This code is returned in Activity.onActivityResult
   */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_change_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String intentString = getIntent().getStringExtra("intent");

        Log.d(TAG, " " +  intentString);

//        if (intentString!=null && intentString.equals("diaper_change")){
//            showFragment(DiaperChangeFragment.class, "diaper_change_fragment", false);

//        } else  {
            showFragment(DiaperChangeListFragment.class, "diaper_change_list_fragment", false);

//        }
//        } else {
//
//        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the option menu items..
//        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }




//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Pass the event to ActionBarDrawerToggle, if it returns
//        // true, then it has handled the app icon touch event
//
//        switch (item.getItemId()){
//            case R.id.action_licenses:
//                break;
//
//
//        }
//        // Handle your other action bar items...
//
//        return super.onOptionsItemSelected(item);
//    }

    /*
     * Class to swap fragments in and out
     */

//    private void showFragment(Class<?> paramClass, String paramString, boolean addToBackStack){
//        Log.d(TAG, "showFragment for " + paramClass);
//
//        FragmentManager localFragmentManager = getSupportFragmentManager();
//
//
//
//        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);
//
//        if ((localFragment==null)||(!paramClass.isInstance(localFragment))){
//            try {
//                Log.d(TAG, "replacing fragments");
//
//                if (addToBackStack) {
//
//                    localFragment = (Fragment)paramClass.newInstance();
//                    localFragmentManager.beginTransaction()
//                            .add(R.id.fragment_container, localFragment)
//                            .addToBackStack("diaper_stack")
//                            .commit();
//
//                } else {
//                    localFragment = (Fragment)paramClass.newInstance();
//                    localFragmentManager.beginTransaction()
//                            .add(R.id.fragment_container, localFragment)
//                            .commit();
//                }
//
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }


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
        public void onDiaperLogCreatedEvent(DiaperLogCreatedEvent event) {
            Log.d(TAG, "onDiaperLogCreatedEvent");
            getSupportFragmentManager().popBackStackImmediate();
//            showFragment(DiaperChangeListFragment.class, "diaper_change_list", false);

//            getFragmentManager().popBackStackImmediate();
//            finish();
        }

        @Subscribe
        public void onStatsItemEvent(StatsItemEvent statsItemEvent) {
                    showFragment(DiaperStatsFragment.class, "diaper_stats_fragment", true);
        }

//        @Subscribe
//        public void onAddDiaperChangeEvent(AddDiaperChangeEvent diaperChangeEvent){
////            showFragment(DiaperChangeFragment.class, "diaper_change");
//            Log.d(TAG, "onAddDiaperChangeEvent");
//            showFragment(DiaperChangeFragment.class, "diaper_change", false);
////            startActivity(DiaperChangeActivity.class,"diaper_change");
//        }


        @Subscribe
        public void onItemAddedEvent(AddItemEvent addItemEvent){
            Log.d(TAG, "onItemAddedEvent");
            switch (addItemEvent.getItemType()) {
                case DIAPER_CHANGE:
                    showFragment(DiaperChangeFragment.class, "diaper_change", true);
                    break;
            }
        }




    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving event list");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
//                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
