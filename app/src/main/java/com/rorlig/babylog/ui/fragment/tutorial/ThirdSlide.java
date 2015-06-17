package com.rorlig.babylog.ui.fragment.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rorlig.babylog.R;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

/**
 * Created by rorlig on 6/16/15.
 *  * Tutorial Slide # 3

 */
public class ThirdSlide extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.intro3, container, false);
        return v;
    }
}
