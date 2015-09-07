package com.rorlig.babyapp.ui.fragment.invite;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rorlig.babyapp.BabyLoggerApplication;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author gaurav gupta
 */
public class AddProviderFragment extends InjectableFragment {

    @InjectView(R.id.email_text_input_layout)
    TextInputLayout emailTextInputLayout;

    @InjectView(R.id.email)
    EditText emailEditText;
    private String TAG = "AddProviderFragment";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailTextInputLayout.setErrorEnabled(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_provider, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.btn_add_provider)
    public void onProviderBtnClicked() {
        String email = emailEditText.getText().toString();

        if (email == null) {
            emailTextInputLayout.setError(getString(R.string.no_email));
            return;
        }

        if (!isValidEmail(email)) {
            emailTextInputLayout.setError(getString(R.string.not_valid_email));
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        ParseCloud.callFunctionInBackground("inviteUser",
                params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        Log.d(TAG,"object " + object + " e " + e);
                        if (e == null) {
//                            HashMap mp = (HashMap) object;
//                            Log.d(TAG, "object " + object);
                            ParseUser user = (ParseUser) object;
                            BabyLoggerApplication application = (BabyLoggerApplication) getActivity().getApplication();
//                            application.getRole().getUsers().add(user);
//                            application.getRole().saveInBackground(new SaveCallback() {
//                                @Override
//                                public void done(ParseException e) {
//                                    Log.d(TAG, "e " + e);
//                                }
//                            });
                        }
                    }
                });
    }


}
