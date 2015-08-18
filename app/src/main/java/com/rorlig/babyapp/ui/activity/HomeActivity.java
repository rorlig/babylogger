package com.rorlig.babyapp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.model.ItemModel;
import com.rorlig.babyapp.otto.ItemsSelectedEvent;
import com.rorlig.babyapp.otto.UpdateProfileEvent;
import com.rorlig.babyapp.ui.fragment.home.HomeFragment;
import com.rorlig.babyapp.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by rorlig on 5/31/15.
 */
public class HomeActivity extends InjectableActivity {

    @Inject
    SharedPreferences preferences;
    private String TAG = "HomeActivity";
    @Inject
    Gson gson;
    private ArrayList<ItemModel> logs;

    private EventListener eventListener = new EventListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFragment(HomeFragment.class, "home_fragment", false);
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

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, PrefsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(this, ExportActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



//    private void showFragment(Class<?> paramClass,String paramString, boolean addToStack ){
//        Log.d(TAG, "showFragment for " + paramClass);
//
//        FragmentManager localFragmentManager = getSupportFragmentManager();
//
//
//
//        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);
//
//        if ((localFragment==null)||(!paramClass.isInstance(localFragment))){
//            Log.d(TAG, "adding to back stack ");
//            try {
//                localFragment = (Fragment)paramClass.newInstance();
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
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //update tool bar...
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== AppUtils.PROFILE_ACTIVITY) {
            Log.d(TAG, "result from profile activity");
//            Log.d(TAG, "profile changes " + data.getBooleanExtra("saved_profile", false));
            if (data!=null && data.getBooleanExtra("saved_profile", false)) {
                scopedBus.post(new UpdateProfileEvent());
            }

        }
    }


    private class EventListener {
        private EventListener() {
        }

        @Subscribe
        public void onItemsSelectedEvent(ItemsSelectedEvent itemSelectedEvent) {
            preferences.edit().putString("logItems", gson.toJson(itemSelectedEvent.getLogListItem())).apply();
            preferences.edit().putString("name", itemSelectedEvent.getName()).apply();
            preferences.edit().putString("dob", itemSelectedEvent.getDob()).apply();
            showFragment(HomeFragment.class, "home_fragment", false);
        }

//        @Subscribe
//        public void updateActionBar(UpdateActionBarEvent event){
//            Log.d(TAG, "updating action bar");
//            profileImageIcon.setImageDrawable(event.getDrawable());
//        }


    }
}
