package com.rorlig.babylog.ui.fragment.feed;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.rorlig.babylog.otto.LeftEvent;
import com.rorlig.babylog.otto.NursingFragmentRestartEvent;
import com.rorlig.babylog.otto.RightEvent;
import com.rorlig.babylog.otto.StopWatchLeftEvent;
import com.rorlig.babylog.otto.StopWatchRightEvent;
import com.rorlig.babylog.otto.TimersEvent;
import com.rorlig.babylog.otto.TimersStartEvent;
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
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

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


//    Typeface typeface;

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
    private CompositeSubscription subscriptions;
    private Subscription leftSubscription;
    private Subscription rightSubscription;

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        Log.d(TAG, "onActivityCreated");
//        typeface=Typeface.createFromAsset(getActivity().getAssets(),
//                "fonts/proximanova_light.ttf");

        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.BLUE);







        soundMgr = SoundManager.getInstance(getActivity());

        setUpViews();




//        scopedBus.post(new Feed("Bottle Feed"));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated");

        notificationManager = (NotificationManager)
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        subscriptions = new CompositeSubscription();







    }

    private void setUpViews() {

        Log.d(TAG, "setUpViews");
        leftTextView = (TextView) leftButton.findViewById(R.id.startLText);
        rightTextView = (TextView) rightButton.findViewById(R.id.startRText);

        hourLTextView = (TextView) leftButton.findViewById(R.id.hour);
        hourRTextView = (TextView) rightButton.findViewById(R.id.hour);

        minuteLTextView = (TextView) leftButton.findViewById(R.id.minute);
        minuteRTextView = (TextView) rightButton.findViewById(R.id.minute);

        secondLTextView = (TextView) leftButton.findViewById(R.id.second);
        secondRTextView = (TextView) rightButton.findViewById(R.id.second);


        Bundle args = getArguments();
        if (args!=null && args.getBoolean("fromNotification", false)) {
                //restore times from the bundle...
                Log.d(TAG, "not null args and fromNotification");
//                hourLTextView.setText(args.getString("hourLTextView", "00"));
//                minuteLTextView.setText(args.getString("minuteLTextView", "00"));
//                secondLTextView.setText(args.getString("secondLTextView", "00"));
//                hourRTextView.setText(args.getString("hourRTextView", "00"));
//                minuteRTextView.setText(args.getString("minuteRTextView", "00"));
//                secondRTextView.setText(args.getString("secondRTextView", "00"));
//                elapsedTimeL = args.getLong("elapsedTimeL", 0L);
//                elapsedTimeR = args.getLong("elapsedTimeR", 0L);
//                leftStarted = args.getBoolean("leftStarted");
//                rightStarted = args.getBoolean("rightStarted");
                Log.d(TAG, "leftStarted " + leftStarted + " rightStarted " + rightStarted);
                scopedBus.post(new NursingFragmentRestartEvent());
                if (leftStarted) {
                    Log.d(TAG, "setting text leftStarted");
//                    Log.d(TAG, "leftSubscription " + leftSubscription.isUnsubscribed());
                    leftTextView.setText("Stop");
                } else {
                    rightTextView.setText("Stop");
                }

                notificationManager.cancel(notification_id);

        }

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
        public void onLeftEvent(LeftEvent event) {

            hourLTextView.setText(event.getHours());
            minuteLTextView.setText(event.getMinutes());
            secondLTextView.setText(event.getSeconds());
            leftValue = Integer.parseInt(event.getSeconds()) + Integer.parseInt(event.getMinutes()) * 60 + Integer.parseInt(event.getHours())*60*60;
        }

        @Subscribe
        public void onRightEvent(RightEvent event) {
            hourRTextView.setText(event.getHours());
            minuteRTextView.setText(event.getMinutes());
            secondRTextView.setText(event.getSeconds());
            rightValue = Integer.parseInt(event.getSeconds()) + Integer.parseInt(event.getMinutes()) * 60 + Integer.parseInt(event.getHours())*60*60;

        }

//        @Subscribe
//        public void onTimerEvent(TimersEvent event) {
//            Log.d(TAG, event.toString());
//        }
//
//        @Subscribe
//        public void onTimeEvent(TimerEvent timerEvent) {
////            Log.d(TAG, "onTimeTimeEvent");
//            soundMgr.doTick();
//
//            Long currentTime = System.currentTimeMillis();
//            int diff=0;
//
////            Log.d(TAG, "leftStarted " + leftStarted + " rightStartded: " + rightStarted + " deltaL " + deltaL + " deltaR " + deltaR);
////            Log.d(TAG, "diff " + diff + " elapsedTimeL " + elapsedTimeL + " elapsedTimeR " + elapsedTimeR);
//            if (leftStarted ) {
//                diff = (int)((currentTime - elapsedTimeL)/1000 + deltaL);
//            } else  {
//                diff = (int)((currentTime - elapsedTimeR)/1000 + deltaR);
//
//            }
////            Log.d(TAG, " diff in seconds " + diff);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, 0);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.MILLISECOND, 0);
//            calendar.set(Calendar.SECOND, diff);
//            String time =  new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
//            String hours = time.substring(0, 2);
//            String minutes = time.substring(3,5);
//            String seconds = time.substring(6,8);
//            Log.d(TAG, "hours "  + hours + " minutes " + minutes + " seconds "  + seconds);
//
//            if (leftStarted) {
//
//                hourLTextView.setText(hours);
//            minuteLTextView.setText(minutes);
//            secondLTextView.setText(seconds);
//
//            } else  {
//
//                hourRTextView.setText(hours);
//                minuteRTextView.setText(minutes);
//                secondRTextView.setText(seconds);
//
//            }
////            hourTextView.setText(hours);
////            minuteTextView.setText(minutes);
////            secondTextView.setText(seconds);
//
//
//
////            int seconds, minutes, hours;
//
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_base, menu);
    }

    private Long deltaL = 0L;
    private Long deltaR = 0L;
    private int leftValue = 0;
    private int rightValue = 0;

    private Long currentLeftValue = 0L;
    private Long currentRightValue = 0L;

    @OnClick(R.id.left)
    public void leftButtonClicked() {

        scopedBus.post(new StopWatchLeftEvent());
        leftStarted=!leftStarted;
        if (leftStarted) {
            if (rightStarted) {
                rightStarted = false;
            }
            rightTextView.setText(getResources().getString(R.string.stopwatch_start_text));
            leftTextView.setText(getResources().getString(R.string.stopwatch_stop_text));
        } else {
            resetTextViews();
        }


//        if (leftStarted) {
//
//            leftValue+=currentLeftValue;
//            leftStarted = false;
//            unsubscribeLeftTimer();
//            resetTextViews();
//
//            return;
//        }
//
//        leftStarted = true;
//
//        rightTextView.setText(getResources().getString(R.string.stopwatch_start_text));
//        leftTextView.setText(getResources().getString(R.string.stopwatch_stop_text));
//
//        if (leftSubscription==null || leftSubscription.isUnsubscribed()) {
//
//            leftSubscription =  Observable.timer(1L, 1L, TimeUnit.SECONDS)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            s -> {
//                                currentLeftValue = s;
//
//
//                                System.out.println(currentLeftValue);
//
//                                String time = getTime((int) (leftValue + currentLeftValue));
//                                String hours = time.substring(0, 2);
//                                String minutes = time.substring(3,5);
//                                String seconds = time.substring(6,8);
//                                Log.d(TAG, "LEFT TIMER hours "  + hours + " minutes " + minutes + " seconds "  + seconds);
//                                hourLTextView.setText(hours);
//                                minuteLTextView.setText(minutes);
//                                secondLTextView.setText(seconds);
//                            });
//
//        }
//
//
//
//        if (rightSubscription!=null && !rightSubscription.isUnsubscribed()) {
//            Log.d(TAG, "unsubscribing right");
//            rightStarted = false;
//            rightSubscription.unsubscribe();
//            rightValue+=currentRightValue;
//        }




//        String time = getTime(leftValue);
//        String hours = time.substring(0, 2);
//        String minutes = time.substring(3,5);
//        String seconds = time.substring(6,8);
//        Log.d(TAG, "hours "  + hours + " minutes " + minutes + " seconds "  + seconds);
//        hourLTextView.setText(hours);
//        minuteLTextView.setText(minutes);
//        secondLTextView.setText(seconds);




    }




    private String getTime(int timeInSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, timeInSeconds);
        return new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
    }


    @OnClick(R.id.right)
    public void rightButtonClicked() {
        Log.d(TAG, "right button clicked");
        rightStarted=!rightStarted;
        if (rightStarted) {
            if (leftStarted) {
                leftStarted = false;
            }
            leftTextView.setText(getResources().getString(R.string.stopwatch_start_text));
            rightTextView.setText(getResources().getString(R.string.stopwatch_stop_text));
        } else {
            resetTextViews();
        }
        scopedBus.post(new StopWatchRightEvent());

//        if (rightStarted) {
//            rightValue+=currentRightValue;
//            rightStarted = false;
//            unsubscribeRightTimer();
//            resetTextViews();
//            return;
//        }
//
//        rightStarted = true;
//
//        leftTextView.setText(getResources().getString(R.string.stopwatch_start_text));
//
//        rightTextView.setText(getResources().getString(R.string.stopwatch_stop_text));
//
//        if (rightSubscription==null || rightSubscription.isUnsubscribed()) {
//            Log.d(TAG, "start right subscription");
//            rightSubscription = Observable.timer(1L, 1L, TimeUnit.SECONDS)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            s -> {
//
//                                currentRightValue = s;
//                                System.out.println(currentRightValue);
//
//                                String time = getTime((int) (rightValue + currentRightValue));
//                                String hours = time.substring(0, 2);
//                                String minutes = time.substring(3,5);
//                                String seconds = time.substring(6,8);
//                                Log.d(TAG, "hours "  + hours + " minutes " + minutes + " seconds "  + seconds);
//                                hourRTextView.setText(hours);
//                                minuteRTextView.setText(minutes);
//                                secondRTextView.setText(seconds);
//
//
//                            }
//
//
//                    );
//
//        }
//
//        if (leftSubscription!=null && !leftSubscription.isUnsubscribed()) {
//            Log.d(TAG, "unsubscribe left");
//            leftStarted = false;
//            leftValue+=currentLeftValue;
//            leftSubscription.unsubscribe();
//        }








    }


    /*
    * Register to events...
    */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        scopedBus.register(eventListener);
        Intent intent = new Intent(getActivity(), StopWatchService.class);
        getActivity().startService(intent);
//        intent.putExtra("stopwatch", "left");
////        if (!leftStarted) {
//            getActivity().startService(intent);
//        }
    }

    /*
     * Unregister from events ...
     */
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");

        Log.d(TAG, " leftStarted " + leftStarted + " rightStarted " + rightStarted);
        scopedBus.unregister(eventListener);
        if (leftStarted || rightStarted) {

            intent = new Intent(getActivity(), FeedingActivity.class);
            intent.putExtra("fromNotification", true);

//            intent.putExtra("hourLTextView", hourLTextView.getText().toString());
//            intent.putExtra("minuteLTextView", minuteLTextView.getText().toString());
//            intent.putExtra("secondLTextView", secondLTextView.getText().toString());
//
//            intent.putExtra("hourRTextView", hourRTextView.getText().toString());
//            intent.putExtra("minuteRTextView", minuteRTextView.getText().toString());
//            intent.putExtra("secondRTextView", secondRTextView.getText().toString());
//
//            intent.putExtra("elapsedTimeL", elapsedTimeL);
//            intent.putExtra("elapsedTimeR", elapsedTimeR);
//            intent.putExtra("leftStarted", leftStarted);
//            intent.putExtra("rightStarted", rightStarted);



//            intent.putExtra("rightTimer", rightTextView.getText().toString());


            pIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

            // build notification
// the addAction re-use the same intent to keep the example short
            notification  = new Notification.Builder(getActivity())
                    .setContentTitle("Breast Feeding")
                    .setContentIntent(pIntent)
                    .setSmallIcon(R.drawable.ic_launcher_icon)
                    .build();



//            notification.flags |= Notification.FL;
            notificationManager.notify(notification_id, notification);
//            notificationManager
        } else {
            unsubscribeTimers();
//            getActivity().stopService(new Intent(getActivity(), StopWatchService.class));
        }

    }

    private Long elapsedTimeL, elapsedTimeR;


    @OnClick(R.id.save_btn)
    public void onSaveBtnClicked() {
        Log.d(TAG, "save btn clicked");
        Dao<FeedDao, Integer> feedDao;
        FeedDao daoObject;
        Date date = dateTimeHeader.getEventTime();
        Log.d(TAG, " currentLeftValue " + currentLeftValue + " currentRightValue " + currentRightValue);
        Log.d(TAG, " leftValue " + leftValue + " rightValue " + rightValue);
        long rightL = rightValue;
        long leftL = leftValue;



        try {
            feedDao = babyLoggerORMLiteHelper.getFeedDao();


            daoObject  = new FeedDao(FeedType.BREAST,
                    "" , -1.0,
                    leftL,
                    rightL, notes.getText().toString(),
                    date);
            feedDao.create(daoObject);
            Log.d(TAG, "created object " + daoObject);
            leftStarted = false;
            rightStarted = false;
//            unsubscribeTimers();
            getActivity().stopService(new Intent(getActivity(), StopWatchService.class));
            scopedBus.post(new FeedItemCreatedEvent());

            soundMgr.stopEndlessAlarm();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void unsubscribeTimers() {
        if (leftSubscription!=null&&!leftSubscription.isUnsubscribed()) {
            leftSubscription.unsubscribe();
        }

        if (rightSubscription!=null&&!rightSubscription.isUnsubscribed()) {
            rightSubscription.unsubscribe();
        }


        
    }


    private void unsubscribeLeftTimer() {
        if (leftSubscription!=null&&!leftSubscription.isUnsubscribed()) {
            leftSubscription.unsubscribe();
        }
    }

    private void unsubscribeRightTimer() {
        if (rightSubscription!=null&&!rightSubscription.isUnsubscribed()) {
            rightSubscription.unsubscribe();
        }
    }

    private void resetTextViews() {
        rightTextView.setText(getResources().getString(R.string.stopwatch_start_text));
        leftTextView.setText(getResources().getString(R.string.stopwatch_start_text));
    }


}
