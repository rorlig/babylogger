package com.rorlig.babylog.ui.fragment.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.events.camera.CameraStartEvent;
import com.rorlig.babylog.otto.events.camera.PictureSelectEvent;
import com.rorlig.babylog.otto.events.profile.SavedProfileEvent;
import com.rorlig.babylog.otto.events.profile.SkipProfileEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.activity.ProfileActivity;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;

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
    CircleImageView babyPicImageView;

    @InjectView(R.id.save_btn)
    Button saveBtn;

    @Inject
    Picasso picasso;
//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;


    Typeface typeface;

    private String TAG = "ProfileFragment";

    private EventListener eventListener = new EventListener();
    private PictureSourceSelectFragment pictureSourceSelectFragment;


    private int BITMAP_MAX_HEIGHT = 256;
    private int BITMAP_MAX_WIDTH = 256;
    private Uri imageUri;

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        Log.d(TAG, "onActivityCreated");


        typeface=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/proximanova_light.ttf");

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

        String imageStr = preferences.getString("imageUri","");
        if (!imageStr.equals("")){
            Uri imageUri = Uri.parse(imageStr);

            babyPicImageView.setImageURI(imageUri);

        }


        scopedBus.register(eventListener);

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
            Log.d(TAG, "onCameraEvent " + event.getImageUri());

            imageUri = event.getImageUri();
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
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
        Log.d(TAG, " imageUri " + ProfileActivity.imageUri);
        if (ProfileActivity.imageUri!=null && !ProfileActivity.imageUri.toString().equals("")) {

            File file = new File(ProfileActivity.imageUri.getPath());
            if (file.exists()) {
                Pair<Integer, Integer> itemDimensions
                        = getBitmapBounds(ProfileActivity.imageUri.getPath());

                float aspectRatio = 1f * itemDimensions.first/itemDimensions.second;

                int inSampleSize = calculateInSampleSize(itemDimensions.first,
                        itemDimensions.second,
                        BITMAP_MAX_WIDTH,
                        BITMAP_MAX_HEIGHT);

                Log.d(TAG, "inSampleSize" + inSampleSize);

                picasso.load(new File(ProfileActivity.imageUri.getPath()))
                        .resize(itemDimensions.first / inSampleSize,
                                itemDimensions.second / inSampleSize)
                        .centerInside()
                        .into(babyPicImageView);
            }


        }
//            babyPicImageView.setImageURI(ProfileActivity.imageUri);

        scopedBus.register(eventListener);
    }

    /*
     * Unregister from events ...
     */
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");

        scopedBus.unregister(eventListener);
//        getActivity().stopService(new Intent(getActivity(), BackgroundLocationService.class));
    }

    private Pair<Integer,Integer> getBitmapBounds(String imagePath){
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bitmapOptions);
        int imageWidth = bitmapOptions.outWidth;
        int imageHeight = bitmapOptions.outHeight;
        Log.d(TAG, " imageWidth " + imageWidth + " imageHeight " + imageHeight);
        return new Pair<Integer,Integer>(imageWidth, imageHeight);
    }


    public static int calculateInSampleSize(
            int inputBitmapWidth, int inputBitmapHeight, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = inputBitmapHeight;
        final int width = inputBitmapWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
