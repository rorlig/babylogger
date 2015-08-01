package com.rorlig.babylog.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;
import com.rorlig.babylog.R;

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
public class LicenseActivity extends LibsActivity {


//    private String TAG  = "LicenseActivity";
//
//    private EventListener eventListener = new EventListener();
//    private Libs libs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*
        Intent intent = new Intent();
        intent.putExtra(Libs.BUNDLE_FIELDS, Libs.toStringArray(R.string.class.getFields()));
        intent.putExtra(Libs.BUNDLE_LIBS, new String[]{"activeandroid", "caldroid"});
        setIntent(intent);
        */

        setIntent(new LibsBuilder().withFields(R.string.class.getFields())
                .withActivityTitle("Licenses")
                .withLicenseShown(true)
                .withActivityTheme(R.style.AppTheme)
                .withActivityStyle(Libs.ActivityStyle.LIGHT)
                .intent(this));


        super.onCreate(savedInstanceState);
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_license);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        setContentView(R.layout.activity_main);
//
//        new LibsBuilder()
//                .withFields(R.string.class.getFields())
//                .withLibraries("crouton, actionbarsherlock", "showcaseview")
//                .withAutoDetect(true)
//                .withLicenseShown(true)
//                .withVersionShown(true)
//
//                .withListener(new LibsConfiguration.LibsListener() {
//                    @Override
//                    public void onIconClicked(View v) {
//                        Toast.makeText(v.getContext(), "We are able to track this now ;)", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public boolean onLibraryAuthorClicked(View v, Library library) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onLibraryContentClicked(View v, Library library) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onLibraryBottomClicked(View v, Library library) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onExtraClicked(View v, Libs.SpecialButton specialButton) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onIconLongClicked(View v) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onLibraryAuthorLongClicked(View v, Library library) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onLibraryContentLongClicked(View v, Library library) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onLibraryBottomLongClicked(View v, Library library) {
//                        return false;
//                    }
//                })
//                .start(LicenseActivity.this);
//
////        showFragment(LicensesFragment.class, "licenses_fragment", false);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }






//    /*
//     * Class to swap fragments in and out
//     */
//
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
//        scopedBus.register(eventListener);
    }

    @Override
    public void onStop(){
        super.onStop();
//        scopedBus.unregister(eventListener);
    }


    private class EventListener {
        private EventListener(){
        }




    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.d(TAG, "saving event list");
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
