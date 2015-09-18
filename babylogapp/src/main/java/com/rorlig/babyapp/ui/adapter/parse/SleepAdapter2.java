package com.rorlig.babyapp.ui.adapter.parse;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.MilestoneItemClicked;
import com.rorlig.babyapp.otto.SleepItemClicked;
import com.rorlig.babyapp.parse_dao.Milestones;
import com.rorlig.babyapp.parse_dao.Sleep;
import com.rorlig.babyapp.ui.widget.SleepView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

/**
 * Created by admin on 4/22/14.
 * todo the UI requires considerable skinning....
 */
public class SleepAdapter2 extends ArrayAdapter<ParseObject, SleepAdapter2.ViewHolder> {


    private final SimpleDateFormat simpleDateFormat;
    private String TAG="SleepAdapter";


    @Inject
    Picasso picasso;
    private Context context;


    public SleepAdapter2( List<ParseObject> parseObjectList) {
        super(parseObjectList);
//        this.parseObjectList = parseObjectList;
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }









    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View growthView = inflater.inflate(R.layout.list_item_sleep, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(growthView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Sleep model = (Sleep) mObjects.get(position);
        viewHolder.textViewTime.setText(simpleDateFormat.format(model.getLogCreationDate()));
        viewHolder.sleepStartTime.setText(simpleDateFormat.format(model.getSleepStartTime()));
        viewHolder.sleepDuration.setText(toHoursandMinutes(model.getDuration()));
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewTime;
        TextView sleepStartTime;
        TextView sleepDuration;
        
        public ViewHolder(View itemView) {
            super(itemView);
            textViewTime = (TextView) itemView.findViewById(R.id.sleep_time);
            sleepStartTime = (TextView) itemView.findViewById(R.id.sleep_start_time);
            sleepDuration = (TextView) itemView.findViewById(R.id.sleep_duration);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick v ");
            int position = getLayoutPosition();
            Sleep sleep = (Sleep) mObjects.get(position);
//            Toast.makeText(context, "hello ", Toast.LENGTH_LONG).show();
            scopedBus.post(new SleepItemClicked(sleep, position));

        }
    }

    private String toHoursandMinutes(Long duration) {
        long hours = duration/60;
        long minutes = duration % 60;
        if (hours==0) {
            return minutes + " minutes";
        } else  {
            return hours + " hours and " + minutes + " minutes";
        }
    }
}
