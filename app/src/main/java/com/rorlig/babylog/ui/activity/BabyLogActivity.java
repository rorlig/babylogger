package com.rorlig.babylog.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.rorlig.babylog.R;
//import com.rorlig.babylog.common.AppConstants;
//import com.rorlig.babylog.otto.events.filter.DistanceFilterChanged;
//import com.rorlig.babylog.otto.events.filter.FilterChangedEvent;
//import com.rorlig.babylog.otto.events.filter.LocationChangedEvent;
//import com.rorlig.babylog.otto.events.ui.EventSelectedEvent;
//import com.rorlig.babylog.otto.events.ui.MenuItemSelectedEvent;
//import com.rorlig.babylog.otto.events.ui.TimeFilterChanged;
import com.rorlig.babylog.otto.events.AddDiaperChangeEvent;
import com.rorlig.babylog.otto.events.other.AddItemEvent;
import com.rorlig.babylog.otto.events.ui.ActionSelectItem;
import com.rorlig.babylog.otto.events.ui.BottleEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.otto.events.ui.MenuItemSelectedEvent;
import com.rorlig.babylog.otto.events.ui.NursingEvent;
import com.rorlig.babylog.scheduler.TypeFaceManager;
//import com.rorlig.babylog.ui.fragment.ConnectionsFragment;
//import com.rorlig.babylog.ui.fragment.LoggingFragment;
import com.rorlig.babylog.ui.fragment.action.ActionsSelectFragment;
import com.rorlig.babylog.ui.fragment.contact.ContactsPagerListFragment;
import com.rorlig.babylog.ui.fragment.diaper.DiaperChangeListFragment;
import com.rorlig.babylog.ui.fragment.feed.BottleFeedFragment;
import com.rorlig.babylog.ui.fragment.development.DevelopmentFragment;
import com.rorlig.babylog.ui.fragment.feed.NursingFeedFragment;
import com.rorlig.babylog.ui.fragment.feed.FeedSelectFragment;
import com.rorlig.babylog.ui.fragment.feed.FeedingListFragment;
//import com.rorlig.babylog.ui.fragment.MessagesFragment;
//import com.rorlig.babylog.ui.fragment.MyEventsFragment;
import com.rorlig.babylog.ui.fragment.growth.GrowthListFragment;
import com.rorlig.babylog.ui.fragment.profile.ProfileFragment;
import com.rorlig.babylog.ui.fragment.sleep.SleepFragment;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by admin on 12/15/13.
 */
public class BabyLogActivity extends InjectableActivity implements FragmentManager.OnBackStackChangedListener{

    //todo still figure out left + right toggle speeds...
    @InjectView(R.id.drawer_layout_main)
    DrawerLayout drawerLayout;


    @Inject
    ActionBar actionBar;

    @Inject
    Gson gson;

    @Inject
    TypeFaceManager typeFaceManager;

    @InjectView(R.id.action_bottle_feed)
    FloatingActionButton actionButtonA;


    @InjectView(R.id.action_diaper_change)
    FloatingActionButton actionButtonB;


    @InjectView(R.id.action_milestones)
    FloatingActionButton actionButtonC;

    @InjectView(R.id.action_nursing)
    FloatingActionButton actionButtonD;

    @InjectView(R.id.action_sleep)
    FloatingActionButton actionButtonE;

    @InjectView(R.id.action_growth)
    FloatingActionButton actionButtonF;






    private ActionBarDrawerToggle drawerToggle;


    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String TAG  = "BabyLogActivity";

    private EventListener eventListener = new EventListener();
    SpannableString s;
    private Typeface typeface;
    private String savedLocation;
    private Address savedAddress;
    private boolean fromRotation;


    /*
   * Define a request code to send to Google Play services
   * This code is returned in Activity.onActivityResult
   */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private View actionBarView;
    private TextView actionBarTitleTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");


        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        actionBarView = inflator.inflate(R.layout.action_bar_main, null);
//        System.out.println("titleId " + titleId);

        actionBarTitleTextView = (TextView) actionBarView.findViewById(R.id.actionBarTitle);
        actionBarTitleTextView.setText("babylogger");
//        ((TextView)v.findViewById(R.id.actionBarTitle)).setText("greet");
        //doesn't work :(

        typeface = typeFaceManager.getTypeFace(getString(R.string.font_bree_light));
//        typeface= Typeface.createFromAsset(getAssets(),
//                "fonts/bree_light.ttf");

        actionBarTitleTextView.setTypeface(typeface);

//        TextView titleTextView = (TextView) getWindow().findViewById(titleId);
//        titleTextView.setTextColor(getResources().getColor(R.color.black));
//
//        titleTextView.setTypeface(typeface);
//        titleTextView.setText("greet");
        getActionBar().setCustomView(actionBarView);

//        s = new SpannableString("greet");//todo put in resources
//        s.setSpan(typeface, 0, s.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////        Log.d(TAG, "s " + s);
////        actionBar.set
//        actionBar.setTitle(s);
//
//
////        drawerLayout.setDrawerListener(drawerToggle);
////        drawerLayout.openDrawer(1);
////        drawerLayout.setDrawerLockMode(1);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

//        actionBar.setDisplayShowTitleEnabled(true);


        initActionBarToggle();

//        savedLocation = filterManager.getLocation();
//        if (!savedLocation.equals("Current Location")){
//            Log.d(TAG, "savedLocation is not default");
//            savedAddress = gson.fromJson(savedLocation, Address.class);
//        }
//
//        if (savedAddress == null) {
//            Log.d(TAG, "savedAddress is null");
//            scopedBus.post(new FilteredLocationEvent(savedLocation));
//        } else {
//            Log.d(TAG, "locality " + savedAddress.getLocality() + " subadminarea " + savedAddress.getSubAdminArea());
//
//            String city="";
//            if (savedAddress.getLocality()!=null) {
//                city = savedAddress.getLocality();
//            } else if (savedAddress.getSubAdminArea()!=null){
//                city = savedAddress.getSubAdminArea();
//            }
//
//            Log.d(TAG, "city " + city);
//
//            scopedBus.post(new FilteredLocationEvent(city));
//        }
//        if (savedInstanceState!=null){
//            fromRotation = savedInstanceState.getBoolean(AppConstants.EVENTLIST_ROTATION, false);
//        }

//        if (savedInstanceState == null) {
//            TestFragment test = new TestFragment();
//            test.setArguments(getIntent().getExtras());
//            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, test, "your_fragment_tag").commit();
//        } else {
//            TestFragment test = (TestFragment) getSupportFragmentManager().findFragmentByTag("your_fragment_tag");
//        }
//        if (savedInstanceState==null) {

        //Listen for changes in the back stack
//        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
//        shouldDisplayHomeUp();


        if (savedInstanceState!=null) {
            Log.d(TAG, "due to rotation");
            Log.d(TAG, "fragments size " + getSupportFragmentManager().getFragments().size());
        } else {
            Log.d(TAG, "first time around");
            showFragment(ActionsSelectFragment.class, "main_fragment", false);
        }
//        } else {
//
//        }

//        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
    }

//    @Override
//    public void onConnected(Bundle bundle) {
////        Log.d(TAG, "onConnected");
////        Log.d(TAG, "fromRotation " + fromRotation);
////        super.onConnected(bundle);
////        if (fromRotation) {
////            return;
////        }
////        Location currentLocation = locationClient.getLastLocation();
////        if(currentLocation!=null && savedLocation.equals("Current Location")){
////            //also check if the current location filter is not null and then post this to filter fragment...
////            Log.d(TAG, "current location");
////            scopedBus.post(new LocationEvent(currentLocation));
////            //post this to filterfragment to find the current location...
////        } else {
////            Log.d(TAG, "saved location is present");
////            scopedBus.post(new LocationChangedEvent(savedAddress));
////        }
//    }
//
//    @Override
//    public void onDisconnected() {
//        super.onDisconnected();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        super.onConnectionFailed(connectionResult);
//        /*
//         * Google Play services can resolve some errors it detects.
//         * If the error has a resolution, try sending an Intent to
//         * start a Google Play services activity that can resolve
//         * error.
//         */
////        if (connectionResult.hasResolution()) {
////            try {
////                // Start an Activity that tries to resolve the error
////                connectionResult.startResolutionForResult(
////                        this,
////                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
////                /*
////                 * Thrown if Google Play services canceled the original
////                 * PendingIntent
////                 */
////            } catch (IntentSender.SendIntentException e) {
////                // Log the error
////                e.printStackTrace();
////            }
////        } else {
////            /*
////             * If no resolution is available, display a dialog to the
////             * user with the error.
////             */
//////            showErrorDialog(connectionResult.getErrorCode());
////        }
////        scopedBus.post(new LocationServicesError());
//    }getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change



//    private boolean servicesConnected() {
//        // Check that Google Play services is available
//        int resultCode =
//                GooglePlayServicesUtil.
//                        isGooglePlayServicesAvailable(this);
//        // If Google Play services is available
//        if (ConnectionResult.SUCCESS == resultCode) {
//            // In debug mode, log the status
//            Log.d("Location Updates",
//                    "Google Play services is available.");
//            // Continue
//            return true;
//            // Google Play services was not available for some reason
//        } else {
//            // Get the error code
//            int errorCode = connectionResult.getErrorCode();
//            // Get the error dialog from Google Play services
//            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
//                    errorCode,
//                    this,
//                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
//
//            // If Google Play services can provide an error dialog
//            if (errorDialog != null) {
//                // Create a new DialogFragment for the error dialog
//                ErrorDialogFragment errorFragment =
//                        new ErrorDialogFragment();
//                // Set the dialog in the DialogFragment
//                errorFragment.setDialog(errorDialog);
//                // Show the error dialog in the DialogFragment
//                errorFragment.show(getSupportFragmentManager(),
//                        "Location Updates");
//            }
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the option menu items..
//        getMenuInflater().inflate(R.menu.main, menu);

//        MenuItem menuItem = menu.findItem(R.id.action_search);
//
//        if (menu!=null) {
////            SearchView searchView = (SearchView) menuItem.getActionView();
////             if (searchView!=null) {
////                 Log.d(TAG, "searchView is not null");
//////                 searchView.setSearchableInfo(((SearchManager)getSystemService("search")).getSearchableInfo(getComponentName()));
////                 searchView.setQueryRefinementEnabled(true);
////             }
//        }
        return super.onCreateOptionsMenu(menu);
    }


    @OnClick(R.id.action_bottle_feed)
    public void onSaveBtnClicked() {

    }



        private void initActionBarToggle() {
        Log.d(TAG, "initActionBarToggle");
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View paramView) {
                Log.d(TAG, "drawer closed" + paramView.getId());
                actionBar.setTitle(s);
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerOpened(View paramView){
                Log.d(TAG, "onDrawerOpened");
                if (R.id.left_drawer == paramView.getId()){
                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    }
                } else {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }
                }
                Log.d(TAG, "drawer opened" + paramView.getId());
                actionBar.setTitle(s);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.left_drawer);
//        boolean drawerOpen = drawerLayout.isDrawerOpen()

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        Log.d(TAG, "onOptionsItemSelected");
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

//        switch (item.getItemId()){
////            case R.id.action_search:
////                break;
////            case R.id.action_add:
////                Log.d(TAG, "action_add");
////                drawerLayout.setDrawerListener(null);
////                if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
////                    Log.d(TAG, "closing left drawer");
////                    drawerLayout.closeDrawer(Gravity.LEFT);
////                }
//////                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)){
//////                    Log.d(TAG, "closing right drawer");
//////                    drawerLayout.closeDrawer(Gravity.RIGHT);
//////                } else {
//////                    Log.d(TAG, "opening right drawer");
////////                    drawerLayout.openDrawer();
//////                    drawerLayout.openDrawer(Gravity.RIGHT);
//////                }
////                drawerLayout.setDrawerListener(drawerToggle);
////                showFragment(ActionsSelectFragment.class, "actions_fragment");
////                break;
//
////            case R.id.action_settings:
////                Intent detailsEventIntent = new Intent(getApplicationContext(), SettingsActivity.class);
////                startActivity(detailsEventIntent);
////                break;
////            case R.id.action_licenses:
////                break;
//
//
//        }
//        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void showFragment(Class<?> paramClass,String paramString) {

        showFragment(paramClass, paramString, true);
    }
    /*
     * Class to swap fragments in and out
     */

    private void showFragment(Class<?> paramClass,String paramString, boolean addToStack ){
        Log.d(TAG, "showFragment for " + paramClass);
        closeDrawers();

        FragmentManager localFragmentManager = getSupportFragmentManager();



        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);

        if ((localFragment==null)||(!paramClass.isInstance(localFragment))){
            Log.d(TAG, "adding to back stack ");
            try {
               localFragment = (Fragment)paramClass.newInstance();
                if (addToStack) {
                    localFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, localFragment)
                            .addToBackStack("main_screen_stack")
                            .commit();
                } else {
                    localFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, localFragment)
                            .commit();
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

//            try {
//                Log.d(TAG, "replacing fragments");
//                localFragment = (Fragment)paramClass.newInstance();
//                localFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, localFragment)
//                        .commitAllowingStateLoss();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void closeDrawers() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }

        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
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

    private class EventListener {
        private EventListener(){
        }

        @Subscribe
        public void onMenuItemSelected(MenuItemSelectedEvent menuItemSelectedEvent) {
            //handle the fragment swap...
            Log.d(TAG, "MenuItemSelectedEvent " + menuItemSelectedEvent.getMenuId());
            selectMenuItem(menuItemSelectedEvent.getMenuId());
        }

        @Subscribe
        public void onNursingEvent(NursingEvent event){
            showFragment(NursingFeedFragment.class, "feeding_fragment");
        }


        @Subscribe
        public void onBottlingEvent(BottleEvent event){
            showFragment(BottleFeedFragment.class, "feeding_fragment");
        }

        @Subscribe
        public void onActionSelectItem(ActionSelectItem item) {
            selectActionItem(item.getId());
        }

        @Subscribe
        public void onFragmentCreated(FragmentCreated fragmentCreated){
            actionBarTitleTextView.setText(fragmentCreated.getFragmentTitle());
        }

        @Subscribe
        public void onAddDiaperChangeEvent(AddDiaperChangeEvent diaperChangeEvent){
//            showFragment(DiaperChangeFragment.class, "diaper_change");
            Log.d(TAG, "onAddDiaperChangeEvent");
            startActivity(DiaperChangeActivity.class,"diaper_change");
        }


        @Subscribe
        public void onItemAddedEvent(AddItemEvent addItemEvent){
            Log.d(TAG, "onItemAddedEvent");
            switch (addItemEvent.getItemType()) {
                case DIAPER_CHANGE:
                    startActivity(DiaperChangeActivity.class,"diaper_change");
                    break;
                case FEED_NURSING:
                    startActivity(NursingFeedActivity.class,"feed_nursing");
                    break;
                case FEED_BOTTLE:
                    startActivity(BottleFeedingActivity.class, "feed_bottle");
                    break;
                case GROWTH_LOG:
                    startActivity(GrowthActivity.class,"growth_activity");
                    break;
            }
        }


//
//        @Subscribe
//        public void onEventSelected(EventSelectedEvent eventSelectedEvent) {
//            //handle the fragment swap...
////            Log.d(TAG, "EventSelectedEvent eventId:  " + eventSelectedEvent.getEvent().getId()
////            + " category: " + eventSelectedEvent.getEvent().getCategory()[0]);
//            //pass the eventId via extras - todo
//            Intent detailsEventIntent = new Intent(getApplicationContext(), EventDetailActivity.class);
//            Log.d(TAG, "Gson gson: " + gson);
//            detailsEventIntent.putExtra("event", new Gson().toJson(eventSelectedEvent.getEvent()));
//            startActivity(detailsEventIntent);
//        }
//
//        @Subscribe
//        public void onLocationChanged(LocationChangedEvent event) {
//           closeDrawers();
//        }
//
//
//
//        @Subscribe
//        public void onFilterChanged(FilterChangedEvent event) {
//            closeDrawers();
//        }
//
//        @Subscribe
//        public void onDistanceFilterChanged(DistanceFilterChanged event){
//            Log.d(TAG, "onDistanceFilterChanged");
//            closeDrawers();
//        }
//
//        @Subscribe
//        public void onTimeFilterChanged(TimeFilterChanged event){
//            Log.d(TAG, "onTimeFilterChanged");
//            closeDrawers();
//        }
    }

    private void startActivity(Class<?> paramClass,String paramString){
        closeDrawers();
        Intent intent = new Intent(this, paramClass);
        startActivity(intent);
    }
    /*
     * selectMenuItem
     * @param int menuId
     */
    private void selectMenuItem(int menuId) {
        switch(menuId){
            case 0:
                Log.d(TAG, "show events fragment");
//                startActivity(DiaperChangeListActivity.class, "diaper_change_activity");
                showFragment(DiaperChangeListFragment.class,"diaper_list_fragment");
                break;
            case 1:
                Log.d(TAG, "show my events fragment");
//                startActivity(FeedingListActivity.class, "feed_list_activity");
//                showFeedSelectFragment(new FeedSelectFragment(), "feed select fragment");
                showFragment(FeedingListFragment.class, "feed_list_fragment");
                break;
            case 2:
                Log.d(TAG, "show my connections");
                showFragment(GrowthListFragment.class,"growth_list_fragment");
                break;
            case 3:
                Log.d(TAG, "show my messages");
                showFragment(SleepFragment.class,"sleep fragment");
                break;
            case 4:
                Log.d(TAG, "show development");
                showFragment(DevelopmentFragment.class,"development fragment");
                break;
            case 5:
                Log.d(TAG, "about fragment");
                showFragment(ContactsPagerListFragment.class, "contacts");
//                showFragment(AboutFragment.class, "about fragment");
                break;
            case 6:
                Log.d(TAG, "stats fragment");
                showFragment(ActionsSelectFragment.class, "action select");
                break;
            case 8:
                Log.d(TAG, "profile fragment");
                showFragment(ProfileFragment.class, "profile fragment");
                break;

        }

    }

    private void showFeedSelectFragment(FeedSelectFragment feedSelectFragment, String s) {
        closeDrawers();
        feedSelectFragment.show(getSupportFragmentManager(),"feed_select");
    }


    /*
   * selectMenuItem
   * @param int menuId
   */
    private void selectActionItem(int menuId) {
        switch(menuId){
            case 0:
                Log.d(TAG, "show diaper fragment");
                showFragment(DiaperChangeListFragment.class,"diaper_list_fragment");

//                startActivity(DiaperChangeListActivity.class, "diaper_change_activity");
                break;
            case 1:
                Log.d(TAG, "show feed select connections");
//                startActivity(FeedingListActivity.class, "feed_list_activity");

                showFragment(FeedingListFragment.class, "feed_list_fragment");
                break;
            case 2:
                Log.d(TAG, "show growth");
                showFragment(GrowthListFragment.class,"growth_list_fragment");
                break;
            case 3:
                Log.d(TAG, "show sleep");
                showFragment(SleepFragment.class,"sleep fragment");
                break;
            case 4:
                Log.d(TAG, "show development");
                showFragment(DevelopmentFragment.class,"development fragment");
                break;
            case 5:
                Log.d(TAG, "show stats");
//                showFragment(.class,"diaper_change_fragment");
                break;
            case 6:
                showFragment(ProfileFragment.class, "profile_frag");
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean(AppConstants.EVENTLIST_ROTATION, true);
        Log.d(TAG, "saving event list");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
//                        ...
                        break;
                }
//                ...
        }
    }




    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>1;
        getActionBar().setDisplayHomeAsUpEnabled(canback);
    }


    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

    /*
     *
     */

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /*
     *
     */

    private FragmentManager.OnBackStackChangedListener getListener() {
        Log.d(TAG, "Fragment OnBackStackChanged");
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    Log.d(TAG, "backStackEntryCount " + backStackEntryCount);
                    if (backStackEntryCount == 0) {
//                        finish();
                        mTitle = "babylogger";
                        restoreActionBar();
                    } else {
                        Fragment fragment = manager.getFragments()
                                .get(backStackEntryCount - 1);
                        fragment.onResume();
                    }

                }
            }
        };
        return result;
    }
}
