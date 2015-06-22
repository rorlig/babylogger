package com.rorlig.babylog.ui.fragment.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rorlig.babylog.R;

/**
 * Created by rorlig on 6/16/15.
 * Tutorial Slide # 1
 */
public class FirstSlide extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro1, container, false);
    }
}
