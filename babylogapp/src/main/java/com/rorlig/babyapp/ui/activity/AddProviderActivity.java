package com.rorlig.babyapp.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import com.google.gson.Gson;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.model.ItemModel;
import com.rorlig.babyapp.ui.fragment.invite.AddProviderFragment;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author gaurav gupta
 */

public class AddProviderActivity extends InjectableActivity {

    @Inject
    SharedPreferences preferences;
    private String TAG = "AddProviderActivity";
    @Inject
    Gson gson;
    private ArrayList<ItemModel> logs;

    private EventListener eventListener = new EventListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFragment(AddProviderFragment.class, "add_provider_fragment", false);
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



    private class EventListener {
        private EventListener() {
        }






    }


}
