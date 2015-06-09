package com.rorlig.babylog.ui.fragment.about;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by rorlig on 7/14/14.
 */
public class AboutFragment extends InjectableFragment {
    @ForActivity
    @Inject
    Context context;

//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;


    Typeface typeface;

    private String TAG = "AboutFragment";

    private EventListener eventListener = new EventListener();

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        typeface=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("About"));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diaper_change, null);
        ButterKnife.inject(this, view);
        return view;
    }


    private class EventListener {
        public EventListener() {

        }
    }
}
