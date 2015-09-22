package com.rorlig.babyapp.ui.fragment.feed;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.model.feed.FeedType;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.Feed;
import com.rorlig.babyapp.ui.fragment.BaseCreateLogFragment;
import com.rorlig.babyapp.ui.widget.DateTimeHeaderFragment;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/14/14.
 */
public class BottleFeedFragment extends BaseCreateLogFragment
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


    Typeface typeface;

    private String TAG = "BottleFeedFragment";

    private EventListener eventListener = new EventListener();

    ArrayAdapter<CharSequence> feedAdapter;


    private String id;

    private boolean showEditDelete = false;
    private boolean quantityEmpty = true;
    private Feed feed;
    private String uuid;

    public BottleFeedFragment() {
        super("Feed");
    }


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

//        saveBtn.setEnabled(false);


        scopedBus.post(new FragmentCreated("Bottle Feed"));

        setUpTextWatchers();




        //initialize views if not creating new feed item
        if (getArguments()!=null) {
            Log.d(TAG, "arguments are not null");
            uuid = getArguments().getString("uuid");
            position = getArguments().getInt("position");
            initViews(uuid);
        }

        notes.setOnEditorActionListener(doneActionListener);

        setSaveEnabled();

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
                    quantityEmpty = false;
                } else {
                    quantityEmpty = true;

                }

                setSaveEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });
    }

    // initialize views based on
    private void initViews(String uuid) {
        Log.d(TAG, "initViews " + uuid);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Feed");
        query.fromLocalDatastore();
        query.whereEqualTo("uuid", uuid);

        query.findInBackground(new FindCallback<ParseObject>() {
               @Override
               public void done(List<ParseObject> objects, ParseException e) {
                   feed = (Feed) objects.get(0);

                   quantityTextView.setText(feed.getQuantity().toString());
                   final String[] values = getResources().getStringArray(R.array.type_array);
                   int index = 0;
                   for (String value : values) {
                       if (value.equals(feed.getFeedItem())) {
                           feedTypeSpinner.setSelection(index);
                           break;
                       }
                       index++;
                   }

                   notes.setText(feed.getNotes());

                   dateTimeHeader.setDateTime(feed.getLogCreationDate());

                   showEditDelete = true;
//            saveBtn.setText("Edit");


                   editDeleteBtn.setVisibility(View.VISIBLE);
                   saveBtn.setVisibility(View.GONE);
               }
           }


        );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.BLUE);


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
        if (!showEditDelete) {
            inflater.inflate(R.menu.menu_add_item, menu);
        } else {
            inflater.inflate(R.menu.menu_edit_delete_item, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case R.id.action_add:
//                createOrEdit();
////                startActivity(new Intent(getActivity(), PrefsActivity.class));
//                return true;
            case R.id.action_add:
                createOrEdit();
                return true;
            case R.id.action_delete:
                onDeleteBtnClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
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


    @OnClick(R.id.edit_btn)
    public void onEditBtnClicked(){
        Log.d(TAG, "edit btn clicked");
        createOrEdit();
    }

    /*
     */
    public void createOrEdit() {

        final Feed tempFeedItem = createLocalFeed();
        if (feed!=null) {
            feed.setLeftBreastTime(tempFeedItem.getLeftBreastTime());
            feed.setLogCreationDate(tempFeedItem.getLogCreationDate());
            feed.setRightBreastTime(tempFeedItem.getRightBreastTime());
            feed.setNotes(tempFeedItem.getNotes());
            feed.setQuantity(tempFeedItem.getQuantity());
            feed.setFeedItem(tempFeedItem.getFeedItem());
            saveEventually(feed);

        } else {
            saveEventually(tempFeedItem);
        }
//        if (id!=null) {
//            Log.d(TAG, "updating it with id " + id);
////                daoObject.setId(id);
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Feed");
//            query.fromLocalDatastore();
//            query.getInBackground(id, new GetCallback<ParseObject>() {
//                @Override
//                public void done(ParseObject object, ParseException e) {
//                    Log.d(TAG, "parse object " + object + " exception " + e);
//                    Feed feed = (Feed) object;
//                    feed.setLeftBreastTime(tempFeedItem.getLeftBreastTime());
//                    feed.setLogCreationDate(tempFeedItem.getLogCreationDate());
//                    feed.setRightBreastTime(tempFeedItem.getRightBreastTime());
//                    feed.setNotes(tempFeedItem.getNotes());
//                    feed.setQuantity(tempFeedItem.getQuantity());
//                    feed.setFeedItem(tempFeedItem.getFeedItem());
//                    saveEventually(feed);
//                }
//            });
////                diaperChange.setObjectId(id);
//
//        } else {
//            Log.d(TAG, "creating it");
//            saveEventually(tempFeedItem);
////                diaperChangeDao.create(daoObject);
//        }

//            Log.d(TAG, "created objected " + daoObject);
        closeSoftKeyBoard();
        scopedBus.post(new ItemCreatedOrChanged("Feed"));

    }

    private void saveEventually(final Feed feed) {
        feed.pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d(TAG, "pinning new object");
                feed.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d(TAG, "saving locally");

                    }
                });
            }
        });
    }


    private Feed createLocalFeed() {
        String quantityText = quantityTextView.getText().toString();

        Log.d(TAG, "value of quantity " + quantityText);


        if (quantityText.trim().equals("")) {
            quantityTextView.setError("Quantity cannot be blank");
            return null;
        }

        Date date = dateTimeHeader.getEventTime();

        return new Feed(FeedType.BOTTLE,
                feedTypeSpinner.getSelectedItem().toString(),
                Double.parseDouble(quantityTextView.getText().toString()),
                -1L,
                -1L, notes.getText().toString(),
                date);
    }

    /*
     * deletes the feed item...
     */
    @OnClick(R.id.delete_btn)
    public void onDeleteBtnClicked(){
        Log.d(TAG, "deleting id " + uuid);
        delete(feed);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");
        if (!quantityEmpty) {
            Log.d(TAG, "disable the action_add");
            menu.findItem(R.id.action_add).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_save_white));

        } else {
            menu.findItem(R.id.action_add).setEnabled(false);

        }
    }

    private void setSaveEnabled() {

        getActivity().invalidateOptionsMenu();

        if (!quantityEmpty) {
            saveBtn.setEnabled(true);
            notes.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            saveBtn.setEnabled(false);
            notes.setImeOptions(EditorInfo.IME_ACTION_NONE);

        }

    }

    private EditText.OnEditorActionListener doneActionListener = new EditText.OnEditorActionListener(){

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.d(TAG, "onEditorAction view " + v.getText() + " actionId " + actionId + " event " + event);
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createOrEdit();
                return true;
            }
            return false;
        }
    };
}
