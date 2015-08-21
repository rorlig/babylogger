package com.rorlig.babyapp.ui.fragment.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.auth.ForgotBtnClickedEvent;
import com.rorlig.babyapp.otto.auth.SignupBtnClickedEvent;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.rorlig.babyapp.utils.AppUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author gaurav gupta
 */
public class LoginFragment extends InjectableFragment {

    @InjectView(R.id.btn_forgot_password)
    Button forgotPasswordBtn;

    @InjectView(R.id.btn_signup)
    Button signupBtn;
    private String TAG = "LoginFragment";


    @InjectView(R.id.skip_message)
    TextView skipMessageTextView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        skipMessageTextView.setText(AppUtils.getSpannable(skipMessageTextView.getText().toString()));
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.btn_forgot_password)
    public void forgotBtnClicked(){
        Log.d(TAG, "forgotBtnClicked");
        scopedBus.post(new ForgotBtnClickedEvent());
    }

    @OnClick(R.id.btn_signup)
    public void btnSignUpClicked(){
        Log.d(TAG, "btnSignUpClicked");

        scopedBus.post(new SignupBtnClickedEvent());


    }

    @OnClick(R.id.btn_login)
    public void btnLoginClicked(){

    }
}
