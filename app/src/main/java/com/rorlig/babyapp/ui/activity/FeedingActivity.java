package com.rorlig.babyapp.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.model.feed.FeedType;
import com.rorlig.babyapp.otto.FeedItemClickedEvent;
import com.rorlig.babyapp.otto.events.feed.FeedItemCreatedEvent;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.scheduler.TypeFaceManager;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.rorlig.babyapp.ui.fragment.feed.BottleFeedFragment;
import com.rorlig.babyapp.ui.fragment.feed.FeedingListFragment;
import com.rorlig.babyapp.ui.fragment.feed.NursingFeedFragment;
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




    private String TAG  = "FeedingActivity";

    private EventListener eventListener = new EventListener();
    private FragmentManager.OnBackStackChangedListener listener = getListener();


    /*
   * Define a request code to send to Google Play services
   * This code is returned in Activity.onActivityResult
   */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feeding_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary_blue));


//        getSupportFragmentManager().addOnBackStackChangedListener(listener);




//
//        String intentString = getIntent().getStringExtra("intent");
//        if (!getIntent().getBooleanExtra("fromNotification", false)) {

        if (savedInstanceState==null)
            showFragment(FeedingListFragment.class, "feeding_list_fragment", false);
//        } else {
//            Fragment localFragment =  new FeedingListFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, localFragment)
//                    .commit();
//            Fragment nursingFragment = new NursingFeedFragment();
//            Bundle args = new Bundle();
//
//            Log.d(TAG, "fromNotification " + getIntent().getBooleanExtra("fromNotification", false));
//
//            Log.d(TAG, "hourLTextView " + getIntent().getStringExtra("hourLTextView"));
//
//            Log.d(TAG, "minuteLTextView " + getIntent().getStringExtra("minuteLTextView"));
//
//            Log.d(TAG, "secondLTextView " + getIntent().getStringExtra("secondLTextView"));
//
//            Log.d(TAG, "hourRTextView " + getIntent().getStringExtra("hourRTextView"));
//
//            Log.d(TAG, "minuteRTextView " + getIntent().getStringExtra("minuteRTextView"));
//
//            Log.d(TAG, "secondRTextView " + getIntent().getStringExtra("secondRTextView"));
//
//
//            Log.d(TAG, "elapsedTimeL " + getIntent().getLongExtra("elapsedTimeL", -1L));
//
//
//            Log.d(TAG, "elapsedTimeR " + getIntent().getLongExtra("elapsedTimeR", -1L));
//
//            Log.d(TAG, "leftStarted " + getIntent().getBooleanExtra("leftStarted", false));
//
//            Log.d(TAG, "rightStarted " + getIntent().getBooleanExtra("rightStarted", false));
//
//
//
//            args.putBoolean("fromNotification", getIntent().getBooleanExtra("fromNotification", false));
//
//
//            args.putString("hourLTextView", getIntent().getStringExtra("hourLTextView"));
//            args.putString("minuteLTextView", getIntent().getStringExtra("minuteLTextView"));
//            args.putString("secondLTextView", getIntent().getStringExtra("secondLTextView"));
//
//            args.putString("hourRTextView", getIntent().getStringExtra("hourRTextView"));
//            args.putString("minuteRTextView", getIntent().getStringExtra("minuteRTextView"));
//            args.putString("secondRTextView", getIntent().getStringExtra("secondRTextView"));
//            args.putLong("elapsedTimeL", getIntent().getLongExtra("elapsedTimeL", -1L));
//            args.putLong("elapsedTimeR", getIntent().getLongExtra("elapsedTimeR", -1L));
//            args.putBoolean("leftStarted", getIntent().getBooleanExtra("leftStarted", false));
//
//            args.putBoolean("rightStarted", getIntent().getBooleanExtra("rightStarted", false));
//
//
//
//            nursingFragment.setArguments(args);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, nursingFragment)
//                    .addToBackStack("feeding_stack")
//                    .commit();
//        }

//        if (intentString!=null && intentString.equals("feeding_activity_bottle")){
//            showFragment(BottleFeedFragment.class, "bottle_feed", false);

//        } else  {

//        }
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




    @Override
    public void onStart(){
        super.onStart();
        scopedBus.register(eventListener);


    }

    @Override
    public void onStop(){
        super.onStop();
        scopedBus.unregister(eventListener);
//        getSupportFragmentManager().removeOnBackStackChangedListener(listener);
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
//            showFragment(DiaperChangeFragment.class, "diaper_change", falgse);
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


        /*
         * event called when an feed item is saved/deleted/edited
         */
        @Subscribe
        public void onFeedItemSaved(FeedItemCreatedEvent event) {
            Log.d(TAG, "onFeedSavedEvent");
            closeSoftKeyBoard();
//            finish();
//            showFragment(FeedingListFragment.class, "feeding_list",false);

//            Log.d(TAG, "onDiaperLogCreatedEvent");
            getSupportFragmentManager().popBackStackImmediate();
        }

        @Subscribe
        public void onFeedItemClickedEvent(FeedItemClickedEvent event) {
            Log.d(TAG, "onFeedItemClickedEvent" + event.getFeedDao());
            Fragment fragment;
            if (event.getFeedDao().getFeedType()== FeedType.BOTTLE) {
                fragment = new BottleFeedFragment();
            } else {
                fragment = new NursingFeedFragment();
            }
//            DiaperChangeFragment fragment = new DiaperChangeFragment();
            Bundle args = new Bundle();
            args.putInt("feed_id", event.getFeedDao().getId());
            fragment.setArguments(args);

            Log.d(TAG, "adding to back stack ");

//            Fragment localFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack("main_screen_stack")
                    .commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Listen to the back stack...
     */

    private FragmentManager.OnBackStackChangedListener getListener()
    {
        FragmentManager.OnBackStackChangedListener onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener()
        {
            public void onBackStackChanged()
            {
                Log.d(TAG, "backstack changed");
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null)
                {   int count = manager.getBackStackEntryCount();
                    Log.d(TAG, " number of fragments " + manager.getFragments().size());
                    Log.d(TAG, " count " + count);
                    if (manager.getFragments().size() > 0 ) {
                        Log.d(TAG, " calling the fragment ");
                        InjectableFragment currFrag = (InjectableFragment)manager.getFragments().get(0);
                        Log.d(TAG, "currFrag " + currFrag);
                        currFrag.onFragmentResume();
                    }

                }
            }
        };

        return onBackStackChangedListener;
    }
}
