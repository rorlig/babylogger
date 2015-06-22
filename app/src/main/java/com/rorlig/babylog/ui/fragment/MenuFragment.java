package com.rorlig.babylog.ui.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.events.ui.MenuItemSelectedEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

//import com.rorlig.babylog.otto.events.ui.EventSelectedEvent;
//import com.rorlig.babylog.otto.events.ui.MainActivityReadyEvent;
//import com.rorlig.babylog.otto.events.ui.MenuItemSelectedEvent;


/**
 * Created by admin on 12/15/13.
 */
    public class MenuFragment extends InjectableFragment {

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.menu_list)
    ListView drawerList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;

    @InjectView(R.id.content)
    View status;

    @InjectView(R.id.baby_name)
    TextView babyName;

//    @InjectView(R.id.babyAge)
//    TextView babyAge;


    Typeface typeface;

    private String TAG = "MenuFragment";

    private EventListener eventListener = new EventListener();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);


    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.add_item, menu);
//    }


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
//        Log.d(TAG, "drawerList is null --> " + (drawerList == null) + " menuHeader--> " + (menuHeader == null));
//        Log.d(TAG, "drawerList is null --> " + drawerList + " menuHeader--> " + menuHeader + " context: " + context);
        drawerList.setAdapter(new MenuAdapter(context));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        typeface=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/proximanova_light.ttf");

//        babyAge.setTypeface(typeface);

        babyName.setTypeface(typeface);

//        drawerList.setAdapter(new MenuAdapter(context));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt("menu_drawer.SELECTED_POSITION", drawerList.getCheckedItemPosition());
    }

    @Override
    public void onStart(){
        super.onStart();
        scopedBus.register(eventListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        scopedBus.unregister(eventListener);
    }





    /*
     * Adapter class for the left drawer...
     */
    private class MenuAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private Drawable[] mIcons;
        private int[] mIds;
        private String[] mItems;

        private MenuAdapter(Context context) {
            Log.d(TAG, "Context is " + context);

            inflater = LayoutInflater.from(context);

            mIds = context.getResources().getIntArray(R.array.menu_drawer_ids);

            mItems = context.getResources().getStringArray(R.array.menu_drawer_items);
            TypedArray localTypedArray = context.getResources().obtainTypedArray(R.array.menu_drawer_icons);
            mIcons = new Drawable[localTypedArray.length()];
            for (int i = 0; i < localTypedArray.length(); i++)
                mIcons[i] = localTypedArray.getDrawable(i);
        }

        public int getCount() {
            return mItems.length;
        }

        public Object getItem(int paramInt) {
            return mItems[paramInt];
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int position, View view, ViewGroup paramViewGroup) {
//            Log.d(TAG, "getView paramvView "  + paramView + " paramViewGroup: " + paramViewGroup);
//            View localView = paramView;
////            if (localView == null)
////                localView = inflater.inflate(R.layout.list_item_menu, paramViewGroup, false);
//            Log.d(TAG, "localView is" + localView);
////            TextView localTextView = (TextView) localView.findViewById(R.id.textview_drawer_item);
//            TextView localTextView = (TextView) inflater.inflate(R.layout.list_item_menu, paramViewGroup, false);
//            localTextView.setCompoundDrawablesWithIntrinsicBounds(mIcons[paramInt], null, null, null);
//            localTextView.setText(mItems[paramInt]);
//            return localTextView;

            Holder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.list_item_menu, null);
                holder = new Holder();
                holder.optionText = (TextView) view
                        .findViewById(R.id.option_text);
                holder.optionText.setTypeface(typeface);
                holder.lefticon = (ImageView) view.findViewById(R.id.left_icon);
                holder.righticon = (ImageView) view
                        .findViewById(R.id.right_icon);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            holder.optionText.setText((String) getItem(position));
//            if (mIcons[position] != -1)
            holder.lefticon.setImageDrawable(mIcons[position]);
//            if (righticons[position] != -1)
//                holder.righti

            return view;
        }

        /*
         * Inner View Holder
         */
        class Holder {
            TextView optionText;
            ImageView lefticon;
            ImageView righticon;
        }
    }

    /*
     * Inner class handling the menu item clicks...
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        public DrawerItemClickListener() {
           Log.d(TAG, "DrawerItemClick Constructor");
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "onItemClick position "  + position);
            selectItem(position);
        }
    }

    /*
     * @param int position of the menuitem clicked.
     */
    private void selectItem(int position) {

        MenuItemSelectedEvent menuItemSelectedEvent = new MenuItemSelectedEvent(position);

        //posting the event to the bus...
        scopedBus.post(menuItemSelectedEvent);
    }


    private class EventListener {
        public EventListener(){
        }

//        @Subscribe
//        public void onEventSelected(EventSelectedEvent paramEventSelectedEvent) {
////            if (paramEventSelectedEvent.eventInfo != null)
////            {
////                UiUtils.fadeIn(MenuFragment.this.mContent);
////                UiUtils.fadeOut(MenuFragment.this.mStatus);
////                MenuFragment.this.mMenuHeader.setText(paramEventSelectedEvent.eventInfo.getName());
////                return;
////            }
////            UiUtils.fadeOut(MenuFragment.this.mContent);
////            UiUtils.fadeIn(MenuFragment.this.mStatus);
////            MenuFragment.this.mMenuHeader.setText(null);
//        }
//
//        @Subscribe
//        public void onMainActivityReady(MainActivityReadyEvent paramMainActivityReadyEvent) {
////            if (MenuFragment.this.mSelectedPosition != -1)
////            {
////                MenuFragment.this.mDrawerList.setItemChecked(MenuFragment.this.mSelectedPosition, true);
////                MenuFragment.this.selectItem(MenuFragment.this.mSelectedPosition);
////                MenuFragment.this.mSelectedPosition = -1;
////            }
//        }
    }
}

