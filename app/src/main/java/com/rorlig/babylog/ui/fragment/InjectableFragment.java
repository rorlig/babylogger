package com.rorlig.babylog.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ObjectGraphActivity;
import com.rorlig.babylog.otto.ScopedBus;
import com.rorlig.babylog.ui.activity.ExportActivity;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.rorlig.babylog.ui.activity.LicenseActivity;
import com.rorlig.babylog.ui.activity.PrefsActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by admin on 12/15/13.
 */
public class InjectableFragment extends Fragment {

    private static final String TAG = "InjectableFragment";

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
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * called when the fragment comes back on the top on pop from the fragment stack..
     */
    public void onFragmentResume() {

        Log.d(TAG, "onFragment Resume ");

    }


}
