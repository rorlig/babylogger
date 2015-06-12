package com.rorlig.babylog.ui.fragment.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.SavedProfileEvent;
import com.rorlig.babylog.otto.SkipProfileEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

    @InjectView(R.id.babyName)
    TextView babyNameTextView;

    @InjectView(R.id.baby_sex)
    RadioGroup babySexRadioGroup;

    @InjectView(R.id.babyGirl)
    RadioButton babyGirlButton;

    @InjectView(R.id.babyBoy)
    RadioButton babyBoyButton;


    @InjectView(R.id.date_picker_birthday)
    DatePicker datePickerBirthday;

//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;


    Typeface typeface;

    private String TAG = "ProfileFragment";

    private EventListener eventListener = new EventListener();

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        typeface=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Profile "));


        String babyName = preferences.getString("name","");
        babyNameTextView.setText(babyName);

        String babySex = preferences.getString("baby_sex","male");
        if (babySex.equals("male")){
            babyBoyButton.setChecked(true);
        } else {
            babyGirlButton.setChecked(true);
        }

        String dob = preferences.getString("dob","");
        Log.d(TAG, dob);
        if (!dob.equals("")){
            String[] dateElements = dob.split(",");
            Log.d(TAG,"" + dateElements.length);
            Log.d(TAG, dateElements[0]);
            int year = Integer.parseInt(dateElements[0]);
            int month = Integer.parseInt(dateElements[1]);
            int day = Integer.parseInt(dateElements[2]);
            Log.d(TAG, " year "  + year + " month " + month + " day " + day);
            datePickerBirthday.updateDate(year, month, day);
        }
        //todo dob...

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, null);
        ButterKnife.inject(this, view);
        return view;
    }


    private class EventListener {
        public EventListener() {

        }
    }

    @OnClick(R.id.saveBtn)
    public void saveBtnClicked() {
        Log.d(TAG, "saveBtnClicked()");
        String babyName = babyNameTextView.getText().toString();
        preferences.edit().putString("name", babyName).apply();
        if (babySexRadioGroup.getCheckedRadioButtonId()==R.id.babyBoy) {
            preferences.edit().putString("baby_sex", "male").apply();
        } else  {
            preferences.edit().putString("baby_sex", "female").apply();
        }


        int year = datePickerBirthday.getYear();
        int month = datePickerBirthday.getMonth() + 1;
        int day = datePickerBirthday.getDayOfMonth();

        String dob = year  + "," + month + "," + day;

        preferences.edit().putString("dob", dob).apply();
        scopedBus.post(new SavedProfileEvent());




    }

    @OnClick(R.id.skipBtn)
    public void skipBtnClicked(){
        Log.d(TAG, "skipBtnClicked()");
        scopedBus.post(new SkipProfileEvent());
//        scopedBus.post(new S);
    }
}
