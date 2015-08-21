package com.rorlig.babyapp.ui.fragment.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;

import butterknife.ButterKnife;

/**
 * @author gaurav gupta
 */
public class SignUpFragment extends InjectableFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_signup, null);
        ButterKnife.inject(this, view);
        return view;
    }

}
