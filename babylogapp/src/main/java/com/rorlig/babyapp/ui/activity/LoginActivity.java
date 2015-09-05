package com.rorlig.babyapp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.model.ItemModel;
import com.rorlig.babyapp.otto.auth.ForgotBtnClickedEvent;
import com.rorlig.babyapp.otto.auth.LoginSkippedEvent;
import com.rorlig.babyapp.otto.auth.LoginSuccessEvent;
import com.rorlig.babyapp.otto.auth.SignupBtnClickedEvent;
import com.rorlig.babyapp.parse_dao.Baby;
import com.rorlig.babyapp.ui.fragment.auth.ForgotFragment;
import com.rorlig.babyapp.ui.fragment.auth.LoginFragment;
import com.rorlig.babyapp.ui.fragment.auth.SignUpFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author gaurav gupta
 */

public class LoginActivity extends InjectableActivity {

    @Inject
    SharedPreferences preferences;
    private String TAG = "LoginActivity";
    @Inject
    Gson gson;
    private ArrayList<ItemModel> logs;

    private EventListener eventListener = new EventListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFragment(LoginFragment.class, "login_fragment", false);
    }

    @Override
    public void onStart(){
        super.onStart();
        scopedBus.register(eventListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        scopedBus.unregister(eventListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    private class EventListener {
        private EventListener() {
        }

        @Subscribe
        public void onSignupEvent(SignupBtnClickedEvent signupBtnClickedEvent){
            Log.d(TAG,"onSignupEvent" );
            showFragment(SignUpFragment.class, "login_fragment", true);
        }

        @Subscribe
        public void onForgotEvent(ForgotBtnClickedEvent forgotBtnClickedEvent){
            Log.d(TAG,"onForgotEvent" );
            showFragment(ForgotFragment.class, "forgot_fragment", true);
        }

        @Subscribe
        public void onLoginSuccessEvent(LoginSuccessEvent event){
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Baby");
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (object==null) {
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    } else {
                        saveToPreferences(object);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                }
            });
        }

        @Subscribe
        public void onLoginSkipEvent(LoginSkippedEvent event) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }



    }

    private void saveToPreferences(ParseObject object) {
        final Baby baby  = (Baby) object;
        preferences.edit().putString("name", baby.getName()).apply();
        preferences.edit().putString("dob", baby.getDob()).apply();
        preferences.edit().putString("imageFile", baby.getParseFile()==null?"": baby.getParseFile().getUrl()).apply();
    }
}
