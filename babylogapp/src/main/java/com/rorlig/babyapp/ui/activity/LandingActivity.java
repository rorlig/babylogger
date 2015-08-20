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

        boolean profile_created = false;

        String babyName = preferences.getString("name", "");
        if (babyName.length()>0) {
            profile_created = true;
        }


        if (!tutorial_shown) {
            startActivity(new Intent(this, TutorialActivity.class));
        } else if (!profile_created) {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra("from_tutorial", true);
            startActivity(profileIntent);
        }
        else {
            startActivity(new Intent(this, HomeActivity.class));
        }

    }




}
