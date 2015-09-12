package com.rorlig.babyapp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.model.ItemModel;
import com.rorlig.babyapp.otto.CreatedUser;
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
import java.util.List;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;

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
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        public void onLoginSuccessEvent(final LoginSuccessEvent event){

            final ParseQuery<ParseObject> heightQuery = ParseQuery.getQuery("HeightToAge");
            final ParseQuery<ParseObject> weightQuery = ParseQuery.getQuery("WeightToAge");
            final ParseQuery<ParseObject> headMeasurementQuery = ParseQuery.getQuery("HeadCircumferenceToAge");
            final ParseQuery query = ParseQuery.getQuery("Baby");

            heightQuery.findInBackground(new FindCallback<ParseObject>() {
                 @Override
                 public void done(List<ParseObject> objects, ParseException e) {
                     Log.d(TAG, "here" + objects);
                     ParseObject.pinAllInBackground("HeightToAge", objects);

                 }
             });

            weightQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    Log.d(TAG, "here" + objects);
                    ParseObject.pinAllInBackground("WeightToAge", objects);

                }
            });

            headMeasurementQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    Log.d(TAG, "here" + objects);
                    ParseObject.pinAllInBackground(objects);

                }
            });


            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (object == null) {
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    } else {
                        saveToPreferences(object);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                }
            });


//            heightQuery.findInBackground().onSuccess(new Continuation<List<ParseObject>, Task<ParseObject>>() {
//                @Override
//                public Task<ParseObject> then(Task<List<ParseObject>> task) throws Exception {
//                        return null;
//                }
//            }).onSuccess(new Continuation<Task<ParseObject>, Object>() {
//                @Override
//                public Object then(Task<Task<ParseObject>> task) throws Exception {
//                    weightQuery.findInBackground();
//                    return null;
//                }
//            }).onSuccess(new Continuation<Object, Object>() {
//                @Override
//                public Object then(Task<Object> task) throws Exception {
//                    return null;
//                }
//            })



//            weightQuery.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List objects, ParseException e) {
//                    if (e != null) {
//                        Log.d(TAG, "objects " + objects);
////                        ParseObject.pinAllInBackground("WeightToAge", objects);
//
//                        heightQuery.findInBackground(new FindCallback<ParseObject>() {
//                            @Override
//                            public void done(List<ParseObject> objects, ParseException e) {
//                                if (e != null) {
//                                    Log.d(TAG, "height objects " + objects);
////                                    ParseObject.pinAllInBackground("HeightToAge", objects);
//                                    headMeasurementQuery.findInBackground(new FindCallback<ParseObject>() {
//                                        @Override
//                                        public void done(List<ParseObject> objects, ParseException e) {
//                                            Log.d(TAG, "head measurement objects " + objects);
////                                            ParseObject.pinAllInBackground("HeadCircumferenceToAge", objects);
//                                            query.getFirstInBackground(new GetCallback<ParseObject>() {
//                                                @Override
//                                                public void done(ParseObject object, ParseException e) {
//                                                    if (object == null) {
//                                                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
//                                                    } else {
//                                                        saveToPreferences(object);
//                                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                                                    }
//                                                }
//                                            });
//
//                                        }
//                                    });
//                                }
//                            }
//                        });
//
//                    }
//                }
//            });


//
//            headMeasurementQuery.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List objects, ParseException e) {
//                    if (e != null) {
//                        Log.d(TAG, "objects " + objects);
//                        ParseObject.pinAllInBackground("HeadCircumferenceToAge", objects);
//
//                    }
//                }
//            });
//





        }

        @Subscribe
        public void onUserCreatedEvent(CreatedUser event){
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
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
