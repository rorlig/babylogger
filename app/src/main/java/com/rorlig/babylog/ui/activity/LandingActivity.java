package com.rorlig.babylog.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.otto.ItemCheckChangeEvent;
import com.rorlig.babylog.otto.ItemsSelectedEvent;
import com.rorlig.babylog.otto.events.ui.MenuItemSelectedEvent;
import com.rorlig.babylog.ui.adapter.LogItemAdapter;
import com.rorlig.babylog.ui.fragment.action.ActionsSelectFragment;
import com.rorlig.babylog.ui.fragment.home.HomeFragment;
import com.rorlig.babylog.ui.fragment.home.LaunchFragment;
import com.rorlig.babylog.ui.fragment.preference.PrefsFragment;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

//import com.fourmob.datetimepicker.date.DatePickerDialog;

public class LandingActivity extends InjectableActivity {

//    ListView listView;
//    private String[] logListings;
//    private LogItemAdapter logItemsAdapter;
//
//    TextView currentDate;
//    private DatePickerDialog datePickerDialog;
//    private String TAG = "MainActivity";
//
//    @InjectView(R.id.babySex)
//    RadioGroup babySexRadioGroup;
//
//    @InjectView(R.id.babyPic)
//    ImageView babyPic;
//
////    @InjectView(R.id.saveBtn)
////    Button saveBtn;
//
//    @InjectView(R.id.babyName)
//    EditText babyNameText;
//
//    BabySex babySex = BabySex.BOY;
//
//    private final static long yearInMillis = 365*24*60*60*1000;
//    private String[] itemNames;
//    private ArrayList<ItemModel> logListItem = new ArrayList<ItemModel>();
//    private Gson gson = new GsonBuilder().create();

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
        } else {
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

//    private void showDatePickerDialog() {

        // Use the current time as the default values for the picker


        // Create a new instance of DatePickerDialog and return it

//        datePickerDialog.show();
////        DialogFragment newFragment = new DatePickerFragment();
//    }

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

//    @Override
//    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
//
//    }


//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        Log.d(TAG, "dateSet " + year + " month " + monthOfYear + " day " + dayOfMonth);
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, monthOfYear, dayOfMonth, 0, 0);
//
//        currentDate.setText(sdf.format(new Date(calendar.getTimeInMillis())));
//    }

//    @OnClick(R.id.saveBtn)
//    public void saveBtnClick() {
//
////        Intent intent = new Intent(this, HomeActivity.class);
////        intent.putExtra("Name", babyNameText.getText());
////        intent.putExtra("Sex", babySex);
//////        intent.putExtra("List", logItemsAdapter.getLogListItem());
//////        Log.d(TAG, "" + logItemsAdapter.getLogListItem());
////        preferences.edit().putString("logItems", gson.toJson(logItemsAdapter.getLogListItem())).commit();
////
////        startActivity(intent);
//
//    }

    private void showFragment(Class<?> paramClass,String paramString, boolean addToStack ){
        Log.d(TAG, "showFragment for " + paramClass);

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
                            .replace(R.id.fragment_container, localFragment)
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


    private class EventListener {
        private EventListener() {
        }

        @Subscribe
        public void onItemsSelectedEvent(ItemsSelectedEvent itemSelectedEvent) {

//
//            if (itemSelectedEvent.getName().equals("")) {
//                Toast.
//            }
            //handle the fragment swap...
            preferences.edit().putString("logItems", gson.toJson(itemSelectedEvent.getLogListItem())).commit();
            preferences.edit().putString("name", itemSelectedEvent.getName()).commit();
            preferences.edit().putString("dob", itemSelectedEvent.getDob()).commit();
            showFragment(HomeFragment.class, "home_fragment", false);
//            Log.d(TAG, "MenuItemSelectedEvent " + itemSelectedEvent.getMenuId());
//            selectMenuItem(menuItemSelectedEvent.getMenuId());
        }


    }

}
