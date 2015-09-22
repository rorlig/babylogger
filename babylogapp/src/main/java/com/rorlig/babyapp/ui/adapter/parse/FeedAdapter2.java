package com.rorlig.babyapp.ui.adapter.parse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.model.feed.FeedType;
import com.rorlig.babyapp.otto.FeedItemClickedEvent;
import com.rorlig.babyapp.otto.ScopedBus;
import com.rorlig.babyapp.parse_dao.Feed;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by admin on 4/22/14.
 * todo the UI requires considerable skinning....
 */
public class FeedAdapter2 extends  ArrayAdapter<ParseObject, RecyclerView.ViewHolder> {

    private static final int FEED_BOTTLE = 0;
    private static final int FEED_NURSING = 1;


    private static String TAG="FeedAdapter";
    private Context context;

    ScopedBus scopedBus = new ScopedBus();

    public FeedAdapter2(List<ParseObject> objects) {
        super(objects);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bottleView = inflater.inflate(R.layout.list_item_bottle_feeding, parent, false);

        View nursingView =  inflater.inflate(R.layout.list_item_breast_feeding, parent, false);

        Log.d(TAG, " viewtype " + viewType);

        switch (viewType) {
            case FEED_BOTTLE:
                return new BottleFeedViewHolder(bottleView);
            default:
                return new NursingViewHolder(nursingView);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Feed feed = (Feed) mObjects.get(position);
        Log.d(TAG, "feed " + feed + " holder " + holder);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        Log.d(TAG, "viewType at the position " + getItemViewType(position));
        switch (getItemViewType(position)){
            case FEED_BOTTLE:
                BottleFeedViewHolder bottleFeedViewHolder = (BottleFeedViewHolder) holder;
                bottleFeedViewHolder.txtQuantity.setText(feed.getQuantity().toString() + " oz of " + feed.getFeedItem());
                bottleFeedViewHolder.textViewTime.setText(simpleDateFormat.format(feed.getLogCreationDate()));
                bottleFeedViewHolder.notesTextView.setText(feed.getNotes()==null?"":feed.getNotes());
                break;
            case FEED_NURSING:
                NursingViewHolder nursingViewHolder = (NursingViewHolder) holder;
                if (nursingViewHolder!=null) {
                    nursingViewHolder.left.setText("Left Breast: " + toHoursandMinutes(feed.getLeftBreastTime()));
                    nursingViewHolder.right.setText("Right Breast: " + toHoursandMinutes(feed.getRightBreastTime()));
                    nursingViewHolder.notesTextView.setText(feed.getNotes()==null?"":feed.getNotes());
                    nursingViewHolder.textViewTime.setText(simpleDateFormat.format(feed.getLogCreationDate()));
                }

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (((Feed)mObjects.get(position)).getFeedType().equals(FeedType.BOTTLE.toString())) {
            return FEED_BOTTLE;
        } else {
            return FEED_NURSING;
        }
    }








    public class BottleFeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView txtQuantity;
        TextView textViewTime;
        TextView notesTextView;

        public BottleFeedViewHolder(View itemView) {
            super(itemView);
            txtQuantity = (TextView) itemView.findViewById(R.id.txt_quantity);
            textViewTime = (TextView) itemView.findViewById(R.id.diaperChangeTime);
            notesTextView = (TextView) itemView.findViewById(R.id.notes_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            Feed feed = (Feed) mObjects.get(position);
//            Toast.makeText(context, "hello ", Toast.LENGTH_LONG).show();
            scopedBus.post(new FeedItemClickedEvent(feed, position));

        }
    }


    public class NursingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//        EditText leftBreastFeedHours;
//        EditText leftBreastFeedMinutes;
//        EditText rightBreastFeedHours;
//        EditText rightBreastFeedMinutes;
//        EditText notes;

        TextView textViewTime;
        TextView notesTextView;
        TextView left;
        TextView right;



        public NursingViewHolder(View itemView) {
            super(itemView);
            textViewTime = (TextView) itemView.findViewById(R.id.diaperChangeTime);
            notesTextView = (TextView) itemView.findViewById(R.id.notes_content);
            left = (TextView) itemView.findViewById(R.id.txtLeft);
            right = (TextView) itemView.findViewById(R.id.txtRight);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getLayoutPosition();
            Feed feed = (Feed) mObjects.get(position);
//            Toast.makeText(context, "hello ", Toast.LENGTH_LONG).show();
            scopedBus.post(new FeedItemClickedEvent(feed, position));

        }
    }



    private static String toHoursandMinutes(Long duration) {

        Log.d(TAG, "duration: " + duration);

        long hours = duration/60;
        long minutes = duration % 60;

        Log.d(TAG, "hours " + hours + " minutes " + minutes);

        if (hours==0) {
            return minutes + " minutes";
        } else  {
            return hours + " hours and " + minutes + " minutes";
        }
    }



}
