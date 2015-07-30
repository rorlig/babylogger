package com.rorlig.babylog.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.ItemsSelectedEvent;
import com.rorlig.babylog.ui.fragment.home.HomeFragment;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

//import com.fourmob.datetimepicker.date.DatePickerDialog;

public class LandingActivity extends InjectableActivity {



    @Inject
    SharedPreferences preferences;

    @Inject
    Gson gson;

    private String TAG ="LandingActivity";

    private EventListener eventListener = new EventListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        boolean tutorial_shown = preferences.getBoolean("tutorial_shown", false);

        if (tutorial_shown) {
            startActivity(new Intent(this, HomeActivity.class));
        }
        else {
            startActivity(new Intent(this, TutorialActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(TAG, "prefs");
//            PrefsFragment mPrefsFragment = new PrefsFragment();
//            showFragment(PrefsFragment.class, "prefs_fragment", true);
//            mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
//            mFragmentTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    private void showFragment(Class<?> paramClass,String paramString, boolean addToStack ) {
//        Log.d(TAG, "showFragment for " + paramClass);
//
//        FragmentManager localFragmentManager = getSupportFragmentManager();
//
//
//        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);
//
//        if ((localFragment == null) || (!paramClass.isInstance(localFragment))) {
//            Log.d(TAG, "adding to back stack ");
//            try {
//                localFragment = (Fragment) paramClass.newInstance();
//                if (addToStack) {
//                    localFragmentManager.beginTransaction()
//                            .add(R.id.fragment_container, localFragment)
//                            .addToBackStack("main_screen_stack")
//                            .commit();
//                } else {
//                    localFragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, localFragment)
//                            .commit();
//                }
//
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }


    private class EventListener {
        private EventListener() {
        }

        @Subscribe
        public void onItemsSelectedEvent(ItemsSelectedEvent itemSelectedEvent) {
            preferences.edit().putString("logItems", gson.toJson(itemSelectedEvent.getLogListItem())).apply();
            preferences.edit().putString("name", itemSelectedEvent.getName()).apply();
            preferences.edit().putString("dob", itemSelectedEvent.getDob()).apply();
            showFragment(HomeFragment.class, "home_fragment", false);
//            Log.d(TAG, "MenuItemSelectedEvent " + itemSelectedEvent.getMenuId());
//            selectMenuItem(menuItemSelectedEvent.getMenuId());
        }


    }


}
