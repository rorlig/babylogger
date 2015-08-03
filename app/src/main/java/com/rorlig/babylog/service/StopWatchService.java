package com.rorlig.babylog.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.LeftEvent;
import com.rorlig.babylog.otto.NursingFragmentRestartEvent;
import com.rorlig.babylog.otto.RightEvent;
import com.rorlig.babylog.otto.ScopedBus;
import com.rorlig.babylog.otto.StopWatchLeftEvent;
import com.rorlig.babylog.otto.StopWatchRightEvent;
import com.rorlig.babylog.otto.TimersEvent;
import com.rorlig.babylog.otto.TimersStartEvent;
import com.rorlig.babylog.otto.events.timer.TimerEvent;
import com.rorlig.babylog.ui.fragment.feed.NursingFeedFragment;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  @author gaurav gupta
 *  StopWatchService
 */
public class StopWatchService extends InjectableService {

    private static Timer timer;
    private String TAG = "StopWatchService";

    private boolean isRunning = false;

    @Inject
    ScopedBus bus;
//    private ScheduledFuture<?> rightFuture;
//    private ScheduledFuture<?> leftFuture;
    private boolean leftStarted;
    private int leftValue;
    private Long currentLeftValue;
    private Subscription leftSubscription;
    private Subscription rightSubscription;
    private boolean rightStarted;
    private int rightValue;
    private Long currentRightValue;

    public enum Timers {
        LEFT, RIGHT;
    }

//    private TimerBeats leftTimerTask;
//    private TimerBeats rightTimerTask;

//    ScheduledExecutorService scheduler =
//            Executors.newSingleThreadScheduledExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (!isRunning)
            isRunning = true;
        else return START_STICKY;


//        leftTimerTask = new TimerBeats(Timers.LEFT);
//        rightTimerTask = new TimerBeats(Timers.RIGHT);

        scopedBus.register(this);



//        scheduler.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                scopedBus.post(new TimerEvent());
//            }
//        }, 0, 1, TimeUnit.SECONDS);


//        leftFuture =
//                scheduler.scheduleAtFixedRate(leftTimerTask, 1L, 1L, TimeUnit.SECONDS);

//        rightFuture =
//                scheduler.scheduleAtFixedRate(rightTimerTask, 1L, 1L, TimeUnit.SECONDS);

        return START_STICKY;
    }

//    private class TimerBeats implements Runnable{
//
//        private int lvalue, rvalue;
//        private boolean isRunning;
//        private Timers tag;
//
//        public TimerBeats(Timers timers) {
//            this.lvalue = 0;
//            this.rvalue = 0;
//            this.isRunning = false;
//            this.tag = timers;
//        }
//
//        @Override
//        public void run() {
////            ++value;
//            Log.d(TAG, "run()");
////            Log.d(TAG, "value " + value + " tag " + tag);
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
////                    scopedBus.post(new TimersEvent(tag, value));
//
//                }
//            });
//        }
//
//
//    }

//    private class MainTask extends TimerTask
//    {
//        private int value;
//        private boolean isRunning;
//
//        public MainTask() {
//            this.value = 0;
//            this.isRunning = false;
//        }
//
//        public void run()
//        {
//
//            updateTimer();
//        }
//
//
//    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        unsubscribeTimers();
//        timer.cancel();
//        timer.purge();
    }

//    private void updateTimer() {
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "posting event out");
//                scopedBus.post(new TimerEvent());
//            }
//        });
//
////        scopedBus.post(new TimerEvent());
//
//    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Subscribe
    public void onLeftButtonClicked(StopWatchLeftEvent event) {

        if (leftStarted) {

            leftValue+=currentLeftValue;
            leftStarted = false;
            unsubscribeLeftTimer();
            resetTextViews();

            return;
        }

        leftStarted = true;

//        rightTextView.setText(getResources().getString(R.string.stopwatch_start_text));
//        leftTextView.setText(getResources().getString(R.string.stopwatch_stop_text));

        if (leftSubscription==null || leftSubscription.isUnsubscribed()) {

            leftSubscription =  Observable.timer(1L, 1L, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            s -> {
                                currentLeftValue = s;


                                System.out.println(currentLeftValue);

                                String time = getTime((int) (leftValue + currentLeftValue));
                                String hours = time.substring(0, 2);
                                String minutes = time.substring(3,5);
                                String seconds = time.substring(6,8);
                                Log.d(TAG, "LEFT TIMER hours "  + hours + " minutes " + minutes + " seconds "  + seconds);
                                scopedBus.post(new LeftEvent(hours, minutes, seconds));

                            });

        }



        if (rightSubscription!=null && !rightSubscription.isUnsubscribed()) {
            Log.d(TAG, "unsubscribing right");
            rightStarted = false;
            rightSubscription.unsubscribe();
            rightValue+=currentRightValue;
        }

    }



    private String getTime(int timeInSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, timeInSeconds);
        return new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
    }


    private void resetTextViews() {

    }



    @Subscribe
    public void onRightButtonClicked(StopWatchRightEvent event) {
        if (rightStarted) {

            rightValue+=currentLeftValue;
            rightStarted = false;
            unsubscribeRightTimer();
            resetTextViews();

            return;
        }

        rightStarted = true;

//        rightTextView.setText(getResources().getString(R.string.stopwatch_start_text));
//        leftTextView.setText(getResources().getString(R.string.stopwatch_stop_text));

        if (rightSubscription==null || rightSubscription.isUnsubscribed()) {

            rightSubscription =  Observable.timer(1L, 1L, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            s -> {
                                currentRightValue = s;


                                System.out.println(currentRightValue);

                                String time = getTime((int) (rightValue + currentRightValue));
                                String hours = time.substring(0, 2);
                                String minutes = time.substring(3,5);
                                String seconds = time.substring(6,8);
                                Log.d(TAG, "RIGHT TIMER hours "  + hours + " minutes " + minutes + " seconds "  + seconds);
                                scopedBus.post(new RightEvent(hours, minutes, seconds));

                            });

        }



        if (leftSubscription!=null && !leftSubscription.isUnsubscribed()) {
            Log.d(TAG, "unsubscribing right");
            leftStarted = false;
            leftSubscription.unsubscribe();
            leftValue+=currentLeftValue;
        }


    }

    @Subscribe
    public void nursingFragmentRestarted(NursingFragmentRestartEvent event) {
        Log.d(TAG, "nursingFragmentRestarted");
        String time = getTime((int) (leftValue + currentLeftValue));
        Log.d(TAG, "time " + time);
        String hours = time.substring(0, 2);
        String minutes = time.substring(3,5);
        String seconds = time.substring(6, 8);
        scopedBus.post(new LeftEvent(hours, minutes, seconds));


        time = getTime((int) (rightValue + currentRightValue));
        Log.d(TAG, "time " + time);

        hours = time.substring(0, 2);
        minutes = time.substring(3, 5);
        seconds = time.substring(6, 8);
        scopedBus.post(new RightEvent(hours, minutes, seconds));
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

}
