package com.rorlig.babylog.ui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dao.MilestonesDao;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.squareup.otto.Bus;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

//import com.gc.materialdesign.views.CheckBox;

/**
 * Created by rorlig on 5/29/15.
 */
public class MilestonesItemAdapter extends ArrayAdapter<MilestonesDao> {

    private final String TAG = "LogItemAdapter";
    private final Activity context;
    private final SimpleDateFormat simpleDateFormat;

    //    private final int[] itemStates;
    private List<MilestonesDao> logListItem;
//    private String[] itemNames = {};


    @Inject
    Bus bus;


    /**
     * Constructor
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     * @param logListItem
     */
    public MilestonesItemAdapter(Activity context, int resource, List<MilestonesDao> logListItem) {
        super(context, R.layout.list_item_home);
            this.context = context;
            this.logListItem = logListItem;

        simpleDateFormat = new SimpleDateFormat("MMM d");
        Log.d(TAG, "default time zone 0" + TimeZone.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

            Log.d(TAG, ""  + this.logListItem);
                ((InjectableActivity)context).inject(this);
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

        final MilestonesDao item = logListItem.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_milestones, parent, false);
            viewHolder = new ViewHolder(convertView);
            viewHolder.logItemLabel = (TextView) convertView.findViewById(R.id.log_item_label);
            viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.icon_image);
            viewHolder.dateCompletionText = (TextView) convertView.findViewById(R.id.date_completed);
            viewHolder.textCompleted = (TextView) convertView.findViewById(R.id.text_completed_status);
//            viewHolder.textCompleted.setText();



//            viewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(iconArr.getResourceId(position,0)));







//            viewHolder.logItemCheckBox.setEnabled(item.isItemChecked());
            Log.d(TAG, "position " + position + " item " + item);

//            if (item.isItemChecked()) {
//                viewHolder.logItemCheckBox.setEnabled(false);
//             }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        if (item.isCompleted()) {
//            viewHolder.itemImage.setImageResource(R.drawable.ic_mood_blue);
//            viewHolder.textCompleted.setText(R.string.txt_completed);
//            viewHolder.dateCompletionText.setText(simpleDateFormat.format(item.getCompletionDate()));
//        } else  {
//            viewHolder.itemImage.setImageResource(R.drawable.ic_mood_black);
//            viewHolder.textCompleted.setText(R.string.txt_not_completed);
//            viewHolder.dateCompletionText.setText(item.getCompletionDateRange());
//
//
//        }
        viewHolder.logItemLabel.setText(item.getTitle());
        viewHolder.itemImage.setImageURI(Uri.parse(item.getImagePath()));

                // Populate the data into the template view using the data object

//        if (item.isItemChecked()) {
//            viewHolder.logItemCheckBox.setEnabled(false);
//        }
//        viewHolder.logItemCheckBox.setText(item);
        // Return the completed view to render on screen
        return convertView;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {

        return logListItem.size();
    }

    /*
               *  View Holder class for individual list items
               */
    public  class ViewHolder {

        TextView logItemLabel;


        ImageView itemImage;


        TextView dateCompletionText;

        TextView textCompleted;


        public ViewHolder(View view){
            view.setTag(this);
        }
    }

//    public ArrayList<ItemModel> getLogListItem() {
//        return logListItem;
//    }


}
