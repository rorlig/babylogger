package com.rorlig.babylog.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.rorlig.babylog.otto.TimerEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gaurav on 6/3/15.
 */
public class StopWatchService extends InjectableService {

    private static Timer timer;
    private String TAG = "StopWatchService";

    private boolean isRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (!isRunning)
            isRunning = true;
        else return START_STICKY;

//        if (timer==null)
            timer = new Timer();

        timer.scheduleAtFixedRate(new MainTask(), 0, 1000);

//        scopedBus.post(new TimerEvent());

        return START_STICKY;
    }

    private class MainTask extends TimerTask
    {
        public void run()
        {

            updateTimer();
        }


    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }

    private void updateTimer() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "posting event out");
                scopedBus.post(new TimerEvent());
            }
        });

//        scopedBus.post(new TimerEvent());

    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

}
