package com.rorlig.babyapp.ui.fragment.preference;

/**
 * Created by Gaurav Gupta on 12/31/13.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;

import com.rorlig.babyapp.R;

import javax.inject.Inject;

/**
 * This fragment shows the preferences for the first header.
 * todo refactor this entire class...
 */
public class PrefsFragment extends InjectablePreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener{

    @Inject
    SharedPreferences sharedPref;





    private String TAG = "PrefsFragment";

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_general);



    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        unRegisterSharedPreferenceChangeListener();

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Log.d(TAG, "onPreferenceChange");

        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            Log.d(TAG, "onSharedPreferenceChanged for key:" + key + " value:" + sharedPreferences.getBoolean(key, false));

    }


    private void unRegisterSharedPreferenceChangeListener(){
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void registerSharedPreferenceChangeListener(){
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }
















}

