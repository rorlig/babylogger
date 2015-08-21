package com.rorlig.babyapp.dagger;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.rorlig.babyapp.BabyLoggerApplication;
import com.rorlig.babyapp.db.BabyLoggerORMLiteHelper;
import com.rorlig.babyapp.otto.BusProvider;
import com.rorlig.babyapp.otto.ScopedBus;
import com.squareup.otto.Bus;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Modules to be injected at the application scope
 */

@Module(injects= {
                BabyLoggerApplication.class
            }, library=true)
public class ApplicationModule
{

    private final BabyLoggerApplication application;

    public ApplicationModule(BabyLoggerApplication paramBabyLoggerApplication)
    {
        this.application = paramBabyLoggerApplication;
    }





    /*
     * @returns alarm manager
     */

    @Provides
    @Singleton
    AlarmManager provideAlarmManager(@ForApplication Context paramContext)
    {
        return (AlarmManager)paramContext.getSystemService(Context.ALARM_SERVICE);
    }


    /*
     * @returns Application Context
     */
    @ForApplication
    @Provides
    @Singleton
    Context provideApplicationContext()
    {
        return this.application;
    }

    /*
     * @returns otto Bus
     */
    @Provides
    @Singleton
    Bus provideBus() {
        return BusProvider.getInstance();
    }

//    @Cache
//    @Provides
//    File provideCacheDirectory(@ForApplication Context paramContext){
//        return paramContext.getCacheDir();
//    }

//    @Provides
//    @Singleton
//    ContentResolver provideContentResolver(){
//        return this.application.getContentResolver();
//    }

//    @Data
//    @Provides
//    File provideDataDirectory(@ForApplication Context paramContext){
//        return paramContext.getFilesDir();
//    }

//    @Provides
//    InputMethodManager providesInputMethodManager(@ForApplication Context paramContext){
//        return (InputMethodManager)paramContext.getSystemService(
//                Context.INPUT_METHOD_SERVICE);
//    }
//    @Provides
//    @Singleton
//    ScheduleDatabase provideDatabase(@ForApplication Context paramContext, Timber paramTimber){
//        return new ScheduleDatabase(paramContext, paramTimber);
//    }
//

//    /*
//     * RestAdapter
//     */
//    @Provides
//    @Singleton
//    public RestAdapter getRestAdapter() {
//        RestAdapter.Builder builder = new RestAdapter.Builder();
//        if (BuildConfig.DEBUG) {
//            builder.setLogLevel(RestAdapter.LogLevel.FULL);
//        }
//        builder.setServer("http://54.225.249.57:3000/");  //todo test configuration...
//        return builder.build();
//    }

//    /*
//     * Rest interface for the Greet Event APIs...
//     */
//    @Provides
//    @Singleton
//    GreetService provideGreetService(RestAdapter paramRestAdapter){
//        return (GreetService)paramRestAdapter.create(GreetService.class);
//    }
//
//    /*
//     * Rest Interface for the Greet Account Handling APIs..
//     */
//
//    @Provides
//    @Singleton
//    AccountService provideAccountService(RestAdapter paramRestAdapter) {
//        return (AccountService)paramRestAdapter.create(AccountService.class);
//    }

    @Provides
    @Singleton
    Downloader provideDownloader(@ForApplication Context paramContext){
        return new OkHttpDownloader(paramContext);
    }

//    @Provides
//    EventInfo provideEventInfo(EventManager paramEventManager){
//        return paramEventManager.getEventInfo();
//    }

    @Provides
    @Singleton
    ExecutorService provideExecutorService(){
        return Executors.newCachedThreadPool();//todo
    }

//    @Provides
//    @Singleton
// todo
//    GoogleCloudMessaging provideGoogleCloudMessaging(@ForApplication Context paramContext){
//        return GoogleCloudMessaging.getInstance(paramContext);
//    }

//    @Provides
//    @Singleton
//    public GsonConverter provideGsonConverter() {
//        return new GsonConverter(new Gson());
//    }

    @Provides
    public Gson providesGson(){
        return new Gson();
    }

//    @Provides
//    @Singleton
//    HttpResponseCache provideHttpResponseCache(@Cache File paramFile)
//    {
//        try
//        {
//            HttpResponseCache localHttpResponseCache = new HttpResponseCache(new File(paramFile, "okhttp"), 52428800L);
//            return localHttpResponseCache;
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    @MainThread
//    @Provides
//    @Singleton
//    Executor provideMainThread() {
//        return new MainThreadExecutor();
//    }

//    @Provides
//    @Singleton
//    NotificationManager provideNotificationManager(@ForApplication Context paramContext) {
//        return (NotificationManager)paramContext.getSystemService("notification");
//    }

//    @Provides
//    @Singleton
//    OkHttpClient provideOkHttpClient(HttpResponseCache paramHttpResponseCache) {
//        OkHttpClient localOkHttpClient = new OkHttpClient();
//        if (paramHttpResponseCache != null)
//            localOkHttpClient.setResponseCache(paramHttpResponseCache);
//        return localOkHttpClient;
//    }

    @Provides
    @Singleton
    Picasso providePicasso(@ForApplication Context paramContext, Downloader paramDownloader) {
        return new Picasso.Builder(paramContext).downloader(paramDownloader).build();
    }

    @Provides
    @Singleton
    ScopedBus provideScopedBus() {
        return new ScopedBus();
    }

//    @Provides
//    @Singleton
//    Timber provideTimber() {
//        return Timber.PROD;
//    }

//    @Provides
//    TimeZone provideTimeZone(EventManager paramEventManager){
//        EventInfo localEventInfo = paramEventManager.getEventInfo();
//        if (localEventInfo != null)
//            return localEventInfo.getTimeZone();
//        return TimeZone.getDefault();
//    }

    @Provides
    Executor providerExecutor(ExecutorService paramExecutorService){
        return paramExecutorService;
    }

    @Provides
    LocationManager providesLocationManager(@ForApplication Context context) {
        return  (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }


    /*
     * Provides a network event client for actual implementation....
     */

//    @Provides
//    EventClient provideEventClient() {
//        return new NetworkEventClient();
//    }
//
//    /*
//     * Returns the JobManager for the application to add network tasks to it...
//     */
//    @Provides
//    JobManager providesJobManager(){return application.getJobManager();}
//    @Provides
//    LocationClient providesLocationClient(@ForApplication Context context){
//        return new LocationClient(c);
//    }

//    @Provides
//    @Singleton
//    FilterManager providesFilterManager(@ForApplication Context context){
//       return new FilterManager(context);
//    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(@ForApplication Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    @Provides
    @Singleton
    BabyLoggerORMLiteHelper providesORMLiteHelper(@ForApplication Context context){

        return OpenHelperManager.getHelper(context, BabyLoggerORMLiteHelper.class);

    }

}
