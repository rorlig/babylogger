package com.rorlig.babylog.dagger;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;

import com.rorlig.babylog.scheduler.TypeFaceManager;
import com.rorlig.babylog.ui.activity.DiaperChangeActivity;
import com.rorlig.babylog.ui.activity.ExportActivity;
import com.rorlig.babylog.ui.activity.FeedingActivity;
import com.rorlig.babylog.ui.activity.GrowthActivity;
import com.rorlig.babylog.ui.activity.HomeActivity;
import com.rorlig.babylog.ui.activity.LandingActivity;
import com.rorlig.babylog.ui.activity.LicenseActivity;
import com.rorlig.babylog.ui.activity.MilestonesActivity;
import com.rorlig.babylog.ui.activity.PrefsActivity;
import com.rorlig.babylog.ui.activity.ProfileActivity;
import com.rorlig.babylog.ui.activity.SleepActivity;
import com.rorlig.babylog.ui.adapter.ContactsAdapter;
import com.rorlig.babylog.ui.adapter.DiaperChangeAdapter;
import com.rorlig.babylog.ui.adapter.ExportItemAdapter;
import com.rorlig.babylog.ui.adapter.FeedAdapter;
import com.rorlig.babylog.ui.adapter.GrowthAdapter;
import com.rorlig.babylog.ui.adapter.HomeItemAdapter;
import com.rorlig.babylog.ui.adapter.LogItemAdapter;
import com.rorlig.babylog.ui.adapter.MilestonesItemAdapter;
import com.rorlig.babylog.ui.adapter.SleepAdapter;
import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.MainFragment;
import com.rorlig.babylog.ui.fragment.about.LicensesFragment;
import com.rorlig.babylog.ui.fragment.datetime.CustomTimePickerFragment;
import com.rorlig.babylog.ui.fragment.datetime.DatePickerFragment;
import com.rorlig.babylog.ui.fragment.datetime.TimePickerFragment;
import com.rorlig.babylog.ui.fragment.diaper.DiaperChangeFragment;
import com.rorlig.babylog.ui.fragment.diaper.DiaperChangeListFragment;
import com.rorlig.babylog.ui.fragment.diaper.DiaperStatsFragment;
import com.rorlig.babylog.ui.fragment.export.ExportFragment;
import com.rorlig.babylog.ui.fragment.feed.BottleFeedFragment;
import com.rorlig.babylog.ui.fragment.feed.FeedSelectFragment;
import com.rorlig.babylog.ui.fragment.feed.FeedingListFragment;
import com.rorlig.babylog.ui.fragment.feed.NursingFeedFragment;
import com.rorlig.babylog.ui.fragment.growth.GrowthFragment;
import com.rorlig.babylog.ui.fragment.growth.GrowthListFragment;
import com.rorlig.babylog.ui.fragment.growth.GrowthStatsFragment;
import com.rorlig.babylog.ui.fragment.home.HomeFragment;
import com.rorlig.babylog.ui.fragment.home.HomeFragment2;
import com.rorlig.babylog.ui.fragment.milestones.MilestoneListFragment;
import com.rorlig.babylog.ui.fragment.milestones.MilestonesCompletedFragment;
import com.rorlig.babylog.ui.fragment.preference.InjectablePreferenceFragment;
import com.rorlig.babylog.ui.fragment.preference.PrefsFragment;
import com.rorlig.babylog.ui.fragment.profile.PictureSourceSelectFragment;
import com.rorlig.babylog.ui.fragment.profile.ProfileFragment;
import com.rorlig.babylog.ui.fragment.sleep.SleepFragment;
import com.rorlig.babylog.ui.fragment.sleep.SleepListFragment;
import com.rorlig.babylog.ui.fragment.sleep.SleepStatsFragment;
import com.rorlig.babylog.ui.widget.DateTimeHeaderFragment;

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
            DiaperChangeActivity.class,
            GrowthActivity.class,
            FeedingActivity.class,
            ProfileActivity.class,
            MilestonesActivity.class,
            PrefsActivity.class,
            ExportActivity.class,
            LicenseActivity.class,
            LicensesFragment.class,
            MainFragment.class,
            InjectableFragment.class,
            InjectablePreferenceFragment.class,
            DiaperChangeFragment.class,
            ProfileFragment.class,
            GrowthFragment.class,
            SleepFragment.class,
            NursingFeedFragment.class,
            LicensesFragment.class,
            InjectableDialogFragment.class,
            FeedSelectFragment.class,
            BottleFeedFragment.class,
            TimePickerFragment.class,
            DatePickerFragment.class,
            DiaperChangeListFragment.class,
            FeedingListFragment.class,
            GrowthListFragment.class,
            SleepListFragment.class,
            MilestoneListFragment.class,
            DiaperChangeAdapter.class,
            ExportItemAdapter.class,
            DateTimeHeaderFragment.class,
            HomeFragment.class,
            HomeFragment2.class,
            SleepActivity.class,
            GrowthStatsFragment.class,
            DiaperStatsFragment.class,
            ContactsAdapter.class,
            FeedAdapter.class,
            LogItemAdapter.class,
            HomeItemAdapter.class,
            GrowthAdapter.class,
            MilestonesItemAdapter.class,
            MilestonesCompletedFragment.class,
            PictureSourceSelectFragment.class,
            SleepStatsFragment.class,
            PrefsFragment.class,
            ExportFragment.class,
            CustomTimePickerFragment.class,
            SleepAdapter.class

          },
          library=true, complete = false)

public class ActivityModule
{
    private final Activity activity;

    public ActivityModule(Activity paramActivity)
    {
        this.activity = paramActivity;
    }

    /*
     * @returns ActionBar
     */
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


    /*
     * @returns LayoutInflater
     */
    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater(){
        return this.activity.getLayoutInflater();
    }

    /*
     * @returns FragmentManager
     */
    @Provides
    @Singleton
    FragmentManager providesFragmentManager() {
        return this.activity.getFragmentManager();
    }



    /*
    * @returns TypeFaceManager
    */
    @Provides
    @Singleton
    TypeFaceManager providesTypeFaceManager(@ForApplication Context context){
        return new TypeFaceManager(context);
    }







}

