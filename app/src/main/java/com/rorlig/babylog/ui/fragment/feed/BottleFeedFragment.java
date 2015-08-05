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
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.gc.materialdesign.views.Button;
import com.j256.ormlite.dao.Dao;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.model.feed.FeedType;
import com.rorlig.babylog.otto.events.diaper.DiaperLogCreatedEvent;
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

    @InjectView(R.id.two_button_layout)
    LinearLayout editDeleteBtn;


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

    private int id =-1;

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

        setUpTextWatchers();




        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.BLUE);

        //initialize views if not creating new feed item
        if (getArguments()!=null) {
            Log.d(TAG, "arguments are not null");
            id = getArguments().getInt("feed_id");
            initViews(id);
        }

//        feedTypeSpinner.setOnItemClickListener(this);
    }


    // sets up the text watchers to monitor text change in the various edittext boxes
    private void setUpTextWatchers() {
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

                if (str.length() > 0) {
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
    }

    // initialize views based on
    private void initViews(int id) {
        Log.d(TAG, "initViews " + id);
        try {
            FeedDao feedDao = babyLoggerORMLiteHelper.getFeedDao().queryForId(id);
            Log.d(TAG, feedDao.toString());
            editDeleteBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);

            quantityTextView.setText(feedDao.getQuantity().toString());
            final String[] values = getResources().getStringArray(R.array.type_array);
            int index = 0;
            for (String value : values) {
                if (value.equals(feedDao.getFeedItem())) {
                    feedTypeSpinner.setSelection(index);
                    break;
                }
                index++;
            }

            notes.setText(feedDao.getNotes());

            dateTimeHeader.setDateTime(feedDao.getDate());

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
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
        createOrEdit();



    }

    /*
     * creates a temporary feed item from the local view values...
     */
    private FeedDao createLocalFeedDao() {

        String quantityText = quantityTextView.getText().toString();

        Log.d(TAG, "value of quantity " + quantityText);


        if (quantityText.trim().equals("")) {
            quantityTextView.setError("Quantity cannot be blank");
            return null;
        }

        Date date = dateTimeHeader.getEventTime();

        return new FeedDao(FeedType.BOTTLE,
                feedTypeSpinner.getSelectedItem().toString(),
                Double.parseDouble(quantityTextView.getText().toString()),
                -1L,
                -1L, notes.getText().toString(),
                date);

    }

    @OnClick(R.id.edit_btn)
    public void onEditBtnClicked(){
        Log.d(TAG, "edit btn clicked");
        createOrEdit();
    }

    /*
     */
    private void createOrEdit() {
        Dao<FeedDao, Integer> feedDao;
        try {

            feedDao = createFeedDao();
            FeedDao daoObject = createLocalFeedDao();

            if (daoObject!=null) {
                if (id!=-1) {
                    Log.d(TAG, "updating it");
                    daoObject.setId(id);
                    feedDao.update(daoObject);
                } else {
                    Log.d(TAG, "creating it");
                    feedDao.create(daoObject);
                }

                Log.d(TAG, "created objected " + daoObject);
                scopedBus.post(new FeedItemCreatedEvent());
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * deletes the feed item...
     */
    @OnClick(R.id.delete_btn)
    public void onDeleteBtnClicked(){
        Log.d(TAG, "delete btn clicked");
        Dao<FeedDao, Integer> daoObject;

        try {

            daoObject = createFeedDao();

            if (id!=-1) {
                Log.d(TAG, "updating it");
                daoObject.deleteById(id);
            }
            scopedBus.post(new FeedItemCreatedEvent());
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private Dao<FeedDao, Integer> createFeedDao() throws SQLException {
        return babyLoggerORMLiteHelper.getFeedDao();
    }
}
