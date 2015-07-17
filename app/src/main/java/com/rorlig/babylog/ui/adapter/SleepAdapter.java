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

    private int BITMAP_MAX_HEIGHT = 256;
    private int BITMAP_MAX_WIDTH = 256;



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
//            viewHolder = new ViewHolder(view);
        }
//        } else {
//            view = convertView;
//        }
        final SleepDao sleepDao = sleepList.get(position);

        view.setModel(sleepDao);
//        Log.d(TAG,  event.toString());

//        Log.d(TAG, "merchant " + diaperChangeDao);
//
//        viewHolder  = (ViewHolder) view.getTag();
//        Log.d(TAG, " date is " + new Date(diaperChangeDao.getDate().getTime()).toString());
//        viewHolder.textViewTime.setText(simpleDateFormat.format(new Date(diaperChangeDao.getDate().getTime())));
//
//        setDiaperChangeType(diaperChangeDao, viewHolder);
//        setDiaperColor(diaperChangeDao,viewHolder);
//        setDiaperIncidentType(diaperChangeDao, viewHolder);
//
//        setPoopTexture(diaperChangeDao, viewHolder);
//
//        viewHolder.notesContent.setText(diaperChangeDao.getDiaperChangeNotes());

//        viewHolder.poopColor.setImageDrawable(co);
        return view;
    }

//    private void setPoopTexture(DiaperChangeDao diaperChangeDao, ViewHolder viewHolder) {
//        if (diaperChangeDao.getPoopTexture()!=null) {
//            viewHolder.textViewPoopTexture.setText(diaperChangeDao.getPoopTexture().toString());
//        }
//    }
//
//    private void setDiaperIncidentType(DiaperChangeDao diaperChangeDao, ViewHolder viewHolder) {
//        if (diaperChangeDao.getDiaperChangeIncidentType()!=null) {
//            viewHolder.incidentDetails.setText(diaperChangeDao.getDiaperChangeIncidentType().toString());
//        }
//
//    }



//    private void setDiaperColor(DiaperChangeDao diaperChangeDao, ViewHolder viewHolder) {
//        if (diaperChangeDao.getPoopColor()!=null) {
//            switch (diaperChangeDao.getPoopColor()) {
//                case COLOR_1:
//                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_1));
//                    break;
//                case COLOR_2:
//                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_2));
//                    break;
//                case COLOR_3:
//                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_3));
//                    break;
//                case COLOR_4:
//                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_4));
//                    break;
//                case COLOR_5:
//                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_5));
//                    break;
//                case COLOR_6:
//                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_6));
//                    break;
//                case COLOR_7:
//                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_7));
//                    break;
//                case COLOR_8:
//                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_8));
//                    break;
//
//            }
//        }
//
//    }
//
//    private void setDiaperChangeType(DiaperChangeDao diaperChangeDao, ViewHolder viewHolder) {
//        if (diaperChangeDao.getDiaperChangeEventType()== DiaperChangeEnum.BOTH) {
//            viewHolder.diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
//            viewHolder.diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
//        } else if (diaperChangeDao.getDiaperChangeEventType()==DiaperChangeEnum.WET){
//            viewHolder.diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
//            viewHolder.diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_unselected));
//            viewHolder.row2.setVisibility(View.GONE);
//        } else {
//            viewHolder.diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_unselected));
//            viewHolder.diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
//        }
//    }


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


    /*
            *  View Holder class for individual list items
            */
//    public static class ViewHolder {
//
////        @InjectView(R.id.card_merchant_icon)
////        ImageView merchantIcon;
////
////        @InjectView(R.id.card_merchant_name)
////        TextView merchantName;
////
//////        @InjectView(R.id.merchant_low_value)
//////        TextView merchantLowValue
//
//        @InjectView(R.id.diaperWetChecked)
//        ImageView diaperWetChecked;
//
//        @InjectView(R.id.diaperPoopChecked)
//        ImageView diaperPoopChecked;
//
//        @InjectView(R.id.diaperChangeTime)
//        TextView textViewTime;
//
//        @InjectView(R.id.poopColor)
//        ImageView poopColor;
//
//        @InjectView(R.id.poopTexture)
//        TextView textViewPoopTexture;
//
//        @InjectView(R.id.notesContent)
//        TextView notesContent;
//
//        @InjectView(R.id.incidentDetails)
//        TextView incidentDetails;
//
//        @InjectView(R.id.row2)
//        LinearLayout row2;
//
//
//
//        public ViewHolder(View view){
//            ButterKnife.inject(this, view);
//            view.setTag(this);
//        }
//    }



}
