package com.rorlig.babyapp.ui.adapter.parse;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.parse_dao.Growth;
import com.rorlig.babyapp.ui.widget.GrowthView;

import java.util.List;

/**
 * Created by rorlig on 6/9/15.
 */
public class GrowthAdapter extends BaseParseAdapter<ParseObject> {


    private final String TAG = "GrowthAdapter";


    public GrowthAdapter(Activity activity,
                               int textViewResourceId,
                               List<ParseObject> parseObjectList) {
        super(activity, textViewResourceId, parseObjectList);
    }






    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {

        GrowthView view = (GrowthView) convertView;


        final ParseObject growth = parseObjectList.get(position);


        if(view == null) {
            LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (GrowthView) inflator.inflate(R.layout.list_item_growth, parent, false);
            view.setModel(growth);
        }
//        final GrowthDao growthDao = growthDaoList.get(position);
//
//        vh.headTextView.setText(growthDao.getHeadMeasurement() + " inches");
//
//        vh.heightTextView.setText(growthDao.getHeight() + " inches");
//
//
//        vh.weightTextView.setText(growthDao.getWeight() + " pounds");
//
//        vh.textViewTime.setText(simpleDateFormat.format(new Date(growthDao.getDate().getTime())));
//
//        vh.notesContentTextView.setText(growthDao.getNotes());
//




//        vh.txtType.setText(feedDao.getFeedItem() + " " + feedDao.getQuantity());
//        vh.textViewTime.setText(simpleDateFormat.format(new Date(feedDao.getDate())));

        return view;
    }




    @Override
    public Growth getItem(int position) {
        return (Growth) parseObjectList.get(position);
    }

//    public void update(List<GrowthDao> growthDaoList) {
//        Log.d(TAG, "update");
//        this.growthDaoList = new ArrayList<GrowthDao>(growthDaoList);
//        notifyDataSetInvalidated();
//        notifyDataSetChanged();
//    }




    public static class ViewHolder {
        TextView weightTextView;
        TextView heightTextView;
        TextView headTextView;
        TextView textViewTime;
        TextView notesContentTextView;
    }


}
