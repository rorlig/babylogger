package com.rorlig.babyapp.ui.fragment.feed;

import android.content.Context;
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
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.dao.FeedDao;
import com.rorlig.babyapp.db.BabyLoggerORMLiteHelper;
import com.rorlig.babyapp.model.feed.FeedType;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.parse_dao.Feed;
import com.rorlig.babyapp.ui.fragment.BaseCreateLogFragment;
import com.rorlig.babyapp.ui.widget.DateTimeHeaderFragment;

import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//import android.widget.Button;
//import com.gc.materialdesign.views.Button;

/**
 * Created by rorlig on 7/14/14.
 */
public class NursingFeedFragment extends BaseCreateLogFragment {

    Chronometer chronometer;

    @ForActivity
    @Inject
    Context context;


    @InjectView(R.id.left_breast_hours)
    EditText leftBreastFeedHours;

    @InjectView(R.id.left_breast_minutes)
    EditText leftBreastFeedMinutes;


    @InjectView(R.id.right_breast_hours)
    EditText rightBreastFeedHours;

    @InjectView(R.id.right_breast_minutes)
    EditText rightBreastFeedMinutes;


    @InjectView(R.id.notes)
    EditText notes;

    @InjectView(R.id.save_btn)
    ButtonRectangle saveBtn;


    @InjectView(R.id.two_button_layout)
    LinearLayout editDeleteBtn;

    private String TAG = "NursingFeedFragment";

    private EventListener eventListener = new EventListener();

    private DateTimeHeaderFragment dateTimeHeader;

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;
    private boolean leftHoursEmpty = true, leftMinutesEmpty  = true, rightHoursEmpty = true, rightMinutesEmpty  = true;
    private String id;
    private boolean showEditDelete = false;
    private Feed feed;

    public NursingFeedFragment() {
        super("Feed");
    }

//    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        Log.d(TAG, "onActivityCreated");
//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.BLUE);


        setSaveEnabled();






//        soundMgr = SoundManager.getInstance(getActivity());
//
        setUpViews();
        setUpTextWatchers();

        //initialize views if not creating new feed item
        if (getArguments()!=null) {
            Log.d(TAG, "arguments are not null");
            id = getArguments().getString("feed_id");
            initViews(id);
        }


        notes.setOnEditorActionListener(doneActionListener);

//        scopedBus.post(new Feed("Bottle Feed"));

    }

    private void initViews(String id) {
        Log.d(TAG, "initViews " + id);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Feed");
        query.fromLocalDatastore();

        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                feed = (Feed) object;

                leftBreastFeedMinutes.setText("" + (int) (feed.getLeftBreastTime() % 60));
                leftBreastFeedHours.setText("" + (int) (feed.getLeftBreastTime() / 60));


                rightBreastFeedMinutes.setText("" + (int) (feed.getRightBreastTime() % 60));
                rightBreastFeedHours.setText("" + (int) (feed.getRightBreastTime() / 60));

                notes.setText(feed.getNotes());

                dateTimeHeader.setDateTime(feed.getLogCreationDate());

                showEditDelete = true;
//            saveBtn.setText("Edit");


                editDeleteBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated");

//        notificationManager = (NotificationManager)
//                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


//        subscriptions = new CompositeSubscription();







    }

    private void setUpViews() {
        Log.d(TAG, "setUpViews");
//        leftBreastFeedMinutes.setOnEditorActionListener(doneActionListener);
//        leftBreastFeedHours.setOnEditorActionListener(doneActionListener);
//        rightBreastFeedMinutes.setOnEditorActionListener(doneActionListener);
//        rightBreastFeedHours.setOnEditorActionListener(doneActionListener);
        notes.setOnEditorActionListener(doneActionListener);

    }

    private void setUpTextWatchers() {

        leftBreastFeedHours.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = leftBreastFeedHours.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = leftBreastFeedHours.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);


                if (str.length() > 0) {
                    leftHoursEmpty = false;
//                    saveBtn.setEnabled(true);
                } else {
                    leftHoursEmpty = true;
//                    saveBtn.setEnabled(false);
                }

                setSaveEnabled();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });

        leftBreastFeedMinutes.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = leftBreastFeedMinutes.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = leftBreastFeedMinutes.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);
//
//                if ((str.length() == 2 && len < str.length())) {
//
//                    Log.d(TAG, "appending .");
//                    //checking length  for backspace.
//                    heightInchesEditText.append(".");
//                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
//                }

                if (str.length() > 0) {
//                    saveBtn.setEnabled(true);
                    leftMinutesEmpty = false;

                } else {
//                    saveBtn.setEnabled(false);
                    leftMinutesEmpty = true;

                }
                setSaveEnabled();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });


        rightBreastFeedHours.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = rightBreastFeedHours.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = rightBreastFeedHours.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

                if (str.length() > 0) {

//                    saveBtn.setEnabled(true);
                    rightHoursEmpty = false;
                    setSaveEnabled();
                } else {
//                    saveBtn.setEnabled(false);
                    rightHoursEmpty = true;
                }
                setSaveEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });




        rightBreastFeedMinutes.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = rightBreastFeedMinutes.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = rightBreastFeedMinutes.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

//                if ((str.length() == 2 && len < str.length())) {
//
//                    Log.d(TAG, "appending .");
//                    //checking length  for backspace.
//                    headInchesEditText.append(".");
//                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
//                }

                if (str.length() > 0) {

//                    saveBtn.setEnabled(true);
                    rightMinutesEmpty = false;
                    setSaveEnabled();
                } else {
//                    saveBtn.setEnabled(false);
                    rightMinutesEmpty = true;
                }
                setSaveEnabled();
            }

            ;

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");
        if ((!rightMinutesEmpty || !rightHoursEmpty || !leftMinutesEmpty || !leftHoursEmpty)) {
            Log.d(TAG, "disable the action_add");
            menu.findItem(R.id.action_add).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_save_white));

        } else {
            menu.findItem(R.id.action_add).setEnabled(false);

        }
    }

    private void setSaveEnabled() {
        getActivity().invalidateOptionsMenu();

        Log.d(TAG, " rightMinutesEmpty " + rightMinutesEmpty + " rightHoursEmpty " + rightHoursEmpty
                + " leftMinutesEmpty " + leftMinutesEmpty + " leftHoursEmpty " + leftHoursEmpty);
        if ((!rightMinutesEmpty || !rightHoursEmpty || !leftMinutesEmpty || !leftHoursEmpty)) {

            Log.d(TAG, "setting ime options to done");
            Log.d(TAG, "setting ime options to done");
            notes.setImeOptions(EditorInfo.IME_ACTION_DONE);
//            rightBreastFeedMinutes.setImeOptions(EditorInfo.IME_ACTION_DONE);
//            leftBreastFeedHours.setImeOptions(EditorInfo.IME_ACTION_DONE);
//            leftBreastFeedMinutes.setImeOptions(EditorInfo.IME_ACTION_DONE);


            saveBtn.setEnabled(true);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_nursing_feed_manual, null);
        ButterKnife.inject(this, view);
        return view;
    }



    private class EventListener {
        public EventListener() {

        }

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



    /*
    * Register to events...
    */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        scopedBus.register(eventListener);
    }

    /*
     * Unregister from events ...
     */
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");

//        Log.d(TAG, " leftStarted " + leftStarted + " rightStarted " + rightStarted);
        scopedBus.unregister(eventListener);

    }

    private Long elapsedTimeL, elapsedTimeR;


    @OnClick(R.id.save_btn)
    public void onSaveBtnClicked() {
        Log.d(TAG, "save btn clicked");
        createOrEdit();
//        Dao<FeedDao, Integer> feedDao;
//        FeedDao daoObject;
//        Date date = dateTimeHeader.getEventTime();
//        try {
//            feedDao = babyLoggerORMLiteHelper.getFeedDao();
//
//
//            daoObject  = new FeedDao(FeedType.BREAST,
//                    "" , -1.0,
//                    getDuration(leftBreastFeedHours, leftBreastFeedMinutes),
//                    getDuration(rightBreastFeedHours, rightBreastFeedMinutes), notes.getText().toString(),
//                    date);
//            feedDao.create(daoObject);
//            Log.d(TAG, "created object " + daoObject);
//            getActivity().stopService(new Intent(getActivity(), StopWatchService.class));
//            scopedBus.post(new FeedItemCreatedEvent());
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

    /*
    */
    public void createOrEdit() {
        final Feed tempFeedItem = createLocalFeed();
        if (feed!=null){
            feed.setLeftBreastTime(tempFeedItem.getLeftBreastTime());
            feed.setLogCreationDate(tempFeedItem.getLogCreationDate());
            feed.setRightBreastTime(tempFeedItem.getRightBreastTime());
            feed.setNotes(tempFeedItem.getNotes());
            feed.setQuantity(tempFeedItem.getQuantity());
            feed.setFeedItem(tempFeedItem.getFeedItem());
            saveEventually(feed);
        } else {
            tempFeedItem.saveEventually();
        }
//        if (id!=null) {
//            Log.d(TAG, "updating it");
////                daoObject.setId(id);
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Feed");
//            query.fromLocalDatastore();
//            query.getInBackground(id, new GetCallback<ParseObject>() {
//                @Override
//                public void done(ParseObject object, ParseException e) {
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

    private Feed createLocalFeed() {
        Date date = dateTimeHeader.getEventTime();

        return new Feed(FeedType.BREAST,
                "" , -1.0,
                getDuration(leftBreastFeedHours, leftBreastFeedMinutes),
                getDuration(rightBreastFeedHours, rightBreastFeedMinutes), notes.getText().toString(),
                date);
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

    @OnClick(R.id.edit_btn)
    public void onEditBtnClicked(){
        Log.d(TAG, "edit btn clicked");
        createOrEdit();
    }


    /*
     * creates a temporary feed item from the local view values...
     */
    private FeedDao createLocalFeedDao() {


        Date date = dateTimeHeader.getEventTime();

        return new FeedDao(FeedType.BREAST,
                        "" , -1.0,
                        getDuration(leftBreastFeedHours, leftBreastFeedMinutes),
                        getDuration(rightBreastFeedHours, rightBreastFeedMinutes), notes.getText().toString(),
                        date);

    }

    /*
     * deletes the feed item...
     */
    @OnClick(R.id.delete_btn)
    public void onDeleteBtnClicked(){
        delete(feed);
    }


    private Long getDuration(EditText hoursTextView, EditText minutesTextView) {
        String hoursText = hoursTextView.getText().toString().equals("")?"0":hoursTextView.getText().toString();
        String minutesText = minutesTextView.getText().toString().equals("")?"0":minutesTextView.getText().toString();

        int hour = Integer.parseInt(hoursText);
        int minute = Integer.parseInt(minutesText);
        return Long.valueOf(hour * 60 + minute);

    }

//    private void unsubscribeTimers() {
//        if (leftSubscription!=null&&!leftSubscription.isUnsubscribed()) {
//            leftSubscription.unsubscribe();
//        }
//
//        if (rightSubscription!=null&&!rightSubscription.isUnsubscribed()) {
//            rightSubscription.unsubscribe();
//        }
//
//
//
//    }
//
//
//    private void unsubscribeLeftTimer() {
//        if (leftSubscription!=null&&!leftSubscription.isUnsubscribed()) {
//            leftSubscription.unsubscribe();
//        }
//    }
//
//    private void unsubscribeRightTimer() {
//        if (rightSubscription!=null&&!rightSubscription.isUnsubscribed()) {
//            rightSubscription.unsubscribe();
//        }
//    }

//    private void resetTextViews() {
//        rightTextView.setText(getResources().getString(R.string.stopwatch_start_text));
//        leftTextView.setText(getResources().getString(R.string.stopwatch_start_text));
//    }
//

}
