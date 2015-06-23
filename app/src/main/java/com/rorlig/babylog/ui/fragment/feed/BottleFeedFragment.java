package com.rorlig.babylog.ui.fragment.feed;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.gc.materialdesign.views.Button;
import com.j256.ormlite.dao.Dao;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.model.feed.FeedType;
import com.rorlig.babylog.otto.events.feed.FeedItemCreatedEvent;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.widget.DateTimeHeaderFragment;

import java.sql.SQLException;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/14/14.
 */
public class BottleFeedFragment extends InjectableFragment
                                implements AdapterView.OnItemClickListener {


    @ForActivity
    @Inject
    Context context;


    @InjectView(R.id.type_spinner)
    Spinner feedTypeSpinner;

    @InjectView(R.id.quantity)
    EditText quantityTextView;

    @InjectView(R.id.save_btn)
    Button saveBtn;


    @InjectView(R.id.notes)
    EditText notes;

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;

    Typeface typeface;

    private String TAG = "BottleFeedFragment";

    private EventListener eventListener = new EventListener();

    ArrayAdapter<CharSequence> feedAdapter;

    DateTimeHeaderFragment dateTimeHeader;

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        feedAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.type_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        feedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        feedTypeSpinner.setAdapter(feedAdapter);

        saveBtn.setEnabled(false);


        scopedBus.post(new FragmentCreated("Bottle Feed"));


        quantityTextView.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = quantityTextView.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = quantityTextView.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

                if ((str.length() == 2 && len < str.length())) {

                    Log.d(TAG, "appending .");
                    //checking length  for backspace.
                    quantityTextView.append(".");
                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
                }

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

        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));


//        feedTypeSpinner.setOnItemClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bottle_feed, null);
        ButterKnife.inject(this, view);
        return view;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    private class EventListener {
        public EventListener() {

        }
    }


    @OnClick(R.id.save_btn)
    public void onSaveBtnClicked(){
        Log.d(TAG, "save btn clicked");

        String quantityText = quantityTextView.getText().toString();

        Log.d(TAG, "value of quantity " + quantityText);


        if (quantityText.trim().equals("")) {
            quantityTextView.setError("Quantity cannot be blank");
        } else {
            Log.d(TAG, "double value " + Double.parseDouble(quantityTextView.getText().toString()));
            Dao<FeedDao, Integer> feedDao;
            FeedDao daoObject;
            Date date = dateTimeHeader.getEventTime();
            Log.d(TAG, "feedTypeSpinner " + feedTypeSpinner);
            try {
                feedDao = babyLoggerORMLiteHelper.getFeedDao();


                daoObject  = new FeedDao(FeedType.BOTTLE,
                        feedTypeSpinner.getSelectedItem().toString() ,
                        Double.parseDouble(quantityTextView.getText().toString()),
                        -1L,
                        -1L, notes.getText().toString(),
                        date);
//            switch (diaperChangeType.getCheckedRadioButtonId()) {
//                case R.id.diaper_wet:
//                    daoObject = new DiaperChangeDao(DiaperChangeEnum.WET, null, null, diaperIncident, notes.getText().toString(), time );
//                    break;
//                case R.id.diaper_both:
//
//                    diaperChangeTexture = getDiaperChangeTexture();
//                    daoObject = new DiaperChangeDao(DiaperChangeEnum.BOTH, diaperChangeTexture, diaperChangeColorType,
//                            diaperIncident, notes.getText().toString(), time );
//                    break;
//                default:
//                    diaperChangeTexture = getDiaperChangeTexture();
//                    daoObject = new DiaperChangeDao(DiaperChangeEnum.POOP, diaperChangeTexture, diaperChangeColorType,
//                            diaperIncident, notes.getText().toString(), time );
//
//                    break;
//            }
                feedDao.create(daoObject);
                Log.d(TAG, "created objected " + daoObject);
                scopedBus.post(new FeedItemCreatedEvent());
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }
}
