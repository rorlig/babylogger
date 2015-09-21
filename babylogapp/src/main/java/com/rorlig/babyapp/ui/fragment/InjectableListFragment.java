package com.rorlig.babyapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;

import com.rorlig.babyapp.dagger.ObjectGraphActivity;
import com.rorlig.babyapp.otto.ScopedBus;
import com.rorlig.babyapp.ui.activity.InjectableActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by admin on 12/15/13.
 */
public class InjectableListFragment extends ListFragment {

    private static final String TAG = "InjectableFragment";

    @Inject
    public ScopedBus scopedBus;

    public InjectableListFragment() {
    }

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







    //    @Override
//    public void onViewCreated(View paramView, Bundle paramBundle)
//    {
//        Views.inject(this, paramView);
//    }

}
