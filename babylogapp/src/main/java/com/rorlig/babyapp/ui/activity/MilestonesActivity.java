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
import com.rorlig.babyapp.otto.MilestoneItemClicked;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.other.AddItemEvent;
import com.rorlig.babyapp.ui.fragment.milestones.MilestoneFragment;
import com.rorlig.babyapp.ui.fragment.milestones.MilestoneListFragment;
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
 * Created by gaurav gupta
 */
public class MilestonesActivity extends InjectableActivity {

    //todo still figure out left + right toggle speeds...


    @Inject
    ActionBar actionBar;

    @Inject
    Gson gson;




    private String TAG  = "MilestonesActivity";

    private EventListener eventListener = new EventListener();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_milestones);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary_orange));


//        String intentString = getIntent().getStringExtra("intent");
//        Log.d(TAG, " " +  intentString);

        //show fragment only if not coming from rotation event...
        if (savedInstanceState==null) {
            showFragment(MilestoneListFragment.class, "milestones_fragment", false);
        }


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
                case MILESTONE:

                    showFragment(MilestoneFragment.class, "milestone_fragment", true);

                    break;
            }
        }

        @Subscribe
        public void onMilestoneItemCreated(ItemCreatedOrChanged event) {
            Log.d(TAG, "onMilestoneItemCreated");
//            finish();
            getSupportFragmentManager().popBackStackImmediate();
            closeSoftKeyBoard();



        }

//        @Subscribe
//        public void updateActionBar(UpdateActionBarEvent event){
//            Log.d(TAG, "updating action bar");
//            profileImageIcon.setImageDrawable(event.getDrawable());
//        }

        @Subscribe
        public void onMilestoneItemClicked(MilestoneItemClicked event) {
            Log.d(TAG, "diaperChangeItemClicked");
            MilestoneFragment fragment = new MilestoneFragment();
            Bundle args = new Bundle();
            args.putString("id", event.getModel().getObjectId());
            args.putString("uuid", event.getModel().getUuidString());
            fragment.setArguments(args);
            Log.d(TAG, "adding to back stack ");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack("main_screen_stack")
                    .commit();

        }

    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving event list");
    }



}
