package com.rorlig.babylog.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ocpsoft.pretty.time.PrettyTime;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.dao.SleepDao;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.rorlig.babylog.ui.widget.DiaperChangeView;
import com.rorlig.babylog.ui.widget.SleepView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

/**
 * Created by admin on 4/22/14.
 * todo the UI requires considerable skinning....
 */
public class SleepAdapter extends ArrayAdapter<SleepDao> {

    private final PrettyTime prettyTime;
    private final SimpleDateFormat simpleDateFormat;
    private Context context;
    private List<SleepDao> sleepList;


    private String TAG="SleepAdapter";


    @Inject
    Picasso picasso;




    public SleepAdapter(Activity activity, int textViewResourceId, List<SleepDao> sleepDaoList) {
        super(activity.getApplicationContext(), textViewResourceId, sleepDaoList);
        Log.d(TAG, "constructor SleepAdapter");
        this.sleepList = new ArrayList<SleepDao>(sleepDaoList);
        this.context = activity.getApplicationContext();
        ((InjectableActivity)activity).inject(this);
        prettyTime = new PrettyTime();
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        Log.d(TAG, "default time zone 0" + TimeZone.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());


    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {
        SleepView view = (SleepView) convertView;
//        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (SleepView) inflator.inflate(R.layout.list_item_sleep, null);
        }
        final SleepDao sleepDao = sleepList.get(position);

        view.setModel(sleepDao);
        return view;
    }



    @Override
    public int getCount() {
        return sleepList.size();
    }

    public void update(List<SleepDao> sleepList) {
        Log.d(TAG, "update");
        this.sleepList = new ArrayList<SleepDao>(sleepList);
        notifyDataSetInvalidated();
        notifyDataSetChanged();
    }






}
