package com.rorlig.babylog.ui.fragment.growth;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gc.materialdesign.views.Button;
import com.j256.ormlite.dao.Dao;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.GrowthDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.otto.GrowthItemCreated;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.widget.DateTimeHeaderFragment;

import java.sql.SQLException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by gaurav
 * Growth element..
 */
public class GrowthFragment extends InjectableFragment {

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.notes)
    EditText notesContentTextView;

    @InjectView(R.id.save_btn)
    Button saveBtn;

    @InjectView(R.id.weight_pounds)
    EditText weightPoundsEditText;

    @InjectView(R.id.weight_ounces)
    EditText weightOuncesEditText;

    @InjectView(R.id.height_inches)
    EditText heightInchesEditText;

    @InjectView(R.id.head_inches)
    EditText headInchesEditText;

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;

    DateTimeHeaderFragment dateTimeHeader;

    private String TAG = "GrowthFragment";

    private EventListener eventListener = new EventListener();

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Growth Fragment"));


        heightInchesEditText.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = heightInchesEditText.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = heightInchesEditText.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

                if ((str.length() == 2 && len < str.length())) {

                    Log.d(TAG, "appending .");
                    //checking length  for backspace.
                    heightInchesEditText.append(".");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }

//                if (str.length()>0) {
//                    saveBtn.setEnabled(true);
//                } else {
//                    saveBtn.setEnabled(false);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });


        headInchesEditText.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = headInchesEditText.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = headInchesEditText.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

                if ((str.length() == 2 && len < str.length())) {

                    Log.d(TAG, "appending .");
                    //checking length  for backspace.
                    headInchesEditText.append(".");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }

//                if (str.length()>0) {
//                    saveBtn.setEnabled(true);
//                } else {
//                    saveBtn.setEnabled(false);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });
//        headInchesEditText.addTextChangedListener(new TextWatcher() {
//            int len = 0;
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.d(TAG, "beforeTextChanged ");
//                String str = headInchesEditText.getText().toString();
//                len = str.length();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d(TAG, "onTextChanged ");
//
//                String str = headInchesEditText.getText().toString();
//
//                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);
//
//                if ((str.length() == 2 && len < str.length())) {
//
//                    Log.d(TAG, "appending .");
//                    //checking length  for backspace.
//                    headInchesEditText.append(".");
//                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
//                }
//
////            if (str.length()>0) {
////                saveBtn.setEnabled(true);
////            } else {
////                saveBtn.setEnabled(false);
////            }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));

    }

    private class MeasurementWatcher implements TextWatcher {


        private final EditText editText;
        private int len;

        public MeasurementWatcher(EditText editText) {

            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            Log.d(TAG, "beforeTextChanged ");
            String str = editText.getText().toString();
            len = str.length();

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            Log.d(TAG, "onTextChanged ");

            String str = editText.getText().toString();

            Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

            if ((str.length() == 2 && len < str.length())) {

                Log.d(TAG, "appending .");
                //checking length  for backspace.
                editText.append(".");
                //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
            }

//            if (str.length()>0) {
//                saveBtn.setEnabled(true);
//            } else {
//                saveBtn.setEnabled(false);
//            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @OnClick(R.id.save_btn)
    public void saveBtnClicked() {
        Dao<GrowthDao, Integer> growthDao;
        GrowthDao daoObject;
        Long time = dateTimeHeader.getEventTime();
        Integer weightPounds = Integer.parseInt(weightPoundsEditText.getText().toString());

        Integer weightOunces = Integer.parseInt(weightOuncesEditText.getText().toString());

        Double totalWeight = weightOunces.doubleValue()/16 + weightPounds;

        Double height  = Double.parseDouble(heightInchesEditText.getText().toString());

        Double headMeasure = Double.parseDouble(headInchesEditText.getText().toString());




        try {
            growthDao = babyLoggerORMLiteHelper.getGrowthDao();


            daoObject  = new GrowthDao(totalWeight, height, headMeasure, notesContentTextView.getText().toString(), time);
            growthDao.create(daoObject);
//            feedDao.create(daoObject);
            Log.d(TAG, "created objected " + daoObject);
            scopedBus.post(new GrowthItemCreated());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BabyLoggerORMLiteHelper getBabyLoggerORMLiteHelper() {
        return babyLoggerORMLiteHelper;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_growth, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    private class EventListener {
        public EventListener() {

        }
    }
}
