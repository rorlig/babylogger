package com.rorlig.babyapp.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.DiaperChangeItemClickedEvent;
import com.rorlig.babyapp.otto.ItemDeleted;
import com.rorlig.babyapp.otto.events.diaper.DiaperLogCreatedEvent;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.otto.events.stats.StatsItemEvent;
import com.rorlig.babyapp.scheduler.TypeFaceManager;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperChangeFragment;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperChangeListFragment;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperStatsFragment;
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
public class DiaperChangeActivity extends InjectableActivity {

    //todo still figure out left + right toggle speeds...


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
//        setContentView(R.layout.activity_diaper_change_list);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary_purple));
//        getSupportActionBar().setBackgroundDrawable(R.color.primary_dark_purple);

//        ImageView profileImageIcon = (ImageView) toolbar.findViewById(R.id.icon_image);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        titleT.setText(getTitle());

//        final String imageUri = preferences.getString("imageUri", "");
//        Log.d(TAG, "imageUri " + imageUri);
//        if (imageUri.equals("")) {
////            getSupportActionBar().setIcon(R.drawable.ic_action_profile);
//
//
//        } else  {
//            profileImageIcon.setImageURI(Uri.parse(imageUri));
////                getSupportActionBar().setIcon(AppUtils.getDrawableFromUri
////                        (Uri.parse(preferences.getString("imageUri", "")), getApplicationContext()));
//        }


        String intentString = getIntent().getStringExtra("intent");

        Log.d(TAG, " " +  intentString);

//        if (intentString!=null && intentString.equals("diaper_change")){
//            showFragment(DiaperChangeFragment.class, "diaper_change_fragment", false);

//        } else  {

        //show fragment only if not coming from rotation event...
        if (savedInstanceState==null) {
            showFragment(DiaperChangeListFragment.class, "diaper_change_list_fragment", false);
        }

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




//



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

//    private void startActivity(Class<?> paramClass,String paramString){
//        Intent intent = new Intent(this, paramClass);
//        startActivity(intent);
//    }

    private class EventListener {
        private EventListener(){
        }

//        @Subscribe
//        public void onDiaperLogCreatedEvent(DiaperLogCreatedEvent event) {
//            Log.d(TAG, "onDiaperLogCreatedEvent");
//            getSupportFragmentManager().popBackStackImmediate();
//            closeSoftKeyBoard();
////            showFragment(DiaperChangeListFragment.class, "diaper_change_list", false);
//
////            getFragmentManager().popBackStackImmediate();
////            finish();
//        }

        @Subscribe
        public void onStatsItemEvent(StatsItemEvent statsItemEvent) {
                    showFragment(DiaperStatsFragment.class, "diaper_stats_fragment", true);
        }



        @Subscribe
        public void onItemAddedEvent(AddItemEvent addItemEvent){
            Log.d(TAG, "onItemAddedEvent");
            switch (addItemEvent.getItemType()) {
                case DIAPER_CHANGE:
//                    getSupportFragmentManager().popBackStackImmediate();
//                    closeSoftKeyBoard();
                    showFragment(DiaperChangeFragment.class, "diaper_change", true);
                    break;
            }
        }




        @Subscribe
        public void onDiaperItemClickedEvent(DiaperChangeItemClickedEvent event) {
            Log.d(TAG, "diaperChangeItemClicked");
            Log.d(TAG, "item it " + event.getDiaperChange().getObjectId());
            DiaperChangeFragment fragment = new DiaperChangeFragment();
            Bundle args = new Bundle();
            args.putString("id", event.getDiaperChange().getObjectId());
            args.putInt("position", event.getPosition());
            args.putString("uuid" , event.getDiaperChange().getUuidString());
            fragment.setArguments(args);

            Log.d(TAG, "adding to back stack ");

//            Fragment localFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack("main_screen_stack")
                    .commit();

        }

        /*
         * event called when an feed item is saved/deleted/edited
         * todo need to modify to probably get the modified elements and positions
         * --- move these to the base classs --
         */
        @Subscribe
        public void onDiaperChangeItem(ItemCreatedOrChanged event) {
            Log.d(TAG, "onDiaperChangeItem");
            closeSoftKeyBoard();
//            finish();
//            showFragment(FeedingListFragment.class, "feeding_list",false);

//            Log.d(TAG, "onDiaperLogCreatedEvent");
            getSupportFragmentManager().popBackStackImmediate();
        }

        @Subscribe
        public void onItemDeletedEvent(ItemDeleted event) {
            Log.d(TAG, "onItemDeletedEvent");
            closeSoftKeyBoard();
            getSupportFragmentManager().popBackStackImmediate();
        }


//
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
            case R.id.action_settings:
                startActivity(new Intent(this, PrefsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(this, ExportActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
