package com.rorlig.babyapp.dagger;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;

import com.rorlig.babyapp.scheduler.TypeFaceManager;
import com.rorlig.babyapp.ui.activity.DiaperChangeActivity;
import com.rorlig.babyapp.ui.activity.ExportActivity;
import com.rorlig.babyapp.ui.activity.FeedingActivity;
import com.rorlig.babyapp.ui.activity.GrowthActivity;
import com.rorlig.babyapp.ui.activity.HomeActivity;
import com.rorlig.babyapp.ui.activity.LandingActivity;
import com.rorlig.babyapp.ui.activity.LicenseActivity;
import com.rorlig.babyapp.ui.activity.LoginActivity;
import com.rorlig.babyapp.ui.activity.MilestonesActivity;
import com.rorlig.babyapp.ui.activity.PrefsActivity;
import com.rorlig.babyapp.ui.activity.ProfileActivity;
import com.rorlig.babyapp.ui.activity.SleepActivity;
import com.rorlig.babyapp.ui.adapter.ContactsAdapter;
import com.rorlig.babyapp.ui.adapter.DiaperChangeAdapter2;
import com.rorlig.babyapp.ui.adapter.ExportItemAdapter;
import com.rorlig.babyapp.ui.adapter.FeedAdapter;
import com.rorlig.babyapp.ui.adapter.GrowthAdapter;
import com.rorlig.babyapp.ui.adapter.HomeItemAdapter;
import com.rorlig.babyapp.ui.adapter.LogItemAdapter;
import com.rorlig.babyapp.ui.adapter.MilestonesItemAdapter;
import com.rorlig.babyapp.ui.adapter.SleepAdapter;
import com.rorlig.babyapp.ui.adapter.parse.DiaperChangeAdapter;
import com.rorlig.babyapp.ui.fragment.auth.ForgotFragment;
import com.rorlig.babyapp.ui.fragment.InjectableDialogFragment;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;
import com.rorlig.babyapp.ui.fragment.auth.LoginFragment;
import com.rorlig.babyapp.ui.fragment.auth.SignUpFragment;
import com.rorlig.babyapp.ui.fragment.about.LicensesFragment;
import com.rorlig.babyapp.ui.fragment.datetime.CustomTimePickerFragment;
import com.rorlig.babyapp.ui.fragment.datetime.DatePickerFragment;
import com.rorlig.babyapp.ui.fragment.datetime.TimePickerFragment;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperChangeFragment;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperChangeListFragment;
import com.rorlig.babyapp.ui.fragment.diaper.DiaperStatsFragment;
import com.rorlig.babyapp.ui.fragment.export.ExportFragment;
import com.rorlig.babyapp.ui.fragment.feed.BottleFeedFragment;
import com.rorlig.babyapp.ui.fragment.feed.FeedSelectFragment;
import com.rorlig.babyapp.ui.fragment.feed.FeedingListFragment;
import com.rorlig.babyapp.ui.fragment.feed.NursingFeedFragment;
import com.rorlig.babyapp.ui.fragment.growth.GrowthFragment;
import com.rorlig.babyapp.ui.fragment.growth.GrowthListFragment;
import com.rorlig.babyapp.ui.fragment.growth.GrowthStatsFragment;
import com.rorlig.babyapp.ui.fragment.home.HomeFragment;
import com.rorlig.babyapp.ui.fragment.milestones.MilestoneFragment;
import com.rorlig.babyapp.ui.fragment.milestones.MilestoneListFragment;
import com.rorlig.babyapp.ui.fragment.milestones.MilestonesCompletedFragment;
import com.rorlig.babyapp.ui.fragment.preference.InjectablePreferenceFragment;
import com.rorlig.babyapp.ui.fragment.preference.PrefsFragment;
import com.rorlig.babyapp.ui.fragment.profile.PictureSourceSelectFragment;
import com.rorlig.babyapp.ui.fragment.profile.ProfileFragment;
import com.rorlig.babyapp.ui.fragment.sleep.SleepFragment;
import com.rorlig.babyapp.ui.fragment.sleep.SleepListFragment;
import com.rorlig.babyapp.ui.fragment.sleep.SleepStatsFragment;
import com.rorlig.babyapp.ui.widget.DateTimeHeaderFragment;

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
            LoginActivity.class,
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
            MilestoneFragment.class,
            LoginFragment.class,
            ForgotFragment.class,
            SignUpFragment.class,
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

