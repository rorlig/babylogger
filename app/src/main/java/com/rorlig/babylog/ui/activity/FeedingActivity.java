package com.rorlig.babylog.ui.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.events.feed.FeedItemCreatedEvent;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.scheduler.TypeFaceManager;
import com.rorlig.babylog.ui.fragment.feed.BottleFeedFragment;
import com.rorlig.babylog.ui.fragment.feed.FeedingListFragment;
import com.rorlig.babylog.ui.fragment.feed.NursingFeedFragment;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by rorlig on 7/18/14.
 * @author gaurav gupta
 * history of feeds changes
 */
public class FeedingActivity extends InjectableActivity {

    @Inject
    ActionBar actionBar;

    @Inject
    Gson gson;

    @Inject
    TypeFaceManager typeFaceManager;




    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String TAG  = "FeedingActivity";

    private EventListener eventListener = new EventListener();


    /*
   * Define a request code to send to Google Play services
   * This code is returned in Activity.onActivityResult
   */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private Typeface typeface;
    private NotificationManager notificationManager;
    private Intent intent;
    private PendingIntent pIntent;
    private Notification notification;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//
        String intentString = getIntent().getStringExtra("intent");


        if (intentString!=null && intentString.equals("feeding_activity_bottle")){
            showFragment(BottleFeedFragment.class, "bottle_feed", false);

        } else  {
            showFragment(FeedingListFragment.class, "feeding_list_fragment", false);

        }
//        showFragment(FeedingListFragment.class, "feeding_list_fragment", false);
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

    private void showFragment(Class<?> paramClass, String paramString, boolean addToBackStack){
        Log.d(TAG, "showFragment for " + paramClass);

        FragmentManager localFragmentManager = getSupportFragmentManager();



        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);

        if ((localFragment==null)||(!paramClass.isInstance(localFragment))){
            try {
                Log.d(TAG, "replacing fragments");

                if (addToBackStack) {

                    localFragment = (Fragment)paramClass.newInstance();
                    localFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, localFragment)
                            .addToBackStack("feeding_stack")
                            .commit();

                } else {
                    localFragment = (Fragment)paramClass.newInstance();
                    localFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, localFragment)
                            .commitAllowingStateLoss();
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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

//        @Subscribe
//        public void onDiaperLogCreatedEvent(DiaperLogCreatedEvent event) {
//            Log.d(TAG, "onDiaperLogCreatedEvent");
//            showFragment(DiaperChangeListFragment.class, "diaper_change", false);
//
////            finish();
//        }

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
                case FEED_BOTTLE:
                    showFragment(BottleFeedFragment.class, "bottle_feed", true);

                    break;
                case FEED_NURSING:
                    showFragment(NursingFeedFragment.class, "nursing_feed", true);
                    break;
            }
        }


        @Subscribe
        public void onFeedSavedEvent(FeedItemCreatedEvent event) {
//            Log.d(TAG, "onFeedSavedEvent");
//            finish();
            showFragment(FeedingListFragment.class, "feeding_list",false);
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
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
