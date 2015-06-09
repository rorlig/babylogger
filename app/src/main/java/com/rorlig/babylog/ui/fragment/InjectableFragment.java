package com.rorlig.babylog.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.style.ScaleXSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ObjectGraphActivity;
import com.rorlig.babylog.otto.ScopedBus;
import com.rorlig.babylog.ui.activity.InjectableActivity;

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
        setHasOptionsMenu(true);


    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.add_item, menu);
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







    //    @Override
//    public void onViewCreated(View paramView, Bundle paramBundle)
//    {
//        Views.inject(this, paramView);
//    }

}
