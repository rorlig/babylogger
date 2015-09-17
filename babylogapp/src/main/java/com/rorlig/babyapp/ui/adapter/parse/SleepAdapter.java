package com.rorlig.babyapp.ui.adapter.parse;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.parse_dao.Sleep;
import com.rorlig.babyapp.ui.widget.SleepView;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by admin on 4/22/14.
 * todo the UI requires considerable skinning....
 */
public class SleepAdapter extends BaseParseAdapter<ParseObject> {



    private String TAG="SleepAdapter";


    @Inject
    Picasso picasso;



    public SleepAdapter(Activity activity,
                               int textViewResourceId,
                               List<ParseObject> parseObjectList) {
        super(activity, textViewResourceId, parseObjectList);
    }




    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {
        SleepView view = (SleepView) convertView;
//        BottleFeedViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (SleepView) inflator.inflate(R.layout.list_item_sleep, null);
        }
        final Sleep sleepModel = (Sleep) (parseObjectList.get(position));

        view.setModel(sleepModel);
        return view;
    }





    @Override
    public Sleep getItem(int position) {
        return (Sleep) parseObjectList.get(position);
    }








}
