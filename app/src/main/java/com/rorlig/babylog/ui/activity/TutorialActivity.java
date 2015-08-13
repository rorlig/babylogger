package com.rorlig.babylog.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.rorlig.babylog.ui.fragment.profile.ProfileFragment;
import com.rorlig.babylog.ui.fragment.tutorial.FirstSlide;
import com.rorlig.babylog.ui.fragment.tutorial.FourthSlide;
import com.rorlig.babylog.ui.fragment.tutorial.SecondSlide;
import com.rorlig.babylog.ui.fragment.tutorial.ThirdSlide;

/**
 * Created by rorlig on 6/16/15.
 */
public class TutorialActivity extends AppIntro {

    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new FirstSlide(), getApplicationContext());
        addSlide(new SecondSlide(), getApplicationContext());
        addSlide(new ThirdSlide(), getApplicationContext());
        addSlide(new FourthSlide(), getApplicationContext());

        // OPTIONAL METHODS
        // Override bar/separator color
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Show Skip button
        showSkipButton(true);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permesssion in Manifest
        setVibrate(false);

        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("tutorial_shown", true).apply();
//        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra("from_tutorial", true);
        startActivity(profileIntent);
    }

    @Override
    public void onDonePressed() {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra("from_tutorial", true);
        startActivity(profileIntent);
    }
}
