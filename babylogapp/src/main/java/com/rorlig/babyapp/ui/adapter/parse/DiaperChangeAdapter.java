package com.rorlig.babyapp.ui.adapter.parse;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.parse_dao.DiaperChange;
import com.rorlig.babyapp.ui.widget.DiaperChangeView;

import java.util.List;

/**
 * Created by admin on 4/22/14.
 * todo the UI requires considerable skinning....
 */
public class DiaperChangeAdapter extends BaseParseAdapter<ParseObject> {



    private String TAG="DiaperChangeAdapter";




    public DiaperChangeAdapter(Activity activity,
                               int textViewResourceId,
                               List<ParseObject> parseObjectList) {
        super(activity, textViewResourceId, parseObjectList);
    }


    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {
        Log.d(TAG, "position " + position);
        DiaperChangeView view = (DiaperChangeView) convertView;
//        BottleFeedViewHolder viewHolder;

        if (view == null) {
            Log.d(TAG, " view is inflated ");
            LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (DiaperChangeView) inflator.inflate(R.layout.list_item_diaper_change, null);
//            viewHolder = new BottleFeedViewHolder(view);
        }
//        } else {
//            view = convertView;
//        }
        final DiaperChange diaperChange = (DiaperChange) parseObjectList.get(position);

        Log.d(TAG, diaperChange.toString());

        view.setModel(diaperChange);
//        Log.d(TAG,  event.toString());

//        Log.d(TAG, "merchant " + DiaperChange);
//
//        viewHolder  = (BottleFeedViewHolder) view.getTag();
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



    @Override
    public DiaperChange getItem(int position) {
        return (DiaperChange) parseObjectList.get(position);
    }





}
