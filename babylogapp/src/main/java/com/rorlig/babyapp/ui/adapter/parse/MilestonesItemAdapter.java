package com.rorlig.babyapp.ui.adapter.parse;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dao.MilestonesDao;
import com.rorlig.babyapp.parse_dao.Milestones;
import com.rorlig.babyapp.parse_dao.Sleep;
import com.rorlig.babyapp.ui.activity.InjectableActivity;
import com.squareup.otto.Bus;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.gc.materialdesign.views.CheckBox;

/**
 * Created by rorlig on 5/29/15.
 */
public class MilestonesItemAdapter extends BaseParseAdapter<ParseObject> {

    private final String TAG = "MilestonesItemAdapter";
    private final Context context;
    private final SimpleDateFormat simpleDateFormat;

    //    private final int[] itemStates;
    private List<MilestonesDao> logListItem;
//    private String[] itemNames = {};


    @Inject
    Bus bus;


    public MilestonesItemAdapter(Activity activity,
                       int textViewResourceId,
                       List<ParseObject> parseObjectList) {
        super(activity, textViewResourceId, parseObjectList);
        this.context = activity.getApplicationContext();
        simpleDateFormat = new SimpleDateFormat("MMM d");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

    }





    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

//        final MilestonesDao item = logListItem.get(position);

        final Milestones milestoneModel = (Milestones) (parseObjectList.get(position));


        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_milestones, parent, false);
            viewHolder = new ViewHolder(convertView);
            viewHolder.logItemLabel = (TextView) convertView.findViewById(R.id.log_item_label);
            viewHolder.itemImage = (CircleImageView) convertView.findViewById(R.id.icon_image);
            viewHolder.dateCompletionText = (TextView) convertView.findViewById(R.id.date_completed);
//            Log.d(TAG, "position " + position + " item " + item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.logItemLabel.setText(milestoneModel.getTitle());
        if (milestoneModel.getImagePath()==null || milestoneModel.getImagePath().equals("")) {
            viewHolder.itemImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mood_black));
        } else {
            viewHolder.itemImage.setImageURI(Uri.parse(milestoneModel.getImagePath()));
        }

        viewHolder.dateCompletionText.setText(simpleDateFormat.format(milestoneModel.getLogCreationDate()));


        return convertView;
    }

    @Override
    public Milestones getItem(int position) {
        return (Milestones) parseObjectList.get(position);
    }

    /*
    *  View Holder class for individual list items
    */
    public  class ViewHolder {

        TextView logItemLabel;


        CircleImageView itemImage;


        TextView dateCompletionText;

//        TextView textCompleted;


        public ViewHolder(View view){
            view.setTag(this);
        }
    }

//    public ArrayList<ItemModel> getLogListItem() {
//        return logListItem;
//    }


}
