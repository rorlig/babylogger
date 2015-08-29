package com.rorlig.babyapp.ui.adapter.parse;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ocpsoft.pretty.time.PrettyTime;
import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.parse_dao.DiaperChange;
import com.rorlig.babyapp.ui.activity.InjectableActivity;
import com.rorlig.babyapp.ui.widget.DiaperChangeView;
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
public class DiaperChangeAdapter extends ArrayAdapter<DiaperChange> {

    private final PrettyTime prettyTime;
    private final SimpleDateFormat simpleDateFormat;
    private Context context;
    private List<ParseObject> diaperChangeArrayList;


    private static final int TYPE_TEXT = 0;
    private static final int TYPE_IMAGE = 1;
    private String TAG="DiaperChangeAdapter";

    private float aspectRatio = 1;

    private RequestCreator request;

    @Inject
    Picasso picasso;

    private int BITMAP_MAX_HEIGHT = 256;
    private int BITMAP_MAX_WIDTH = 256;



    public DiaperChangeAdapter(Activity activity, int textViewResourceId,
                               List<ParseObject> diaperChange) {
        super(activity.getApplicationContext(), textViewResourceId);
        Log.d(TAG, "constructor DiaperChangeAdapter");
        this.diaperChangeArrayList = diaperChange;
        this.context = activity.getApplicationContext();
        ((InjectableActivity)activity).inject(this);
        prettyTime = new PrettyTime();
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        Log.d(TAG, "default time zone 0" + TimeZone.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());


    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {
        Log.d(TAG, "position " + position);
        DiaperChangeView view = (DiaperChangeView) convertView;
//        ViewHolder viewHolder;

        if (view == null) {
            Log.d(TAG, " view is inflated ");
            LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (DiaperChangeView) inflator.inflate(R.layout.list_item_diaper_change, null);
//            viewHolder = new ViewHolder(view);
        }
//        } else {
//            view = convertView;
//        }
        final DiaperChange diaperChange = (DiaperChange) diaperChangeArrayList.get(position);

        Log.d(TAG, diaperChange.toString());

        view.setModel(diaperChange);
//        Log.d(TAG,  event.toString());

//        Log.d(TAG, "merchant " + DiaperChange);
//
//        viewHolder  = (ViewHolder) view.getTag();
//        Log.d(TAG, " date is " + new Date(DiaperChange.getDate().getTime()).toString());
//        viewHolder.textViewTime.setText(simpleDateFormat.format(new Date(DiaperChange.getDate().getTime())));
//
//        setDiaperChangeType(DiaperChange, viewHolder);
//        setDiaperColor(DiaperChange,viewHolder);
//        setDiaperIncidentType(DiaperChange, viewHolder);
//
//        setPoopTexture(DiaperChange, viewHolder);
//
//        viewHolder.notesContent.setText(DiaperChange.getDiaperChangeNotes());

//        viewHolder.poopColor.setImageDrawable(co);
        return view;
    }

//    private void setPoopTexture(DiaperChange DiaperChange, ViewHolder viewHolder) {
//        if (DiaperChange.getPoopTexture()!=null) {
//            viewHolder.textViewPoopTexture.setText(DiaperChange.getPoopTexture().toString());
//        }
//    }
//
//    private void setDiaperIncidentType(DiaperChange DiaperChange, ViewHolder viewHolder) {
//        if (DiaperChange.getDiaperChangeIncidentType()!=null) {
//            viewHolder.incidentDetails.setText(DiaperChange.getDiaperChangeIncidentType().toString());
//        }
//
//    }



//    private void setDiaperColor(DiaperChange DiaperChange, ViewHolder viewHolder) {
//        if (DiaperChange.getPoopColor()!=null) {
//            switch (DiaperChange.getPoopColor()) {
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
//    private void setDiaperChangeType(DiaperChange DiaperChange, ViewHolder viewHolder) {
//        if (DiaperChange.getDiaperChangeEventType()== DiaperChangeEnum.BOTH) {
//            viewHolder.diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
//            viewHolder.diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
//        } else if (DiaperChange.getDiaperChangeEventType()==DiaperChangeEnum.WET){
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
        Log.d(TAG, "getCount " + diaperChangeArrayList.size());
        return diaperChangeArrayList.size();
    }


    @Override
    public DiaperChange getItem(int position) {
        return (DiaperChange) diaperChangeArrayList.get(position);
    }

    public void update(List<ParseObject> DiaperChangeArrayList) {
        Log.d(TAG, "update");
        this.diaperChangeArrayList = new ArrayList<>(DiaperChangeArrayList);
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
