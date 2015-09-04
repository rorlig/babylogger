package com.rorlig.babyapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ObjectGraphActivity;
import com.rorlig.babyapp.otto.ScopedBus;
import com.rorlig.babyapp.parse_dao.DiaperChange;
import com.rorlig.babyapp.ui.activity.InjectableActivity;
import com.rorlig.babyapp.ui.activity.LicenseActivity;
import com.rorlig.babyapp.ui.activity.LoginActivity;
import com.rorlig.babyapp.ui.activity.PrefsActivity;
import com.rorlig.babyapp.ui.activity.TutorialActivity;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperChangeStatsType;
import com.rorlig.babyapp.utils.AppConstants;
import com.rorlig.babyapp.utils.AppUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by admin on 12/15/13.
 */
public class InjectableFragment extends Fragment {

    private static final String TAG = "InjectableFragment";
    @Inject
    public SharedPreferences preferences;
    @Inject
    public ScopedBus scopedBus;

    public InjectableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().invalidateOptionsMenu();
//        setHasOptionsMenu(true);


    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.menu_add_item, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//    }

    @Override
    public void onActivityCreated(Bundle paramBundle)
    {
        FragmentActivity localFragmentActivity = getActivity();
        Log.d(TAG, "localFragmentActivity " + localFragmentActivity);
        Log.d(TAG, "localFragmentActivity is instanceof ObjectGraphActivity "
                    + (localFragmentActivity instanceof ObjectGraphActivity));

        Log.d(TAG, "localFragmentActivity is instanceof InjectableActivity "
                + (localFragmentActivity instanceof InjectableActivity));
//        if ((localFragmentActivity instanceof ObjectGraphActivity)) {
            Log.d(TAG, "Injecting Fragment");
            ((InjectableActivity)localFragmentActivity).inject(this);

        super.onActivityCreated(paramBundle);

//        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                startActivity(new Intent(getActivity(), PrefsActivity.class));
                return true;
            case R.id.action_licenses:
                startActivity(new Intent(getActivity(), LicenseActivity.class));
                return true;

            case R.id.action_tutorial:
                Intent tutorialIntent = new Intent(getActivity(), TutorialActivity.class);
//                Bundle args = new Bundle();
//                args.putBoolean("fromLauncher", false);
                tutorialIntent.putExtra("fromLauncher", false);
                startActivity(tutorialIntent);
                return true;

            case R.id.action_logout:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d(TAG, "logging out from parse");
                        clearUserInfo();
                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }
    /*
     * clears the user information...
     */
    private void clearUserInfo() {
        Log.d(TAG, "clearUserInfo");
        preferences.edit().putString("name", "").apply();
        preferences.edit().putString("imageUri", "").apply();
        preferences.edit().putString("baby_sex","").apply();
        preferences.edit().putString("dob","").apply();
        AppUtils.invalidateDiaperChangeCaches(getActivity().getApplicationContext());
        AppUtils.invalidateSleepChangeCaches(getActivity().getApplicationContext());
//        preferences.edit().putString(DiaperChangeStatsType.WEEKLY.getValue(), "").apply();
//        preferences.edit().putString(DiaperChangeStatsType.MONTHLY.getValue(), "").apply();
//        preferences.edit().putString(DiaperChangeStatsType.YEARLY.getValue(), "").apply();
        try {
            ParseObject.unpinAll();
        } catch (ParseException e) {
            Log.d(TAG, "ParseException " + e);
            e.printStackTrace();
        }
        //clear the caches of the stored results...
//        ParseQuery.clearAllCachedResults();
    }

    /*
     * called when the fragment comes back on the top on pop from the fragment stack..
     */
    public void onFragmentResume() {
        Log.d(TAG, "onFragment Resume ");
    }

    /*
   * closes the keyboard
   */
    protected void closeSoftKeyBoard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.soft
        if (getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus().getWindowToken()!=null) {
            Log.d(TAG, "closing the window");
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

    }


    protected boolean isValidEmail(final CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


}
