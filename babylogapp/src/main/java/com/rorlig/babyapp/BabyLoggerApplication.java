package com.rorlig.babyapp;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.rorlig.babyapp.dagger.ApplicationModule;
import com.rorlig.babyapp.dagger.ObjectGraphApplication;
import com.rorlig.babyapp.parse_dao.Baby;
import com.rorlig.babyapp.parse_dao.DiaperChange;
import com.rorlig.babyapp.parse_dao.Feed;
import com.rorlig.babyapp.parse_dao.Growth;
import com.rorlig.babyapp.parse_dao.HeadCircumferenceToAge;
import com.rorlig.babyapp.parse_dao.HeightToAge;
import com.rorlig.babyapp.parse_dao.Milestones;
import com.rorlig.babyapp.parse_dao.Sleep;
import com.rorlig.babyapp.parse_dao.WeightToAge;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;

//import com.path.android.jobqueue.JobManager;
//import com.path.android.jobqueue.config.Configuration;
//import com.path.android.jobqueue.log.CustomLogger;

/**
 * Created by gaurav
 */
//@ReportsCrashes(
//        formKey = "", // will not be used
//        mailTo = "guptgaurav@gmail.com"
//)
public class BabyLoggerApplication extends Application implements ObjectGraphApplication {
    private ObjectGraph objectGraph;
    private final String TAG = "BabyLoggerApplication";

//    public String APPLICATION_ID = "g0bRVs758HB3brdGBQaar9o6VxvReNPENWBwhQPY";
//
//    public String CLIENT_KEY = "7O6De7cnIwoNo57UZZ6hxE9SiGgk24ESnuTY42LM";
//    private JobManager jobManager;

    @Override
    public ObjectGraph getApplicationGraph() {
//        initializeDagger();
        Log.d(TAG, "getApplicaitonGraph");
        if (objectGraph==null) initializeDagger();

        return objectGraph;
    }

    @Override
    public void inject(Object paramObject) {
       initializeDagger();
       objectGraph.inject(paramObject);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initializeDagger();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Register subclasses...
        ParseObject.registerSubclass(DiaperChange.class);

        ParseObject.registerSubclass(Feed.class);

        ParseObject.registerSubclass(Growth.class);

        ParseObject.registerSubclass(Milestones.class);

        ParseObject.registerSubclass(Sleep.class);

        ParseObject.registerSubclass(Baby.class);

        ParseObject.registerSubclass(WeightToAge.class);

        ParseObject.registerSubclass(HeightToAge.class);

        ParseObject.registerSubclass(HeadCircumferenceToAge.class);







        // Register subclasses...
//        ParseObject.registerSubclass(DiaperChange.class);
        // Add your initialization code here
        Parse.initialize(this, getString(R.string.application_id),getString(R.string.client_key));

        ParseUser.enableRevocableSessionInBackground();

//        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        ParseInstallation.getCurrentInstallation().saveInBackground();



//        DualCacheContextUtils.setContext(getApplicationContext());

//        configureJobManager();
//        ACRA.init(this);

    }

    protected List<Object> getModules(){
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = new ApplicationModule(this);
//        arrayOfObject[1] = new SchedulerModule();   //todo add test module...
        return Arrays.asList(arrayOfObject);
    }

    protected final void initializeDagger() {
        Log.d(TAG, "initializeDagger");
        if (objectGraph == null) {
            Log.d(TAG, "make object graph");
            ApplicationModule module = new ApplicationModule(this);
//            if (BuildConfig.DEBUG){
//                TestModule testModule  = new TestModule(this);
                objectGraph = ObjectGraph.create(
                        module);
//                objectGraph.inject(GetUserJob.class);
//            }

//            objectGraph.validate();
//            objectGraph.inject(this);
        } else {
            Log.d(TAG, "not null");
        }

    }

//    private void configureJobManager() {
//        Configuration configuration = new Configuration.Builder(this)
//                .customLogger(new CustomLogger() {
//                    private static final String TAG = "JOBS";
//                    @Override
//                    public boolean isDebugEnabled() {
//                        return true;
//                    }
//
//                    @Override
//                    public void d(String text, Object... args) {
//                        Log.d(TAG, String.format(text, args));
//                    }
//
//                    @Override
//                    public void e(Throwable t, String text, Object... args) {
//                        Log.e(TAG, String.format(text, args), t);
//                    }
//
//                    @Override
//                    public void e(String text, Object... args) {
//                        Log.e(TAG, String.format(text, args));
//                    }
//                })
//                .minConsumerCount(1)//always keep at least one consumer alive
//                .maxConsumerCount(3)//up to 3 consumers at a time
//                .loadFactor(3)//3 jobs per consumer
//                .consumerKeepAlive(120)//wait 2 minute
//                .build();
//        jobManager = new JobManager(this, configuration);
//    }
//
//    public JobManager getJobManager() {
//        return jobManager;
//    }




}
