package com.rorlig.babylog.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.rorlig.babylog.otto.ScopedBus;
import com.rorlig.babylog.otto.TimersEvent;
import com.rorlig.babylog.otto.TimersStartEvent;
import com.rorlig.babylog.otto.events.timer.TimerEvent;
import com.squareup.otto.Subscribe;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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
    private ScheduledFuture<?> rightFuture;
    private ScheduledFuture<?> leftFuture;

    public enum Timers {
        LEFT, RIGHT;
    }

    private TimerBeats leftTimerTask;
    private TimerBeats rightTimerTask;

    ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (!isRunning)
            isRunning = true;
        else return START_STICKY;


        leftTimerTask = new TimerBeats(Timers.LEFT);
        rightTimerTask = new TimerBeats(Timers.RIGHT);

        scopedBus.register(this);



//        scheduler.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                scopedBus.post(new TimerEvent());
//            }
//        }, 0, 1, TimeUnit.SECONDS);


        leftFuture =
                scheduler.scheduleAtFixedRate(leftTimerTask, 1L, 1L, TimeUnit.SECONDS);

//        rightFuture =
//                scheduler.scheduleAtFixedRate(rightTimerTask, 1L, 1L, TimeUnit.SECONDS);

        return START_STICKY;
    }

    private class TimerBeats implements Runnable{

        private int lvalue, rvalue;
        private boolean isRunning;
        private Timers tag;

        public TimerBeats(Timers timers) {
            this.lvalue = 0;
            this.rvalue = 0;
            this.isRunning = false;
            this.tag = timers;
        }

        @Override
        public void run() {
//            ++value;
            Log.d(TAG, "run()");
//            Log.d(TAG, "value " + value + " tag " + tag);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
//                    scopedBus.post(new TimersEvent(tag, value));

                }
            });
        }


    }

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
    public void onTimerEvent(TimersStartEvent event) {
        Log.d(TAG, "onTimerEvent " + event.getTag());
        if (event.getTag().equals("left")) {
            try {
                leftFuture.get();
                rightFuture.cancel(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            try {
                rightFuture.get();
                leftFuture.cancel(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            leftFuture.cancel(false);
        }

    }

}
