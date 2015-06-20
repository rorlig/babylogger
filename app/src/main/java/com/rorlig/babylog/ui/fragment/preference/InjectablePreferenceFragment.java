package com.rorlig.babylog.ui.fragment.preference;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.View;


import com.rorlig.babylog.dagger.ObjectGraphActivity;
import com.rorlig.babylog.otto.ScopedBus;
import com.rorlig.babylog.ui.activity.InjectableActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

//import android.app.FragmentActivity;


/**
 * @author gaurav gupta
 * An injectable preference fragment - allows the extend fragments to control preferences as well as receive and
 * send events via event bus..
 */
public class InjectablePreferenceFragment extends PreferenceFragment {
    private static final String TAG = "InjectableFragment";

    @Inject
    ScopedBus scopedBus;

    public InjectablePreferenceFragment() {
    }

    @Override
    public void onActivityCreated(Bundle paramBundle)
    {
        Activity localFragmentActivity = getActivity();
        Log.d(TAG, "localFragmentActivity " + localFragmentActivity);
        Log.d(TAG, "localFragmentActivity is instanceof ObjectGraphActivity "
                + (localFragmentActivity instanceof ObjectGraphActivity));

        Log.d(TAG, "localFragmentActivity is instanceof InjectableActivity "
                + (localFragmentActivity instanceof InjectableActivity));

        Log.d(TAG, "Injecting Fragment");
        ((InjectableActivity)localFragmentActivity).inject(this);

        super.onActivityCreated(paramBundle);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(view);
    }

}
