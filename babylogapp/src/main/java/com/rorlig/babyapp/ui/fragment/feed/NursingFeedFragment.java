package com.rorlig.babyapp.ui.fragment.feed;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.j256.ormlite.dao.Dao;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.dao.FeedDao;
import com.rorlig.babyapp.db.BabyLoggerORMLiteHelper;
import com.rorlig.babyapp.model.feed.FeedType;
import com.rorlig.babyapp.otto.events.feed.FeedItemCreatedEvent;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.rorlig.babyapp.ui.widget.DateTimeHeaderFragment;

import java.sql.SQLException;
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
public class NursingFeedFragment extends InjectableFragment {

    Chronometer chronometer;

    @ForActivity
    @Inject
    Context context;


    @InjectView(R.id.left_sleep_hours)
    EditText leftSleepHours;

    @InjectView(R.id.left_sleep_minutes)
    EditText leftSleepMinutes;


    @InjectView(R.id.right_sleep_hours)
    EditText rightSleepHours;

    @InjectView(R.id.right_sleep_minutes)
    EditText rightSleepMinutes;


    @InjectView(R.id.notes)
    EditText notes;

    @InjectView(R.id.save_btn)
    ButtonRectangle saveBtn;


    @InjectView(R.id.two_button_layout)
    LinearLayout editDeleteBtn;
////    @InjectView(R.id.minute)
//    TextView minuteLTextView;
//
//
//
////    @InjectView(R.id.second)
//    TextView secondLTextView;
//
//
//
////    @InjectView(R.id.hour)
//    TextView hourRTextView;
//
//
//    TextView minuteRTextView;
//
//
//
//    //    @InjectView(R.id.second)
//    TextView secondRTextView;
//
//
//
//    //    @InjectView(R.id.hour)
//    TextView hourLTextView;
////
////
////    TextView leftTextView;
////
////    TextView rightTextView;
////    @InjectView(R.id.leftStopWatch)
////    RelativeLayout leftStopWatch;
////
////    @InjectView(R.id.rightStopWatch)
////    RelativeLayout rightStopWatch;
//
////    @InjectView(R.id.gridview)
////    GridView actionsList;
//
////    @InjectView(R.id.menu_header)
////    TextView menuHeader;
//

//    Typeface typeface;

    private String TAG = "NursingFeedFragment";

    private EventListener eventListener = new EventListener();
    private long startTime;
//    private boolean leftStarted = false;
//    private boolean rightStarted = false;
//    private TextView rightTextView;
//    private TextView leftTextView;
    private DateTimeHeaderFragment dateTimeHeader;

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;
    private NotificationManager notificationManager;
    private Intent intent;
    private PendingIntent pIntent;
    private Notification notification;
    private int notification_id=0;
    private boolean leftHoursEmpty = true, leftMinutesEmpty  = true, rightHoursEmpty = true, rightMinutesEmpty  = true;
    private int id = -1;
    private boolean showEditDelete = false;
//    private SoundManager soundMgr;
//    private CompositeSubscription subscriptions;
//    private Subscription leftSubscription;
//    private Subscription rightSubscription;

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
            id = getArguments().getInt("feed_id");
            initViews(id);
        }


        notes.setOnEditorActionListener(doneActionListener);

//        scopedBus.post(new Feed("Bottle Feed"));

    }

    private void initViews(int id) {
        Log.d(TAG, "initViews " + id);
        try {
            FeedDao feedDao = babyLoggerORMLiteHelper.getFeedDao().queryForId(id);
            Log.d(TAG, feedDao.toString());
            editDeleteBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
            leftSleepMinutes.setText("" + (int) (feedDao.getLeftBreastTime() % 60));
            leftSleepHours.setText("" + (int) (feedDao.getLeftBreastTime()/60));


            rightSleepMinutes.setText("" + (int) (feedDao.getRightBreastTime() % 60));
            rightSleepHours.setText("" + (int) (feedDao.getRightBreastTime()/60));

            notes.setText(feedDao.getNotes());

            dateTimeHeader.setDateTime(feedDao.getDate());


            showEditDelete = true;

//            quantityTextView.setText(feedDao.getQuantity().toString());
//            final String[] values = getResources().getStringArray(R.array.type_array);
//            int index = 0;
//            for (String value : values) {
//                if (value.equals(feedDao.getFeedItem())) {
//                    feedTypeSpinner.setSelection(index);
//                    break;
//                }
//                index++;
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated");

        notificationManager = (NotificationManager)
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


//        subscriptions = new CompositeSubscription();







    }

    private void setUpViews() {
        Log.d(TAG, "setUpViews");
//        leftSleepMinutes.setOnEditorActionListener(doneActionListener);
//        leftSleepHours.setOnEditorActionListener(doneActionListener);
//        rightSleepMinutes.setOnEditorActionListener(doneActionListener);
//        rightSleepHours.setOnEditorActionListener(doneActionListener);
        notes.setOnEditorActionListener(doneActionListener);

    }

    private void setUpTextWatchers() {

        leftSleepHours.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = leftSleepHours.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = leftSleepHours.getText().toString();

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

        leftSleepMinutes.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = leftSleepMinutes.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = leftSleepMinutes.getText().toString();

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


        rightSleepHours.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = rightSleepHours.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = rightSleepHours.getText().toString();

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




        rightSleepMinutes.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = rightSleepMinutes.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = rightSleepMinutes.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

//                if ((str.length() == 2 && len < str.length())) {
//
//                    Log.d(TAG, "appending .");
//                    //checking length  for backspace.
//                    headInchesEditText.append(".");
//                    //Toast.makeText(getBaseContext(), "add minus", Toast.LENGTH_SHORT).show();
//                }

                if (str.length()>0) {

//                    saveBtn.setEnabled(true);
                    rightMinutesEmpty = false;
                    setSaveEnabled();
                } else {
//                    saveBtn.setEnabled(false);
                    rightMinutesEmpty = true;
                }
                setSaveEnabled();
            };

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
//            rightSleepMinutes.setImeOptions(EditorInfo.IME_ACTION_DONE);
//            leftSleepHours.setImeOptions(EditorInfo.IME_ACTION_DONE);
//            leftSleepMinutes.setImeOptions(EditorInfo.IME_ACTION_DONE);


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
//                    getDuration(leftSleepHours, leftSleepMinutes),
//                    getDuration(rightSleepHours, rightSleepMinutes), notes.getText().toString(),
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
                        getDuration(leftSleepHours, leftSleepMinutes),
                        getDuration(rightSleepHours, rightSleepMinutes), notes.getText().toString(),
                        date);

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
