package com.rorlig.babyapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ActivityModule;
import com.rorlig.babyapp.dagger.ObjectGraphActivity;
import com.rorlig.babyapp.dagger.ObjectGraphUtils;
import com.rorlig.babyapp.otto.ScopedBus;
import com.rorlig.babyapp.otto.UpdateActionBarEvent;
import com.rorlig.babyapp.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

//import butterknife.;

/**
 * Created by admin on 12/14/13.
 * @author gaurav gupta
 *
 *
 */
public class InjectableActivity extends AppCompatActivity implements ObjectGraphActivity
//        , GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
    {

    private ObjectGraph activityGraph;

    @Inject
    public ScopedBus scopedBus;

    @Inject
    public SharedPreferences preferences;

    private String TAG = "InjectableActivity";

    private TextView titleTextView;


    ImageView profileImageIcon;

    TextView babyNameTextView;

    TextView babyAgeTextView;


        @Override
    public void inject(Object paramObject) {
       Log.d(TAG, "injecting " + paramObject);
       activityGraph.inject(paramObject);
    }


    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
        updateToolbar();
        scopedBus.register(this);
    }

    @Override
    public void onStop(){
        super.onStop();

        Log.d(TAG, "onStop");
        scopedBus.unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_base);


        activityGraph = ObjectGraphUtils.getApplicationGraph(this).plus(getModules().toArray());
        activityGraph.inject(this);
        setUpViews();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityGraph = null;
    }

    @Override
    protected void onPause() {

        Log.d(TAG, "onPause()");
        super.onPause();
        this.scopedBus.paused();
    }

    @Override
    protected void onResume(){
        Log.d(TAG, "onResume()");
        super.onResume();
        scopedBus.resumed();
    }


    protected List<Object> getModules(){
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = new ActivityModule(this);
//        arrayOfObject[1] = new SocialActivityModule(this, this, this);
        return Arrays.asList(arrayOfObject);
    }
    public void onContentChanged(){
        super.onContentChanged();
        ButterKnife.inject(this);
    }


    public ObjectGraph getActivityGraph() {
        return activityGraph;
    }

    public void setActivityGraph(ObjectGraph activityGraph) {
        this.activityGraph = activityGraph;
    }

    public ScopedBus getScopedBus() {
        return scopedBus;
    }

    public void setScopedBus(ScopedBus scopedBus) {
        this.scopedBus = scopedBus;
    }


    protected void showFragment(Class<?> paramClass,String paramString, boolean addToStack ){
        Log.d(TAG, "showFragment for " + paramClass);

        FragmentManager localFragmentManager = getSupportFragmentManager();



        Fragment localFragment = localFragmentManager.findFragmentById(R.id.fragment_container);

        if ((localFragment==null)||(!paramClass.isInstance(localFragment))){
            Log.d(TAG, "adding to back stack ");
            try {
                localFragment = (Fragment)paramClass.newInstance();
                if (addToStack) {
                    localFragmentManager.beginTransaction()
                            .add(R.id.fragment_container, localFragment)
                            .addToBackStack("main_screen_stack")
                            .commit();
                } else {
                    localFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, localFragment)
                            .commit();
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== AppUtils.PROFILE_ACTIVITY) {
            Log.d(TAG, "result from profile activity");
//            Log.d(TAG, "profile changes " + data.getBooleanExtra("saved_profile", false));


            updateToolbar();
        }
    }

    protected void setToolbarIconVisibility(boolean isVisible) {
        if (!isVisible)
            profileImageIcon.setVisibility(View.GONE);
        else
            profileImageIcon.setVisibility(View.VISIBLE);
    }


    private void setUpViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        profileImageIcon = (ImageView) toolbar.findViewById(R.id.icon_image);
        titleTextView = (TextView) toolbar.findViewById(R.id.title);
//        babyAgeTextView = (TextView) toolbar.findViewById(R.id.baby_age);
        babyNameTextView  = (TextView) toolbar.findViewById(R.id.baby_name);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final String imageUri = preferences.getString("imageUri", "");
        final String babyName = preferences.getString("name", "");
//        final String dob = preferences.getString("dob", "");

        Log.d(TAG, "imageUri " + imageUri + " babyName " + babyName);

//        if (!babyName.equals("")) {
//            babyNameTextView.setText(babyName);
//        }

//        if (!dob.equals("")) {
//            babyAgeTextView.setText(dobString(dob));
//        }
        if (!imageUri.equals("")) {
            Log.d(TAG, "setting the profile image");
            profileImageIcon.setImageURI(Uri.parse(imageUri));
        }

        profileImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), ProfileActivity.class), AppUtils.PROFILE_ACTIVITY);

            }
        });
        titleTextView.setText(getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_tutorial:
                Intent tutorialIntent = new Intent(this, TutorialActivity.class);
//                Bundle args = new Bundle();
//                args.putBoolean("fromLauncher", false);
                tutorialIntent.putExtra("fromLauncher", false);
                startActivity(tutorialIntent);
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, PrefsActivity.class));
                return true;
            case R.id.action_export:
                startActivity(new Intent(this, ExportActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateToolbar() {
        final String imageUri = preferences.getString("imageUri", "");

        final String babyName = preferences.getString("name", "");
//        final String dob = preferences.getString("dob", "");

        Log.d(TAG, "imageUri " + imageUri);
        if (!imageUri.equals("")) {
            profileImageIcon.setImageURI(Uri.parse(imageUri));
        } else {
            profileImageIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baby_profile));
        }

//        if (!babyName.equals("")) {
//            babyNameTextView.setText(babyName);
//        }

//        if (!dob.equals("")) {
//            babyAgeTextView.setText(dobString(dob));
//        }



    }

//    @Subscribe
//    public void onDiaperLogCreated(DiaperLogCreatedEvent event) {
//        closeSoftKeyBoard();
//
//    }


    @Subscribe
    public void updateActionBar(UpdateActionBarEvent event){
        Log.d(TAG, "updating action bar");
        profileImageIcon.setImageDrawable(event.getDrawable());
    }




     /*
     * closes the keyboard
     */
    protected void closeSoftKeyBoard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.soft
        if (getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null) {
            Log.d(TAG, "closing the window");
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    private String dobString(String dob) {
//        if (!dob.equals("")){
            String[] dateElements = dob.split(",");
            Log.d(TAG,"" + dateElements.length);
            Log.d(TAG, dateElements[0]);
            int year = Integer.parseInt(dateElements[0]);
            int month = Integer.parseInt(dateElements[1]);
            int day = Integer.parseInt(dateElements[2]);
            Log.d(TAG, " year "  + year + " month " + month + " day " + day);
            Calendar c = Calendar.getInstance();
            c.set(year,month,day);
            Log.d(TAG, "time " + c.getTimeInMillis());
            long diff = System.currentTimeMillis() - c.getTimeInMillis();
            long days = diff/(86400*1000);
            Log.d(TAG, "days old " + days);
//        }
        return days + "days old ";
    }
}
