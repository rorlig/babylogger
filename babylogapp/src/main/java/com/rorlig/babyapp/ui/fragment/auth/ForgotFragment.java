package com.rorlig.babyapp.ui.fragment.auth;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.rorlig.babyapp.utils.AppUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author gaurav gupta
 */
public class ForgotFragment extends InjectableFragment {

    @InjectView(R.id.email_text_input_layout)
    TextInputLayout emailTextInputLayout;

    @InjectView(R.id.email)
    EditText emailEditText;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailTextInputLayout.setErrorEnabled(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_forgot_password, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.btn_reset)
    public void onResetBtnClicked(){
        if (!AppUtils.isNetworkAvailable(getActivity())){
            showErrorIfNotConnected();
        } else {
            String email = emailEditText.getText().toString();

            if (email == null) {
                emailTextInputLayout.setError(getString(R.string.no_email));
                return;
            }

            if (!isValidEmail(email)) {
                emailTextInputLayout.setError(getString(R.string.not_valid_email));
                return;
            }

            ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                @Override
                public void done(ParseException e) {
                    if (e!=null) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    } else  {
                        Toast.makeText(getActivity(), R.string.txt_reset_request, Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }


}
