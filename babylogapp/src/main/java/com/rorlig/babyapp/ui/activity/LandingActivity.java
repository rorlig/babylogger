package com.rorlig.babyapp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.parse.ParseUser;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.utils.AppConstants;

import javax.inject.Inject;

//import com.fourmob.datetimepicker.date.DatePickerDialog;

public class LandingActivity extends InjectableActivity {



    @Inject
    SharedPreferences preferences;

    @Inject
    Gson gson;

    private String TAG ="LandingActivity";

//    private EventListener eventListener = new EventListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        boolean tutorial_shown = preferences.getBoolean(getString(R.string.tutorial_shown), false);

        boolean profile_created = false;

        String babyName = preferences.getString("name", "");
        if (babyName.length()>0) {
            profile_created = true;
        }


        if (!tutorial_shown) {
            startActivity(new Intent(this, TutorialActivity.class));
        }
        else if (ParseUser.getCurrentUser()==null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.putExtra(getString(R.string.from_tutorial), true);
            startActivity(loginIntent);
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }

    }




}
