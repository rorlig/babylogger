package com.rorlig.babyapp.ui.fragment.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.camera.CropImageIntentBuilder;
import com.desmond.squarecamera.CameraActivity;
import com.gc.materialdesign.views.Button;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.otto.CroppedImageEvent;
import com.rorlig.babyapp.otto.GalleryEvent;
import com.rorlig.babyapp.otto.events.camera.CameraStartEvent;
import com.rorlig.babyapp.otto.events.camera.PictureSelectEvent;
import com.rorlig.babyapp.otto.events.profile.SavedProfileEvent;
import com.rorlig.babyapp.otto.events.profile.SkipProfileEvent;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.Baby;
import com.rorlig.babyapp.ui.PictureInterface;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.rorlig.babyapp.utils.AppUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/14/14.
 * fragment for baby ic_profile.
 */
public class ProfileFragment extends InjectableFragment implements PictureInterface {

    private static final int RESULT_CROP_IMAGE = 3;


    @ForActivity
    @Inject
    Context context;

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.baby_name)
    TextView babyNameTextView;

    @InjectView(R.id.baby_sex)
    RadioGroup babySexRadioGroup;

    @InjectView(R.id.babyGirl)
    RadioButton babyGirlButton;

    @InjectView(R.id.babyBoy)
    RadioButton babyBoyButton;


    @InjectView(R.id.date_picker_birthday)
    DatePicker datePickerBirthday;

    @InjectView(R.id.baby_pic)
    ImageView babyPicImageView;

//    @InjectView(R.id.add_picture)
//    RelativeLayout addPictureLayout;

    @InjectView(R.id.add_image_button)
    android.widget.Button addButton;

    @InjectView(R.id.save_btn)
    Button saveBtn;

    @Inject
    Picasso picasso;

    @InjectView(R.id.skip_btn)
    Button skipBtn;
//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;


//    Typeface typeface;

    private String TAG = "ProfileFragment";

    private EventListener eventListener = new EventListener();
    private PictureSourceSelectFragment pictureSourceSelectFragment;


    private int BITMAP_MAX_HEIGHT = 256;
    private int BITMAP_MAX_WIDTH = 256;
    private Uri imageUri;
    private File croppedImageFile;
    private Uri croppedImage;

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        Log.d(TAG, "onActivityCreated");

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Baby");

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object!=null) {
                    Baby baby = (Baby) object;
                    babyNameTextView.setText(baby.getName());

                }
            }
        });


//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Profile "));

//        String babyName = preferences.getString("name","");
//        if (babyName.length()==0) {
//            saveBtn.setEnabled(false);
//        }
//        babyNameTextView.setText(babyName);

        if (pictureSourceSelectFragment!=null && pictureSourceSelectFragment.isVisible()){
            pictureSourceSelectFragment.dismiss();
        }

        babyNameTextView.addTextChangedListener(new TextWatcher() {
//            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
//                String str = babyNameTextView.getText().toString();
//                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = babyNameTextView.getText().toString();

//                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);



                if (str.length()>0) {
                    saveBtn.setEnabled(true);
                } else {
                    saveBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });

        String babySex = preferences.getString("baby_sex","male");
        if (babySex.equals("male")){
            babyBoyButton.setChecked(true);
        } else {
            babyGirlButton.setChecked(true);
        }
        datePickerBirthday.setMaxDate(Calendar.getInstance().getTimeInMillis());
        String dob = preferences.getString("dob", "");
        if (!dob.equals("")){
            String[] dateElements = dob.split(",");
            Log.d(TAG,"" + dateElements.length);
            Log.d(TAG, dateElements[0]);

            try {
                int year = Integer.parseInt(dateElements[0]);
                int month = Integer.parseInt(dateElements[1]);
                int day = Integer.parseInt(dateElements[2]);
                Log.d(TAG, " year "  + year + " month " + month + " day " + day);
                datePickerBirthday.updateDate(year, month, day);
            } catch (NumberFormatException ex){
                Log.e(TAG, "NumberFormatException " + ex);
            }



        }

        String imageString = preferences.getString("imageUri", "");
        Log.d(TAG, " imageUri " + imageString);
//
        if (!imageString.equals("")){
            Log.d(TAG, "setting imageString into babyPic");
            imageUri = Uri.parse(imageString);

            updateImageUri(imageString);
//            picasso.load(imageUri)
//                    .fit()
//                    .transform(new CircleTransform())
//                    .into(babyPicImageView);

            addButton.setText("Change Picture");
        } else {
           resetImageView();
        }

        if (getArguments()!=null) {
            Log.d(TAG, "arguments is not null " + getArguments());
           if (getArguments().getBoolean("from_tutorial")) {
                Log.d(TAG, "from_tutorial");
                skipBtn.setVisibility(View.GONE);
           }
        } else  {
            Log.d(TAG, "getArguments is null");
        }
    }

    private void resetImageView() {
        babyPicImageView.setImageURI(null);

        babyPicImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_action_profile));

        addButton.setVisibility(View.VISIBLE);

        addButton.setText("Add Picture");


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view =  inflater.inflate(R.layout.fragment_profile, null);
        ButterKnife.inject(this, view);
        return view;
    }




    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void pictureSelectedEvent(PictureSelectEvent event) {
            Log.d(TAG, "pictureSelectEvent");
            updateImageUri(event.getImageUri().toString());
        }

        @Subscribe
        public void onCameraEvent(CameraStartEvent event) {
            handleCameraEvent();
        }

        @Subscribe
        public void onCroppedImageEvent(CroppedImageEvent event) {
            Log.d(TAG, "onCroppedImageEvent " + event.getImageUri());
        }

        @Subscribe
        public void onGalleryEvent(GalleryEvent event) {
            handleGalleryEvent();
        }
    }

    @Override
    public void handleGalleryEvent() {
        Log.d(TAG, "onGalleryEvent");
        Log.d(TAG, " getActivity " + getActivity());
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(intent, AppUtils.RESULT_LOAD_IMAGE);
    }

    @Override
    public void handleCameraEvent() {
        Log.d(TAG, "onCameraEvent");
        long callTime = System.currentTimeMillis();
        String dir = AppUtils.getCameraDirectory();
        File file = new File(dir, callTime + ".jpg");
        Uri imageUri = Uri.fromFile(file);
        Log.d(TAG, "imageUri " + imageUri);
//            scopedBus.post(new CameraStartEvent(imageUri));
        Log.d(TAG, " getActivity " + getActivity());
        Intent startCustomCameraIntent = new Intent(getActivity(), CameraActivity.class);
        startCustomCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            dismiss();
        startActivityForResult(startCustomCameraIntent, AppUtils.RESULT_CAMERA_IMAGE_CAPTURE);
    }


    public void deleteImage() {
//        preferences.edit().putString("imageUri", "").apply();
        imageUri = null;
        resetImageView();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.d("on result:", "onActivityResult:" + resultCode + " request:" + requestCode + " data " + data);
        //Request was successful
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AppUtils.RESULT_LOAD_IMAGE:
                    imagePicked(data);
                    break;
                case AppUtils.RESULT_CAMERA_IMAGE_CAPTURE:
                    cameraImageCaptured(data);
                    break;
                case RESULT_CROP_IMAGE:
                    Log.d(TAG, "croppedImage URI " + croppedImage);
                    updateImageUri(croppedImage.toString());
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }


    @OnClick(R.id.add_image_button)
    public void setBabyImageButtonClicked(){
        imageClicked();

    }

    @OnClick(R.id.baby_pic)
    public void setBabyPicImageViewClicked(){
        imageClicked();
    }


    private void imageClicked() {
        pictureSourceSelectFragment = new PictureSourceSelectFragment();
        pictureSourceSelectFragment.setTargetFragment(this,1);
        pictureSourceSelectFragment.show(getActivity().getSupportFragmentManager(), "picture_select_fragment");

    }



    @OnClick(R.id.save_btn)
    public void saveBtnClicked() {
        Log.d(TAG, "saveBtnClicked()");
        String babyName = babyNameTextView.getText().toString();
        if (babyName.trim().equals("")) {
            babyNameTextView.setError("Name cannot be blank");
        } else {
            preferences.edit().putString("name", babyName).apply();
            if (babySexRadioGroup.getCheckedRadioButtonId()==R.id.babyBoy) {
                preferences.edit().putString("baby_sex", "male").apply();
            } else  {
                preferences.edit().putString("baby_sex", "female").apply();
            }
            int year = datePickerBirthday.getYear();
            int month = datePickerBirthday.getMonth();
            int day = datePickerBirthday.getDayOfMonth();

            String dob = year  + "," + month + "," + day;

            Log.d(TAG, "dob: " + dob);


            preferences.edit().putString("dob", dob).apply();
            saveImageUri(imageUri);
            scopedBus.post(new SavedProfileEvent());


            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Baby");

            Baby baby = new Baby(babyName,dob, imageUri==null?"":imageUri.toString(), null);

            baby.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.d(TAG, "baby saved");
                }
            });



        }








    }

    @OnClick(R.id.skip_btn)
    public void skipBtnClicked(){
        Log.d(TAG, "skipBtnClicked()");
        scopedBus.post(new SkipProfileEvent());
//        scopedBus.post(new S);
    }

    /*
    * Register to events...
    */
    @Override
    public void onStart(){
        super.onStart();
        scopedBus.register(eventListener);

        Log.d(TAG, "onStart");





    }


    private void cameraImageCaptured(Intent data) {

        Log.d(TAG, "cameraImageCaptured : " + data.getData());

        Uri returnedUri;

        if(data != null) {
            returnedUri = data.getData();

            if(returnedUri != null) {

                Log.d(TAG, "imageUri " + imageUri);

                croppedImageFile = new File(getActivity().getFilesDir(), "test_" + System.currentTimeMillis() + "_.jpg");

                croppedImage = Uri.fromFile(croppedImageFile);

                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, croppedImage);
                cropImage.setOutlineColor(0xFF03A9F4);
                cropImage.setSourceImage(returnedUri);

                Log.d(TAG, "cropping image");
                startActivityForResult(cropImage.getIntent(getActivity()), RESULT_CROP_IMAGE);



            }
        }



//        updateImageUri(imageUri.toString());

    }


    private void imagePicked(Intent data) {
        Log.d(TAG, "imagePicked");
//            InCallAnalyticsData.getInstance().trackAnalyticsData(AnalyticsStatEvent.UIActionShare.SHARE_GALLERY);
        if(data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                if(selectedImage != null)
                    cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if(cursor != null)
                    cursor.moveToFirst();



                Log.d(TAG, "selectedImage " + selectedImage);

                croppedImageFile = new File(getActivity().getFilesDir(), "test_" + System.currentTimeMillis() + "_.jpg");

                croppedImage = Uri.fromFile(croppedImageFile);

                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, croppedImage);
                cropImage.setOutlineColor(0xFF03A9F4);
                cropImage.setSourceImage(selectedImage);

                Log.d(TAG, "cropping image");
                startActivityForResult(cropImage.getIntent(getActivity()), RESULT_CROP_IMAGE);


//                imageUri = selectedImage;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                if(cursor != null)
                    cursor.close();
            }
        }
    }

    private void updateImageUri(String imageString){
        Log.d(TAG, "updateImageUri " + imageString);
        if (!imageString.equals("")){

            imageUri = Uri.parse(imageString);
            Log.d(TAG, "update the image " + imageUri.toString());
//            babyPicImageView.set
//            picasso.load(imageUri)
//                    .fit()
//                    .transform(new CircleTransform())
//                            .into(babyPicImageView);
            babyPicImageView.setImageURI(null);
            babyPicImageView.setImageURI(imageUri);
            addButton.setText("Change Picture");
//            addButton.setVisibility(View.GONE);
        }
    }


    private void saveImageUri(Uri imageUri) {
        Log.d(TAG, "saveImageUri " + imageUri);
        if (imageUri!=null)
        preferences.edit().putString("imageUri", imageUri.toString()).apply();
        else preferences.edit().putString("imageUri", "").apply();
    }

}
