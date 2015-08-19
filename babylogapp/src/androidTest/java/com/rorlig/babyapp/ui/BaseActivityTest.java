package com.rorlig.babyapp.ui;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.ui.activity.LandingActivity;
import com.squareup.spoon.Spoon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * @author gaurav gupta
 */
@RunWith(AndroidJUnit4.class)

public class BaseActivityTest extends ActivityInstrumentationTestCase2<LandingActivity> {


//    @Rule
//    public ActivityTestRule<LandingActivity> activityRule = new ActivityTestRule<>(LandingActivity.class, true, true);

//    private LandingActivity mActivity = activityRule.getActivity();
//    private Instrumentation instrumentation;

    public BaseActivityTest(){
        super(LandingActivity.class);
//        mActivity = activityRule.getActivity();
    }
//    public BaseActivityTest(Class<LandingActivity> activityClass, LandingActivity mActivity) {
//        super(LandingActivity.class);
//        this.mActivity = mActivity;
//    }
//
//    public BaseActivityTest(Class<LandingActivity> activityClass) {
//        super(LandingActivity.class);
//    }

    @Before
    public void resetTimeout() {
        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(26, TimeUnit.SECONDS);
        PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext()).edit().putBoolean("tutorial_shown", false).commit();


//        Log.d("Test", "is null" + (mActivity==null));

    }
//    @Before
//    public void setUp() throws Exception {
//        super.setUp();
//        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
//        instrumentation = getInstrumentation();
//
//        mActivity = getActivity();
//        PreferenceManager.getDefaultSharedPreferences(mActivity).edit().putBoolean("tutorial_shown", false).commit();
//
//    }
    @Test
    public void test_show_tutorial_screen(){


        // Make sure Espresso does not time out
//        IdlingPolicies.setMasterPolicyTimeout(75 * 2, TimeUnit.MILLISECONDS);
//        IdlingPolicies.setIdlingResourceTimeout(75 * 2, TimeUnit.MILLISECONDS);

        // Now we wait
//        IdlingResource idlingResource = new ElapsedTimeIdlingResource(75);
//        Espresso.registerIdlingResources(idlingResource);

//        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
//        instrumentation.waitForIdleSync();
        try {

            Log.d("Base", getCurrentActivity().toString());
            Spoon.screenshot(getCurrentActivity(), "Showing_Tutorial_View");
//        getInstrumentation().waitForIdleSync();

        onView(withText("Skip")).perform(click());

//        getInstrumentation().waitForIdleSync();

//        instrumentation.waitForIdleSync();

//        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

            Log.d("Base", getCurrentActivity().toString());

            Spoon.screenshot(getCurrentActivity(), "Showing_Main_View");

            onView(withId(R.id.diaper_block)).perform(click());

            Log.d("Base", getCurrentActivity().toString());

            Spoon.screenshot(getCurrentActivity(), "Showing_Diaper_Change_Activity");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

//        Espresso.unregisterIdlingResources(idlingResource);

//        intended()
//

    }



    Activity getCurrentActivity() throws Throwable {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        final Activity[] activity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                java.util.Collection activites = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                activity[0] = (Activity) Iterables.getOnlyElement(activites);
            }
        });
        return activity[0];
    }

//    @Test
//    public void init_activity() {
//        onView(withId())
//        // Type text and then press the button.
////        onView
////        onView
////        onView(withId(R.id.editTextUserInput))
////                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
////        onView(withId(R.id.changeTextBt)).perform(click());
////
////        // Check that the text was changed.
////        onView(withId(R.id.textToBeChanged)).check(matches(withText(STRING_TO_BE_TYPED)));
//    }



}
