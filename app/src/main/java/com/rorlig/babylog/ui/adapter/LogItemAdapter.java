package com.rorlig.babylog.ui.adapter;

import android.app.Activity;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

//import com.gc.materialdesign.views.CheckBox;
import com.rorlig.babylog.R;
import com.rorlig.babylog.model.ItemModel;
import com.rorlig.babylog.otto.ItemCheckChangeEvent;
import com.rorlig.babylog.ui.activity.InjectableActivity;
import com.squareup.otto.Bus;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by rorlig on 5/29/15.
 */
public class LogItemAdapter extends ArrayAdapter<ItemModel> {

    private final String TAG = "LogItemAdapter";
    private final TypedArray iconArr;
    private final Activity context;
    //    private final int[] itemStates;
    private ArrayList<ItemModel> logListItem;
//    private String[] itemNames = {};


    @Inject
    Bus bus;


    /**
     * Constructor
     *  @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     * @param logListItem
     */
    public LogItemAdapter(Activity context, int resource, ArrayList<ItemModel> logListItem) {
    super(context, R.layout.list_item_log);
        this.context = context;
        this.logListItem = new ArrayList<ItemModel>(logListItem);

        iconArr = context.getResources().obtainTypedArray(R.array.itemIcons);



        Log.d(TAG, ""  + this.logListItem);
//        itemNames = context.getResources().getStringArray(R.array.items);
//
//        itemStates = context.getResources().getIntArray(R.array.itemStates);
//        int index = 0;
//        for (; index<itemStates.length; ++index) {
//            ItemModel itemModel;
//            Log.d(TAG, "itemState " + itemStates[index] + " item " + itemNames[index]);
//            if (itemStates[index]==1) {
//                Log.d(TAG, "1");
//                 itemModel = new ItemModel(itemNames[index], true);
//                itemModel.setItemChecked(true);
//
//            } else  {
//                itemModel = new ItemModel(itemNames[index], false);
//
//            }
//
////            itemModel = new ItemModel(itemNames[index], false);
//
//            logListItem.add(itemModel);

            ((InjectableActivity)context).inject(this);

//            ++index;
        }


//        Log.d(TAG, "listitem size " + logListItem.length);


    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ItemModel item = logListItem.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_log, parent, false);
            viewHolder = new ViewHolder(convertView);
            viewHolder.logItemLabel = (TextView) convertView.findViewById(R.id.log_item_label);
            viewHolder.logItemCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.icon_image);

            viewHolder.logItemLabel.setText(item.getItemName());
            viewHolder.logItemCheckBox.setChecked(item.isItemChecked());

            viewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(iconArr.getResourceId(position,0)));


            viewHolder.logItemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                /**
                 * Called when the checked state of a compound button has changed.
                 *
                 * @param buttonView The compound button view whose state has changed.
                 * @param isChecked  The new checked state of buttonView.
                 */
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    item.setItemChecked(!item.isItemChecked());

                    bus.post(new ItemCheckChangeEvent(position));

                }

//                @Override
//                public void onCheck(CheckBox checkBox, boolean b) {
//                    Log.d(TAG, "toggling item");
//                    item.setItemChecked(!item.isItemChecked());
//                }

            });




//            viewHolder.logItemCheckBox.setEnabled(item.isItemChecked());
            Log.d(TAG, "position " + position + " item " + item);

//            if (item.isItemChecked()) {
//                viewHolder.logItemCheckBox.setEnabled(false);
//             }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
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

        CheckBox logItemCheckBox;

        ImageView itemImage;




        public ViewHolder(View view){
            view.setTag(this);
        }
    }

    public ArrayList<ItemModel> getLogListItem() {
        return logListItem;
    }

    public int enabledCount() {
        int enabled = 0;
        for (ItemModel itemModel : logListItem) {
            if (itemModel.isItemChecked()) {
                enabled++;
            }
        }

        Log.d(TAG, "enabled count " + enabled);

        return enabled;
    }
}
