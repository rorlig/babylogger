package com.rorlig.babylog.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String logItems = preferences.getString("logItems", "");

        Log.d(TAG, logItems);
        if (!logItems.equals("")) {

            logs = gson.fromJson(logItems, new TypeToken<List<ItemModel>>(){}.getType());
            Log.d(TAG, "logs " + logs);
        }
    }
}
