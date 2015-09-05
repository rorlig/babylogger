package com.rorlig.babyapp.ui.fragment.preference;

/**
 * Created by Gaurav Gupta on 12/31/13.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.Browser;
import android.util.Log;

import com.rorlig.babyapp.R;

import javax.inject.Inject;

/**
 * This fragment shows the preferences for the first header.
 * todo refactor this entire class...
 */
public class PrefsFragment extends InjectablePreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    SharedPreferences sharedPref;


    private String TAG = "PrefsFragment";

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_general);

        Preference termsAndConditions = findPreference(getString(R.string.string_toc));
        if (termsAndConditions != null) {
            termsAndConditions
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference arg0) {
                            Uri uri = Uri.parse(getString(R.string.toc_link));
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.putExtra(Browser.EXTRA_APPLICATION_ID, getActivity().getPackageName());
                            startActivity(intent);
                            return true;
                        }
                    });
        }

        // Create listener for more settings button
        Preference privacy = findPreference(getString(R.string.settings_privacy));
        if (privacy != null) {
            privacy
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference arg0) {
                            Uri uri = Uri.parse(getString(R.string.privacy_link));
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.putExtra(Browser.EXTRA_APPLICATION_ID, getActivity().getPackageName());
                            startActivity(intent);
                            return true;
                        }
                    });
        }

        // Create listener for more settings button
        Preference termsAndConditions = findPreference(getString(R.string.string_toc));
        if (termsAndConditions != null) {
            termsAndConditions
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference arg0) {
                            Uri uri = Uri.parse(getString(R.string.toc_link));
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.putExtra(Browser.EXTRA_APPLICATION_ID, getActivity().getPackageName());
                            startActivity(intent);
                            return true;
                        }
                    });
        }

        // Create listener for more settings button
        Preference privacy = findPreference(getString(R.string.settings_privacy));
        if (privacy != null) {
            privacy
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference arg0) {
                            Uri uri = Uri.parse(getString(R.string.privacy_link));
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.putExtra(Browser.EXTRA_APPLICATION_ID, getActivity().getPackageName());
                            startActivity(intent);
                            return true;
                        }
                    });
        }



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


    private void unRegisterSharedPreferenceChangeListener() {
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void registerSharedPreferenceChangeListener() {
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }


}

