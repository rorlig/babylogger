package com.rorlig.babyapp.ui.adapter.parse;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.MilestoneItemClicked;
import com.rorlig.babyapp.parse_dao.Milestones;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.gc.materialdesign.views.CheckBox;

/**
 * Created by rorlig on 5/29/15.
 */
public class MilestonesItemAdapter2 extends ArrayAdapter<ParseObject, MilestonesItemAdapter2.ViewHolder> {

    private final String TAG = "MilestonesItemAdapter";
    private Context context;
    private final SimpleDateFormat simpleDateFormat;

    //    private final int[] itemStates;
//    private String[] itemNames = {};


    @Inject
    Picasso picasso;


    public MilestonesItemAdapter2( List<ParseObject> parseObjectList) {
        super(parseObjectList);
//        this.parseObjectList = parseObjectList;
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }







    @Override
    public MilestonesItemAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View growthView = inflater.inflate(R.layout.list_item_milestones, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(growthView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MilestonesItemAdapter2.ViewHolder viewHolder, int position) {
        Milestones model = (Milestones) mObjects.get(position);
        viewHolder.logItemLabel.setText(model.getTitle());

        if (model.getParseFile()==null || model.getParseFile().getUrl().equals("")) {
            viewHolder.itemImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mood_black));
        } else {
            picasso.with(context).load(model.getParseFile().getUrl()).into(viewHolder.itemImage);
//            viewHolder.itemImage.setImageURI(milestoneModel.getParseFile().getUrl());
        }

        viewHolder.dateCompletionText.setText(simpleDateFormat.format(model.getLogCreationDate()));

    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView logItemLabel;
        CircleImageView itemImage;
        TextView dateCompletionText;

        public ViewHolder(View itemView) {
            super(itemView);

            logItemLabel = (TextView) itemView.findViewById(R.id.log_item_label);
            itemImage = (CircleImageView) itemView.findViewById(R.id.icon_image);
            dateCompletionText = (TextView) itemView.findViewById(R.id.date_completed);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.d(TAG, "onClick v ");
            int position = getLayoutPosition();
            Milestones milestones = (Milestones) mObjects.get(position);
//            Toast.makeText(context, "hello ", Toast.LENGTH_LONG).show();
            scopedBus.post(new MilestoneItemClicked(milestones, position));

        }
    }


}
