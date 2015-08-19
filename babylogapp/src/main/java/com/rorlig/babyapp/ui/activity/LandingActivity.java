package com.rorlig.babyapp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;

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

        boolean tutorial_shown = preferences.getBoolean("tutorial_shown", false);

        if (tutorial_shown) {
            startActivity(new Intent(this, HomeActivity.class));
        }
        else {
            startActivity(new Intent(this, TutorialActivity.class));
        }

    }




}
