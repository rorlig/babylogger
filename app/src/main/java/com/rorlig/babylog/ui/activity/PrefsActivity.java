package com.rorlig.babylog.ui.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.rorlig.babylog.R;
import com.rorlig.babylog.scheduler.TypeFaceManager;
import com.rorlig.babylog.ui.fragment.preference.PrefsFragment;

import javax.inject.Inject;



/**
 * Created by admin on.
 */
public class PrefsActivity extends InjectableActivity {

    //todo still figure out left + right toggle speeds...


    @Inject
    ActionBar actionBar;

    @Inject
    Gson gson;

    @Inject
    TypeFaceManager typeFaceManager;




    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String TAG  = "PrefsActivity";

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
        setContentView(R.layout.activity_base);

        PrefsFragment mPrefsFragment = new PrefsFragment();

//        FragmentManager localFragmentManager = getSupportFragmentManager();


        FragmentTransaction mFragmentTransaction = getFragmentManager()
                .beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, mPrefsFragment);
        mFragmentTransaction.commit();
//        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);

//        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
//        mFragmentTransaction.commit();

//        showFragment(PrefsFragment.class, "prefs_fragment", false);


//        localFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, mPrefsFragment)
//                .commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the option menu items..
//        getMenuInflater().inflate(R.menu.add_item, menu);

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




    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



}
