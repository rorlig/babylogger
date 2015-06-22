package com.rorlig.babylog.ui.fragment.contact;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.model.ContactCategory;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rorlig on 7/14/14.
 */
public class ContactsPagerListFragment extends InjectableFragment {
    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.vpPager)
    ViewPager viewPager;



//    @InjectView(R.id.gridview)
//    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;




    private String TAG = "ContactsPagerListFragment";

    private EventListener eventListener = new EventListener();
    private MyPagerAdapter adapterViewPager;

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        Log.d(TAG, "gifting orm lite helper ");

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterViewPager  = new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_contact_pager_list, null);
        ButterKnife.inject(this, view);
        return view;
    }


    private class EventListener {
        public EventListener() {

        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        scopedBus.post(new FragmentCreated("Buy Cards"));
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new ContactsFragment();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ContactSelectFragment.newInstance(1, ContactCategory.FACEBOOK);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return ContactSelectFragment.newInstance(1, ContactCategory.GOOGLEPLUS);
                case 3: // Fragment # 1 - This will show SecondFragment
                    return ContactSelectFragment.newInstance(1, ContactCategory.LINKEDIN);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return  ContactCategory.ADDRESS_BOOK.toString();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return  ContactCategory.FACEBOOK.toString();
                case 2: // Fragment # 1 - This will show SecondFragment
                    return  ContactCategory.GOOGLEPLUS.toString();
                case 3: // Fragment # 1 - This will show SecondFragment
                    return  ContactCategory.LINKEDIN.toString();

                default:
                    return null;
            }
        }

    }


}
