package com.rorlig.babylog.ui.fragment.feed;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.model.feed.FeedType;
import com.rorlig.babylog.otto.events.feed.FeedItemCreatedEvent;
import com.rorlig.babylog.otto.events.timer.TimerEvent;
import com.rorlig.babylog.service.StopWatchService;
import com.rorlig.babylog.ui.activity.FeedingActivity;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.widget.DateTimeHeaderFragment;
import com.rorlig.babylog.utils.SoundManager;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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


    @InjectView(R.id.left)
    RelativeLayout leftButton;


    @InjectView(R.id.right)
    RelativeLayout rightButton;


    @InjectView(R.id.notes)
    EditText notes;


//    @InjectView(R.id.minute)
    TextView minuteLTextView;



//    @InjectView(R.id.second)
    TextView secondLTextView;



//    @InjectView(R.id.hour)
    TextView hourRTextView;


    TextView minuteRTextView;



    //    @InjectView(R.id.second)
    TextView secondRTextView;



    //    @InjectView(R.id.hour)
    TextView hourLTextView;
//
//
//    TextView leftTextView;
//
//    TextView rightTextView;
//    @InjectView(R.id.leftStopWatch)
//    RelativeLayout leftStopWatch;
//
//    @InjectView(R.id.rightStopWatch)
//    RelativeLayout rightStopWatch;

//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;


    Typeface typeface;

    private String TAG = "NursingFeedFragment";

    private EventListener eventListener = new EventListener();
    private long startTime;
    private boolean leftStarted = false;
    private boolean rightStarted = false;
    private TextView rightTextView;
    private TextView leftTextView;
    private DateTimeHeaderFragment dateTimeHeader;

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;
    private NotificationManager notificationManager;
    private Intent intent;
    private PendingIntent pIntent;
    private Notification notification;
    private int notification_id=0;
    private SoundManager soundMgr;

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);

        typeface=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/proximanova_light.ttf");

        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));


        notificationManager = (NotificationManager)
                getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

        intent = new Intent(getActivity(), FeedingActivity.class);
        pIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        // build notification
// the addAction re-use the same intent to keep the example short
        notification  = new Notification.Builder(getActivity())
                .setContentTitle("Logging Location")
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .build();


        soundMgr = SoundManager.getInstance(getActivity());



//        scopedBus.post(new Feed("Bottle Feed"));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        leftTextView = (TextView) leftButton.findViewById(R.id.startLText);
        rightTextView = (TextView) rightButton.findViewById(R.id.startRText);

        hourLTextView = (TextView) leftButton.findViewById(R.id.hour);
        hourRTextView = (TextView) rightButton.findViewById(R.id.hour);

        minuteLTextView = (TextView) leftButton.findViewById(R.id.minute);
        minuteRTextView = (TextView) rightButton.findViewById(R.id.minute);

        secondLTextView = (TextView) leftButton.findViewById(R.id.second);
        secondRTextView = (TextView) rightButton.findViewById(R.id.second);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_feeding, null);
        ButterKnife.inject(this, view);
        return view;
    }


    private class EventListener {
        public EventListener() {

        }

        @Subscribe
        public void onTimeEvent(TimerEvent timerEvent) {
//            Log.d(TAG, "onTimeTimeEvent");
            soundMgr.doTick();

            Long currentTime = System.currentTimeMillis();
            int diff;

            Log.d(TAG, "leftStarted " + leftStarted + " rightStartded: " + rightStarted + " deltaL " + deltaL + " deltaR " + deltaR);

            if (leftStarted ) {
                diff = (int)((currentTime - elapsedTimeL)/1000 + deltaL);
            } else  {
                diff = (int)((currentTime - elapsedTimeR)/1000 + deltaR);

            }
//            Log.d(TAG, " diff in seconds " + diff);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, diff);
            String time =  new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
            String hours = time.substring(0, 2);
            String minutes = time.substring(3,5);
            String seconds = time.substring(6,8);
            Log.d(TAG, "hours "  + hours + " minutes " + minutes + " seconds "  + seconds);

            if (leftStarted) {

                hourLTextView.setText(hours);
            minuteLTextView.setText(minutes);
            secondLTextView.setText(seconds);

            } else  {

                hourRTextView.setText(hours);
                minuteRTextView.setText(minutes);
                secondRTextView.setText(seconds);

            }
//            hourTextView.setText(hours);
//            minuteTextView.setText(minutes);
//            secondTextView.setText(seconds);



//            int seconds, minutes, hours;

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    private Long deltaL = 0L;
    private Long deltaR = 0L;

    @OnClick(R.id.left)
    public void leftButtonClicked() {
        Intent intent = new Intent(getActivity(), StopWatchService.class);
        intent.putExtra("stopwatch", "left");

        if (!leftStarted) {

            getActivity().startService(intent);

            elapsedTimeL = System.currentTimeMillis();
            leftStarted = true;
            leftTextView.setText(getResources().getString(R.string.stopwatch_stop_text));
//
//            leftButton.
            showStartButtons();

            if (rightStarted) {
//                intent = new Intent(getActivity(), StopWatchService.class);
//                intent.putExtra("stopwatch", "left");
                rightTextView.setText(getResources().getString(R.string.stopwatch_start_text));
                rightStarted = false;
                deltaR += (System.currentTimeMillis() - elapsedTimeR)/1000;
//            rightTextView.setText("Start");
//                rightTextView.setText(getResources().getString(R.string.stopwatch_start_text));


            }


        } else {
            getActivity().stopService(new Intent(getActivity(), StopWatchService.class));
            leftTextView.setText(getResources().getString(R.string.stopwatch_start_text));

            deltaL += (System.currentTimeMillis() - elapsedTimeL)/1000;
            leftStarted = false;
//            soundMgr.stopEndlessAlarm();
//            showStopButtons();
        }

    }

    private void showStartButtons() {

        Log.d(TAG, " leftButton " + leftButton);
//        leftButton.setText("Pause");
    }

    private void showStopButtons() {
//        leftButton.setText("Start");
    }

    @OnClick(R.id.right)
    public void rightButtonClicked() {
        Log.d(TAG, "right button pressed");
//        leftStarted = false;

        Log.d(TAG, "rightbutton " + rightStarted);
        if (!rightStarted) {


            getActivity().startService(new Intent(getActivity(), StopWatchService.class));

            elapsedTimeR = System.currentTimeMillis();
            rightStarted = true;
            rightTextView.setText(getResources().getString(R.string.stopwatch_stop_text));
//
//            leftButton.
            showStartButtons();

            if (leftStarted) {
//                intent = new Intent(getActivity(), StopWatchService.class);
//                intent.putExtra("stopwatch", "left");
                leftStarted = false;
                deltaL += (System.currentTimeMillis() - elapsedTimeL)/1000;
//            rightTextView.setText("Start");
                leftTextView.setText(getResources().getString(R.string.stopwatch_start_text));


            }

//            soundMgr.doTick();


        } else {
            getActivity().stopService(new Intent(getActivity(), StopWatchService.class));
            rightTextView.setText(getResources().getString(R.string.stopwatch_start_text));

            deltaR += (System.currentTimeMillis() - elapsedTimeR)/1000;
            rightStarted = false;

//            soundMgr.stopEndlessAlarm();
//            showStopButtons();
        }

//        deltaL = 0L;
//        hourTextView.setText("00");
//        minuteTextView.setText("00");
//        secondTextView.setText("00");
//        leftButton.setText(getResources().getString(R.string.stopwatch_start_text));
//        getActivity().stopService(new Intent(getActivity(), StopWatchService.class));

    }


    /*
    * Register to events...
    */
    @Override
    public void onStart(){
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
        scopedBus.unregister(eventListener);
        if (leftStarted || rightStarted) {
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            notificationManager.notify(notification_id, notification);
        } else {
            getActivity().stopService(new Intent(getActivity(), StopWatchService.class));
        }

    }

    private Long elapsedTimeL, elapsedTimeR;


    @OnClick(R.id.save_btn)
    public void onSaveBtnClicked() {
        Log.d(TAG, "save btn clicked");
        Dao<FeedDao, Integer> feedDao;
        FeedDao daoObject;
        Long time = dateTimeHeader.getEventTime();
        //stop the service....
        getActivity().stopService(new Intent(getActivity(), StopWatchService.class));
        if (leftStarted) {
            deltaL += (System.currentTimeMillis() - elapsedTimeL)/1000;
        } else if (rightStarted) {
            deltaR += (System.currentTimeMillis() - elapsedTimeR)/1000;

        }
//        deltaR += (System.currentTimeMillis() - elapsedTimeR)/1000;
//        deltaL += (System.currentTimeMillis() - elapsedTimeL)/1000;

        Log.d(TAG, " deltaL " + deltaL + " deltaR " + deltaR);


        try {
            feedDao = babyLoggerORMLiteHelper.getFeedDao();


            daoObject  = new FeedDao(FeedType.BREAST,
                    "" , -1.0,
                    deltaL,
                    deltaR, notes.getText().toString(),
                    time);
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
            Log.d(TAG, "created object " + daoObject);
            scopedBus.post(new FeedItemCreatedEvent());
            leftStarted = false;
            rightStarted = false;
            soundMgr.stopEndlessAlarm();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
