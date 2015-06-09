package com.rorlig.babylog.dagger;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;

//import com.rorlig.babylog.jobs.user.GetUserJob;
//import com.rorlig.babylog.retrofit.NetworkEventClient;
import com.rorlig.babylog.scheduler.TypeFaceManager;
import com.rorlig.babylog.service.StopWatchService;
import com.rorlig.babylog.ui.activity.BabyLogActivity;
//import com.rorlig.babylog.ui.activity.CheckInActivity;
//import com.rorlig.babylog.ui.activity.ConnectionDetailActivity;
//import com.rorlig.babylog.ui.activity.EventDetailActivity;
import com.rorlig.babylog.ui.activity.BottleFeedingActivity;
import com.rorlig.babylog.ui.activity.DiaperChangeActivity;
//import com.rorlig.babylog.ui.activity.MapActivity;
//import com.rorlig.babylog.ui.activity.MessageBoardListActivity;
//import com.rorlig.babylog.ui.activity.MessageDetailActivity;
//import com.rorlig.babylog.ui.activity.MessagesActivity;
//import com.rorlig.babylog.ui.activity.MessagingActivity;
//import com.rorlig.babylog.ui.activity.SearchActivity;
//import com.rorlig.babylog.ui.activity.SettingsActivity;
//import com.rorlig.babylog.ui.activity.UserListActivity;
//import com.rorlig.babylog.ui.activity.UserProfileActivity;
//import com.rorlig.babylog.ui.adapter.EventAdapter;
//import com.rorlig.babylog.ui.adapter.MessageAdapter;
//import com.rorlig.babylog.ui.fragment.CheckInDialogFragment;
//import com.rorlig.babylog.ui.fragment.ConnectionDetailFragment;
//import com.rorlig.babylog.ui.fragment.ConnectionsFragment;
//import com.rorlig.babylog.ui.fragment.EventDetailFragment;
//import com.rorlig.babylog.ui.fragment.FilterFragment;
//import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;
import com.rorlig.babylog.ui.activity.FeedingActivity;
import com.rorlig.babylog.ui.activity.GrowthActivity;
import com.rorlig.babylog.ui.activity.HomeActivity;
import com.rorlig.babylog.ui.activity.LandingActivity;
import com.rorlig.babylog.ui.activity.NursingFeedActivity;
import com.rorlig.babylog.ui.adapter.ContactsAdapter;
import com.rorlig.babylog.ui.adapter.DiaperChangeAdapter;
import com.rorlig.babylog.ui.adapter.FeedAdapter;
import com.rorlig.babylog.ui.adapter.HomeItemAdapter;
import com.rorlig.babylog.ui.adapter.LogItemAdapter;
import com.rorlig.babylog.ui.fragment.about.AboutFragment;
import com.rorlig.babylog.ui.fragment.action.ActionsSelectFragment;
import com.rorlig.babylog.ui.fragment.contact.ContactSelectFragment;
import com.rorlig.babylog.ui.fragment.contact.ContactsFragment;
import com.rorlig.babylog.ui.fragment.contact.ContactsPagerListFragment;
import com.rorlig.babylog.ui.fragment.datetime.DatePickerFragment;
import com.rorlig.babylog.ui.fragment.datetime.TimePickerFragment;
import com.rorlig.babylog.ui.activity.DiaperChangeListActivity2;
import com.rorlig.babylog.ui.fragment.diaper.DiaperChangeActivity2;
import com.rorlig.babylog.ui.fragment.diaper.DiaperChangeListFragment;
import com.rorlig.babylog.ui.fragment.feed.BottleFeedFragment;
import com.rorlig.babylog.ui.fragment.development.DevelopmentFragment;
import com.rorlig.babylog.ui.fragment.diaper.DiaperChangeFragment;
import com.rorlig.babylog.ui.fragment.feed.NursingFeedFragment;
import com.rorlig.babylog.ui.fragment.feed.FeedSelectFragment;
import com.rorlig.babylog.ui.fragment.feed.FeedingListFragment;
import com.rorlig.babylog.ui.fragment.growth.GrowthFragment;
import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
//import com.rorlig.babylog.ui.fragment.InjectablePreferenceFragment;
//import com.rorlig.babylog.ui.fragment.LoggingFragment;
import com.rorlig.babylog.ui.fragment.MainFragment;
import com.rorlig.babylog.ui.fragment.MenuFragment;
import com.rorlig.babylog.ui.fragment.growth.GrowthListFragment;
import com.rorlig.babylog.ui.fragment.home.HomeFragment;
import com.rorlig.babylog.ui.fragment.home.LaunchFragment;
import com.rorlig.babylog.ui.fragment.profile.ProfileFragment;
import com.rorlig.babylog.ui.fragment.sleep.SleepFragment;
import com.rorlig.babylog.ui.widget.DateTimeHeaderFragment;
//import com.rorlig.babylog.ui.fragment.MessageBoardListFragment;
//import com.rorlig.babylog.ui.fragment.MessagesDetailFragment;
//import com.rorlig.babylog.ui.fragment.MessagesFragment;
//import com.rorlig.babylog.ui.fragment.MyEventsFragment;
//import com.rorlig.babylog.ui.fragment.PictureDialogFragment;
//import com.rorlig.babylog.ui.fragment.PrefsFragment;
//import com.rorlig.babylog.ui.fragment.UserListFragment;
//import com.rorlig.babylog.ui.fragment.UsersGridFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Module to be injected into the Activity and Fragments..
 * @author Gaurav Gupta
 */
@Module(injects={
            LandingActivity.class,
            HomeActivity.class,
            BabyLogActivity.class,
            DiaperChangeActivity.class,
            DiaperChangeActivity2.class,
            DiaperChangeListActivity2.class,
            NursingFeedActivity.class,
            BottleFeedingActivity.class,
            GrowthActivity.class,
            FeedingActivity.class,
    //            SearchActivity.class,
//            MessageBoardListActivity.class,
//            MessagesActivity.class,
//            MapActivity.class,
            MenuFragment.class,
            MainFragment.class,
//            FilterFragment.class,
            InjectableFragment.class,
            ActionsSelectFragment.class,
            DiaperChangeFragment.class,
            ProfileFragment.class,
            GrowthFragment.class,
            SleepFragment.class,
            NursingFeedFragment.class,
            DevelopmentFragment.class,
            AboutFragment.class,
            InjectableDialogFragment.class,
            FeedSelectFragment.class,
            BottleFeedFragment.class,
            TimePickerFragment.class,
            DatePickerFragment.class,
            DiaperChangeListFragment.class,
            FeedingListFragment.class,
            GrowthListFragment.class,
            DiaperChangeAdapter.class,
            ContactsFragment.class,
            ContactsPagerListFragment.class,
            DateTimeHeaderFragment.class,
            ContactSelectFragment.class,
            HomeFragment.class,
            LaunchFragment.class,
            ContactsAdapter.class,
            FeedAdapter.class,
            LogItemAdapter.class,
            HomeItemAdapter.class
          },
          library=true, complete = false)

public class ActivityModule
{
    private final Activity activity;

    public ActivityModule(Activity paramActivity)
    {
        this.activity = paramActivity;
    }

    @Provides
    @Singleton
    ActionBar provideActionBar() {
        return this.activity.getActionBar();
    }

    /*
     * @ForActivity ensures that the Context is an activity
     */
    @ForActivity
    @Provides
    @Singleton
    Context provideActivityContext() {
        return this.activity;
    }

    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater(){
        return this.activity.getLayoutInflater();
    }

    @Provides
    @Singleton
    FragmentManager providesFragmentManager() {
        return this.activity.getFragmentManager();
    }




    @Provides
    @Singleton
    TypeFaceManager providesTypeFaceManager(@ForApplication Context context){
        return new TypeFaceManager(context);
    }







}

