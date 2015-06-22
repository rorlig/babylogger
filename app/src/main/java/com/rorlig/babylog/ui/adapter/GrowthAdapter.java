package com.rorlig.babylog.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dao.GrowthDao;
import com.rorlig.babylog.ui.activity.InjectableActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by rorlig on 6/9/15.
 */
public class GrowthAdapter extends ArrayAdapter<GrowthDao>{


    private final String TAG = "GrowthAdapter";
    private LayoutInflater mInflater;
    private SimpleDateFormat simpleDateFormat;
    private PrettyTime prettyTime;
    private Context context;
    private List<GrowthDao> growthDaoList;

    public GrowthAdapter(Context context, int resource) {
        super(context, resource);
    }

    public GrowthAdapter(Activity activity, int textViewResourceId, List<GrowthDao> growthDao) {
        super(activity.getApplicationContext(), textViewResourceId, growthDao);
        Log.d(TAG, "constructor FeedAdapter");
        this.growthDaoList = new ArrayList<GrowthDao>(growthDao);
        this.context = activity.getApplicationContext();
        ((InjectableActivity)activity).inject(this);
        prettyTime = new PrettyTime();
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        Log.d(TAG, "default time zone 0" + TimeZone.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }






    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {
        View view = null;
        int type = getItemViewType(position);

        ViewHolder vh = null;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_growth, parent, false);
            if(convertView != null) {
                vh = new ViewHolder();
                vh.weightTextView = (TextView) convertView.findViewById(R.id.weight_value);
                vh.heightTextView = (TextView) convertView.findViewById(R.id.height_value);
                vh.headTextView = (TextView) convertView.findViewById(R.id.headValue);
                vh.textViewTime = (TextView) convertView.findViewById(R.id.diaperChangeTime);
                vh.notesContentTextView = (TextView) convertView.findViewById(R.id.notes_content);


                convertView.setTag(vh);
            }
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
        final GrowthDao growthDao = growthDaoList.get(position);

        vh.headTextView.setText(growthDao.getHeadMeasurement() + " inches");

        vh.heightTextView.setText(growthDao.getHeight() + " inches");


        vh.weightTextView.setText(growthDao.getWeight() + " pounds");

        vh.textViewTime.setText(simpleDateFormat.format(new Date(growthDao.getTime())));

        vh.notesContentTextView.setText(growthDao.getNotes());





//        vh.txtType.setText(feedDao.getFeedItem() + " " + feedDao.getQuantity());
//        vh.textViewTime.setText(simpleDateFormat.format(new Date(feedDao.getTime())));

        return convertView;
    }



    @Override
    public int getCount() {
        return growthDaoList.size();
    }

    public void update(List<GrowthDao> growthDaoList) {
        Log.d(TAG, "update");
        this.growthDaoList = new ArrayList<GrowthDao>(growthDaoList);
        notifyDataSetInvalidated();
        notifyDataSetChanged();
    }




    public static class ViewHolder {
        TextView weightTextView;
        TextView heightTextView;
        TextView headTextView;
        TextView textViewTime;
        TextView notesContentTextView;
    }


}
