package com.rorlig.babylog.ui.fragment.action;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.otto.events.ui.ActionSelectItem;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.fragment.InjectableFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rorlig on 7/15/14.
 */
public class ActionsSelectFragment extends InjectableFragment {

    @ForActivity
    @Inject
    Context context;

    @InjectView(R.id.gridview)
    GridView actionsList;

//    @InjectView(R.id.menu_header)
//    TextView menuHeader;


    Typeface typeface;

    private String TAG = "ActionsSelectFragment";

    private EventListener eventListener = new EventListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        actionsList.setAdapter(new ActionsAdapter(context));
        actionsList.setOnItemClickListener(new ActionItemClickListener());

        typeface=Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/proximanova_light.ttf");

        scopedBus.post(new FragmentCreated("Activities"));



    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_actions_sheet, null);
        ButterKnife.inject(this, view);
        return view;
    }

    private class EventListener {
        public EventListener(){
        }

//        }
    }

    private class ActionsAdapter extends BaseAdapter {

        private final LayoutInflater inflater;
        private Drawable[] mIcons;
        private int[] mIds;
        private String[] mItems;


        private final Context context;

        public ActionsAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);

            mIds = context.getResources().getIntArray(R.array.menu_drawer_ids);

            mItems = context.getResources().getStringArray(R.array.action_fragment_items);
            TypedArray localTypedArray = context.getResources().obtainTypedArray(R.array.action_fragment_icons);
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

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Holder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.grid_item_action_sheet, null);
                holder = new Holder();
                holder.actionText = (TextView) view
                        .findViewById(R.id.actionText);
                holder.actionText.setTypeface(typeface);
                holder.actionImage = (ImageView) view.findViewById(R.id.actionPicture);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            holder.actionText.setText((String) getItem(position));
//            if (mIcons[position] != -1)
            holder.actionImage.setImageDrawable(mIcons[position]);
//            if (righticons[position] != -1)
//                holder.righti

            return view;
        }

        /*
         * Inner View Holder
         */
        class Holder {
            ImageView actionImage;
            TextView actionText;
        }
    }


    private class ActionItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        //todo -- wire the correct fragment....
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            scopedBus.post(new ActionSelectItem(position));
        }
    }
}
