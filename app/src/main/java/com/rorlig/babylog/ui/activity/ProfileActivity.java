package com.rorlig.babylog.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.camera.CropImageIntentBuilder;
import com.google.gson.Gson;
import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.CroppedImageEvent;
import com.rorlig.babylog.otto.events.camera.CameraStartEvent;
import com.rorlig.babylog.otto.events.camera.PictureSelectEvent;
import com.rorlig.babylog.otto.events.profile.SavedProfileEvent;
import com.rorlig.babylog.otto.events.profile.SkipProfileEvent;
import com.rorlig.babylog.scheduler.TypeFaceManager;
import com.rorlig.babylog.ui.fragment.profile.ProfileFragment;
import com.rorlig.babylog.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.io.File;

import javax.inject.Inject;

//import com.rorlig.babylog.common.AppConstants;
//import com.rorlig.babylog.otto.events.filter.DistanceFilterChanged;
//import com.rorlig.babylog.otto.events.filter.FilterChangedEvent;
//import com.rorlig.babylog.otto.events.filter.LocationChangedEvent;
//import com.rorlig.babylog.otto.events.ui.EventSelectedEvent;
//import com.rorlig.babylog.otto.events.ui.MenuItemSelectedEvent;
//import com.rorlig.babylog.otto.events.ui.TimeFilterChanged;
//import com.rorlig.babylog.ui.fragment.ConnectionsFragment;
//import com.rorlig.babylog.ui.fragment.LoggingFragment;
//import com.rorlig.babylog.ui.fragment.MessagesFragment;
//import com.rorlig.babylog.ui.fragment.MyEventsFragment;

/**
 * Created by admin on 12/15/13.
 */
public class ProfileActivity extends InjectableActivity {
    private static final int RESULT_CROP_IMAGE = 3;

    //todo still figure out left + right toggle speeds...


    @Inject
    ActionBar actionBar;

    @Inject
    Gson gson;

    @Inject
    TypeFaceManager typeFaceManager;

    @Inject
    SharedPreferences preferences;




    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String TAG  = "ProfileActivity";

    private EventListener eventListener = new EventListener();
    public static Uri imageUri;
    private File croppedImageFile;
    private Uri croppedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
//        setContentView(R.layout.activity_profile);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showFragment(ProfileFragment.class, "profile_fragment", false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the option menu items..
//        getMenuInflater().inflate(R.menu.menu_add_item, menu);

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        switch (item.getItemId()){

            case R.id.action_licenses:
                break;


        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
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

    private void startActivity(Class<?> paramClass,String paramString){
        Intent intent = new Intent(this, paramClass);
        startActivity(intent);
    }

    private class EventListener {
        private EventListener(){
        }



        @Subscribe
        public void onSaveProfileEvent(SavedProfileEvent event) {

            finish();
        }

        @Subscribe
        public void onSkipProfileEvent(SkipProfileEvent event){
            finish();
        }

        @Subscribe
        public void onCameraEvent(CameraStartEvent event) {
            Log.d(TAG, "onCameraEvent " + event.getImageUri());

            imageUri = event.getImageUri();
        }

    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving event list");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("on result:", "onActivityResult:" + resultCode + " request:" + requestCode  + " data " + data );

       super.onActivityResult(requestCode, resultCode, data);

//        Log.d("on result:", "onActivityResult:" + resultCode + " request:" + requestCode + " data " + data);
//        //Request was successful
//        if (resultCode == RESULT_OK) {
////                mBus.post(new ShareRequestEvent(AnalyticsStatEvent.UIActionShare.SHARE_CAMERA));
//
//            switch (requestCode) {
//
//                case AppUtils.RESULT_LOAD_IMAGE:
//                    imagePicked(data);
//
//                    break;
//                case AppUtils.RESULT_CAMERA_IMAGE_CAPTURE:
//                    cameraImageCaptured(data);
//                    break;
//                case RESULT_CROP_IMAGE:
//                    Log.d(TAG, "croppedImage URI " + croppedImage);
//                    imageUri = croppedImage;
//                    preferences.edit().putString("imageUri", imageUri.toString()).apply();
//                    scopedBus.post(new CroppedImageEvent(imageUri.toString()));
//                    break;
//                default:
//                    super.onActivityResult(requestCode, resultCode, data);
//                    break;
//            }
//        }
    }


//    private void cameraImageCaptured(Intent data) {
//
//        Log.d(TAG, "cameraImageCaptured : " + data.getData());
//
//        Uri returnedUri;
//
//        if(data != null) {
//            returnedUri = data.getData();
//
//            if(returnedUri != null) {
//                imageUri = returnedUri;
//
//            }
//        }
//
//        Log.d(TAG, "imageUri " + imageUri);
//        preferences.edit().putString("imageUri", imageUri.toString()).apply();
//
//        scopedBus.post(new PictureSelectEvent(imageUri));
//    }
//
//
//    private void imagePicked(Intent data) {
//        Log.d(TAG, "imagePicked");
////            InCallAnalyticsData.getInstance().trackAnalyticsData(AnalyticsStatEvent.UIActionShare.SHARE_GALLERY);
//        if(data != null) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = null;
//            try {
//                if(selectedImage != null)
//                    cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                if(cursor != null)
//                    cursor.moveToFirst();
//
//
//
//                Log.d(TAG, "selectedImage " + selectedImage);
//
//                croppedImageFile = new File(getFilesDir(), "test_" + System.currentTimeMillis() + "_.jpg");
//
//                croppedImage = Uri.fromFile(croppedImageFile);
//
//                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, croppedImage);
//                cropImage.setOutlineColor(0xFF03A9F4);
//                cropImage.setSourceImage(selectedImage);
//
//                Log.d(TAG, "cropping image");
//                startActivityForResult(cropImage.getIntent(this), RESULT_CROP_IMAGE);
//
//
////                imageUri = selectedImage;
//            }
//            catch(Exception e) {
//                e.printStackTrace();
//            }
//            finally {
//                if(cursor != null)
//                    cursor.close();
//            }
//        }
//    }



}
