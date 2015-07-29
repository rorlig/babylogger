package com.rorlig.babylog.ui.fragment.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.CroppedImageEvent;
import com.rorlig.babylog.otto.GalleryEvent;
import com.rorlig.babylog.otto.events.camera.CameraStartEvent;
import com.rorlig.babylog.otto.events.camera.PictureSelectEvent;
import com.rorlig.babylog.otto.events.profile.SavedProfileEvent;
import com.rorlig.babylog.otto.events.profile.SkipProfileEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.utils.AppUtils;
import com.rorlig.babylog.utils.transform.BlurTransformation;
import com.rorlig.babylog.utils.transform.CircleTransform;
import com.rorlig.babylog.utils.transform.CropTransform;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rorlig on 7/14/14.
 * fragment for baby ic_profile.
 */
public class ProfileFragment extends InjectableFragment {

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

    @InjectView(R.id.save_btn)
    Button saveBtn;

    @Inject
    Picasso picasso;
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


//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Profile "));

        String babyName = preferences.getString("name","");
        if (babyName.length()==0) {
            saveBtn.setEnabled(false);
        }
        babyNameTextView.setText(babyName);

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
            Uri imageUri = Uri.parse(imageString);
            picasso.load(imageUri)
                    .fit()
                    .transform(new CircleTransform())
                    .into(babyPicImageView);
        }

//        String imageStr = preferences.getString("imageUri","");
//        if (!imageStr.equals("")){
//            Uri imageUri = Uri.parse(imageStr);
//
//            babyPicImageView.setImageURI(imageUri);
//
//        }


//        scopedBus.register(eventListener);

        //todo dob...

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
            babyPicImageView.setImageURI(event.getImageUri());
        }

        @Subscribe
        public void onCameraEvent(CameraStartEvent event) {

            Log.d(TAG, "onCameraEvent");
            long callTime = System.currentTimeMillis();
            String dir = AppUtils.getCameraDirectory();
            File file = new File(dir, callTime + ".jpg");
            Uri imageUri = Uri.fromFile(file);
            Log.d(TAG, "imageUri " + imageUri);
//            scopedBus.post(new CameraStartEvent(imageUri));
            Intent startCustomCameraIntent = new Intent(getActivity(), CameraActivity.class);
            startCustomCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            dismiss();
           startActivityForResult(startCustomCameraIntent, AppUtils.RESULT_CAMERA_IMAGE_CAPTURE);

//            imageUri = event.getImageUri();
//            babyPicImageView.setImageURI(event.getImageUri());

        }

        @Subscribe
        public void onCroppedImageEvent(CroppedImageEvent event) {
            Log.d(TAG, "onCroppedImageEvent " + event.getImageUri());
        }

        @Subscribe
        public void onGalleryEvent(GalleryEvent event) {
            Log.d(TAG, "onGalleryEvent");

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivityForResult(intent, AppUtils.RESULT_LOAD_IMAGE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.d("on result:", "onActivityResult:" + resultCode + " request:" + requestCode  + " data " + data );
        //Request was successful
        if (resultCode == Activity.RESULT_OK) {
//                mBus.post(new ShareRequestEvent(AnalyticsStatEvent.UIActionShare.SHARE_CAMERA));

            switch (requestCode) {

                case AppUtils.RESULT_LOAD_IMAGE:
                    imagePicked(data);

                    break;
                case AppUtils.RESULT_CAMERA_IMAGE_CAPTURE:
                    cameraImageCaptured(data);
                    break;
                case RESULT_CROP_IMAGE:
                    Log.d(TAG, "croppedImage URI " + croppedImage);
//                    imageUri = croppedImage;
                    updateImageUri(croppedImage.toString());
//                    preferences.edit().putString("imageUri", imageUri.toString()).apply();
//                    scopedBus.post(new CroppedImageEvent(imageUri.toString()));
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }


    @OnClick(R.id.baby_pic)
    public void setBabyPicImageViewClicked(){
         pictureSourceSelectFragment = new PictureSourceSelectFragment();
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

//        Log.d(TAG, dob);
//        if (!dob.equals("")){
//            String[] dateElements = dob.split(",");
//            Log.d(TAG,"" + dateElements.length);
//            Log.d(TAG, dateElements[0]);
//            int year = Integer.parseInt(dateElements[0]);
//            int month = Integer.parseInt(dateElements[1]);
//            int day = Integer.parseInt(dateElements[2]);
//            Log.d(TAG, " year "  + year + " month " + month + " day " + day);
//            datePickerBirthday.updateDate(year, month, day);
//            Calendar c = Calendar.getInstance();
//            c.set(year,month,day);
//            Log.d(TAG, "time " + c.getTimeInMillis());
//            long diff = System.currentTimeMillis() - c.getTimeInMillis();
//            long days = diff/(86400*1000);
//            Log.d(TAG, "days old " + days);
//        }

            preferences.edit().putString("dob", dob).apply();
            saveImageUri(imageUri);
            scopedBus.post(new SavedProfileEvent());


        }








    }

    @OnClick(R.id.skipBtn)
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




//        if (imageUri!=null && !imageUri.toString().equals("")) {
//
//            File file = new File(imageUri);
//            if (file.exists()) {
//                Pair<Integer, Integer> itemDimensions
//                        = getBitmapBounds(imageUri);
//
////                float aspectRatio = 1f * itemDimensions.first/itemDimensions.second;
//
//                int inSampleSize = calculateInSampleSize(itemDimensions.first,
//                        itemDimensions.second,
//                        BITMAP_MAX_WIDTH,
//                        BITMAP_MAX_HEIGHT);
//
//                Log.d(TAG, "inSampleSize" + inSampleSize);
//
//                picasso.load(new File(imageUri))
//                        .resize(itemDimensions.first / inSampleSize,
//                                itemDimensions.second / inSampleSize)
//                        .centerInside()
//                        .into(babyPicImageView);
//            }
//
//
//        }
//            babyPicImageView.setImageURI(ProfileActivity.imageUri);

    }


    private void cameraImageCaptured(Intent data) {

        Log.d(TAG, "cameraImageCaptured : " + data.getData());

        Uri returnedUri;

        if(data != null) {
            returnedUri = data.getData();

            if(returnedUri != null) {
                imageUri = returnedUri;

            }
        }

        Log.d(TAG, "imageUri " + imageUri);

        updateImageUri(imageUri.toString());

//        preferences.edit().putString("imageUri", imageUri.toString()).apply();
//
//        scopedBus.post(new PictureSelectEvent(imageUri));
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
            picasso.load(imageUri)
                    .fit()
                    .transform(new CircleTransform())
                            .into(babyPicImageView);
//            babyPicImageView.setImageURI(null);
//            babyPicImageView.setImageURI(imageUri);
        }
    }


    private void saveImageUri(Uri imageUri) {
        preferences.edit().putString("imageUri", imageUri.toString()).apply();
    }

}
