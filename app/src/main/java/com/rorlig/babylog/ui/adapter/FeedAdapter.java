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
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.model.feed.FeedType;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.rorlig.babylog.ui.widget.BottleFeedView;
import com.rorlig.babylog.ui.widget.NursingFeedView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

/**
 * Created by admin on 4/22/14.
 * todo the UI requires considerable skinning....
 */
public class FeedAdapter extends ArrayAdapter<FeedDao> {

    private static final int FEED_BOTTLE = 0;
    private static final int FEED_NURSING = 1;
    private final PrettyTime prettyTime;
    private final SimpleDateFormat simpleDateFormat;
    private Context context;
    private List<FeedDao> feedDaoList;


    private String TAG="FeedAdapter";

    private LayoutInflater mInflater;



    @Inject
    Picasso picasso;




    public FeedAdapter(Activity activity, int textViewResourceId, List<FeedDao> feedDao) {
        super(activity.getApplicationContext(), textViewResourceId, feedDao);
        Log.d(TAG, "constructor FeedAdapter");
        this.feedDaoList = new ArrayList<FeedDao>(feedDao);
        this.context = activity.getApplicationContext();
        ((InjectableActivity)activity).inject(this);
        prettyTime = new PrettyTime();
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        Log.d(TAG, "default time zone 0" + TimeZone.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        if (feedDaoList.get(position).getFeedType()==FeedType.BOTTLE) {
            return FEED_BOTTLE;
        } else {
            return FEED_NURSING;
        }
    }



    @Override
    public View getView( final int position, View convertView, ViewGroup parent ) {
        View view = convertView;

//        DiaperChangeView view = (DiaperChangeView) convertView;
//        ViewHolder viewHolder;


        int type = getItemViewType(position);

        if (view == null) {

            switch (type) {
                case FEED_BOTTLE:
                    view= getBottleFeedView(position, convertView, parent);

                break;
                case FEED_NURSING:
                    view= getNursingView(position, convertView, parent);

                break;
            }

        }

        return view;
    }

    private View getNursingView(int position, View convertView, ViewGroup parent) {

        final FeedDao feedDao = feedDaoList.get(position);

//        if (convertView == null) {
        LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(R.layout.list_item_breast_feeding, parent, false);
        NursingFeedView view = (NursingFeedView) convertView;
        view.setModel(feedDao);


//        NursingFeedViewHolder vh = null;
//        if(convertView == null) {
//            convertView = mInflater.inflate(R.layout.list_item_breast_feeding, parent, false);
//            if(convertView != null) {
//                vh = new NursingFeedViewHolder();
//                vh.left = (TextView) convertView.findViewById(R.id.txtLeft);
//                vh.right = (TextView) convertView.findViewById(R.id.txtRight);
//                vh.textViewTime = (TextView) convertView.findViewById(R.id.diaperChangeTime);
//                vh.notesTextView = (TextView) convertView.findViewById(R.id.notes_content);
//
//                convertView.setTag(vh);
//            }
//        }
//        else {
//            vh = (NursingFeedViewHolder) convertView.getTag();
//        }
//        final FeedDao feedDao = feedDaoList.get(position);
//
//        Log.d(TAG, feedDao.toString() + " left " + feedDao.getLeftBreastTime() + " right " + feedDao.getRightBreastTime());
//
//        vh.left.setText("Left Breast: " +  feedDao.getLeftBreastTime() + " seconds ");
//        vh.right.setText("Left Breast: " +  feedDao.getRightBreastTime() + " seconds ");
//        vh.textViewTime.setText(simpleDateFormat.format(new Date(feedDao.getDate().getTime())));
//        vh.notesTextView.setText(feedDao.getNotes());


        return view;
    }

    private View getBottleFeedView(int position, View convertView, ViewGroup parent) {
        final FeedDao feedDao = feedDaoList.get(position);

//        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.list_item_bottle_feeding, parent, false);
            BottleFeedView view = (BottleFeedView) convertView;
            view.setModel(feedDao);
//            viewHolder = new ViewHolder(view);
//        }
//        if(convertView == null) {
//            convertView = mInflater.inflate(R.layout.list_item_bottle_feeding, parent, false);
//            if(convertView != null) {
//                vh = new BottleFeedViewHolder();
//                vh.txtType = (TextView) convertView.findViewById(R.id.txt_type);
//                vh.textViewTime = (TextView) convertView.findViewById(R.id.diaperChangeTime);
//                vh.notesTextView = (TextView) convertView.findViewById(R.id.notes_content);
//
//                convertView.setTag(vh);
//            }
//        }
//        else {
//            vh = (BottleFeedViewHolder) convertView.getTag();
//        }
//        final FeedDao feedDao = feedDaoList.get(position);

//        vh.txtType.setText(feedDao.getFeedItem() + " " + feedDao.getQuantity());
//        vh.textViewTime.setText(simpleDateFormat.format(new Date(feedDao.getDate().getTime())));
//        vh.notesTextView.setText(feedDao.getNotes());

        return convertView;
    }


    @Override
    public int getCount() {
        return feedDaoList.size();
    }

    public void update(List<FeedDao> feedDaoList) {
        Log.d(TAG, "update");
        this.feedDaoList = new ArrayList<FeedDao>(feedDaoList);
        notifyDataSetInvalidated();
        notifyDataSetChanged();
    }


//    public static class NursingFeedViewHolder extends ViewHolder{
//        TextView left;
//        TextView right;
//        TextView textViewTime;
//        TextView notesTextView;
//
//
//
//    }
//
//    public static class BottleFeedViewHolder extends  ViewHolder{
//        TextView txtType;
//        TextView textViewTime;
//        TextView notesTextView;
//
//
//    }
//
//    public static class ViewHolder {
//        TextView textViewTime;
//    }




}
