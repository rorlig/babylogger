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
import com.parse.SignUpCallback;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author gaurav gupta
 */
public class SignUpFragment extends InjectableFragment {

    @InjectView(R.id.first_name_text_input_layout)
    TextInputLayout firstNameTextInputLayout;

    @InjectView(R.id.last_name_text_input_layout)
    TextInputLayout lastNameTextInputLayout;

    @InjectView(R.id.email_text_input_layout)
    TextInputLayout emailTextInputLayout;

    @InjectView(R.id.password_text_input_layout)
    TextInputLayout passwordTextInputLayout;

    @InjectView(R.id.first_name)
    EditText firstNameEditText;

    @InjectView(R.id.last_name)
    EditText lastNameEditText;

    @InjectView(R.id.email)
    EditText emailEditText;

    @InjectView(R.id.password)
    EditText passwordEditText;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstNameTextInputLayout.setErrorEnabled(true);
        lastNameTextInputLayout.setErrorEnabled(true);
        emailTextInputLayout.setErrorEnabled(true);
        passwordTextInputLayout.setErrorEnabled(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_signup, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.btn_signup)
    public void btnSignUp() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (firstName.length()==0) {
            firstNameTextInputLayout.setError(getString(R.string.no_first_name));
            return;
        }


        if (lastName.length()==0) {
            lastNameTextInputLayout.setError(getString(R.string.no_last_name));
            return;
        }

        if (email.length()==0) {
            emailTextInputLayout.setError(getString(R.string.no_email));
            return;
        }

        if (password.length()==0) {
            passwordTextInputLayout.setError(getString(R.string.no_password));
            return;
        }

        if (!isValidEmail(email)) {
            emailTextInputLayout.setError(getString(R.string.not_valid_email));
            return;
        }

        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Created User", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.txt_login)
    public void onBtnLoginClicked() {
        getFragmentManager().popBackStackImmediate();
    }

}
