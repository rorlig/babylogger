package com.rorlig.babyapp.ui.adapter.parse;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.model.feed.FeedType;
import com.rorlig.babyapp.parse_dao.Feed;
import com.rorlig.babyapp.ui.widget.BottleFeedView;
import com.rorlig.babyapp.ui.widget.NursingFeedView;

import java.util.List;

/**
 * Created by admin on 4/22/14.
 * todo the UI requires considerable skinning....
 */
public class FeedAdapter extends BaseParseAdapter<ParseObject> {

    private static final int FEED_BOTTLE = 0;
    private static final int FEED_NURSING = 1;


    private String TAG="FeedAdapter";

    public FeedAdapter(Activity activity,
                               int textViewResourceId,
                               List<ParseObject> parseObjectList) {
        super(activity, textViewResourceId, parseObjectList);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        if (((Feed)parseObjectList.get(position)).getFeedType().equals(FeedType.BOTTLE.toString())) {
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

        final Feed feedDao = (Feed)parseObjectList.get(position);

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
        final Feed feed = (Feed) parseObjectList.get(position);

//        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.list_item_bottle_feeding, parent, false);
            BottleFeedView view = (BottleFeedView) convertView;
            view.setModel(feed);
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
    public Feed getItem(int position) {
        return (Feed) parseObjectList.get(position);
    }

//    public void update(List<FeedDao> feedDaoList) {
//        Log.d(TAG, "update");
//        this.feedDaoList = new ArrayList<FeedDao>(feedDaoList);
//        notifyDataSetInvalidated();
//        notifyDataSetChanged();
//    }


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
