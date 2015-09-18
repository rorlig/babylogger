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
import com.rorlig.babyapp.otto.GrowthItemClicked;
import com.rorlig.babyapp.otto.ScopedBus;
import com.rorlig.babyapp.parse_dao.Growth;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by rorlig on 6/9/15.
 */
public class GrowthAdapter2 extends ArrayAdapter<ParseObject, GrowthAdapter2.ViewHolder> {


    private final String TAG = "GrowthAdapter";
    private final SimpleDateFormat simpleDateFormat;


    private Context context;




    public GrowthAdapter2( List<ParseObject> parseObjectList) {
        super(parseObjectList);
//        this.parseObjectList = parseObjectList;
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }





    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View growthView = inflater.inflate(R.layout.list_item_growth, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(growthView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Growth model = (Growth) mObjects.get(position);
        holder.headTextView.setText(model.getHeadMeasurement()!=-1?model.getHeadMeasurement() + " inches": "");
        holder.heightTextView.setText(model.getHeight() + " inches");
        holder.weightTextView.setText(model.getWeight() + " pounds");
        holder.textViewTime.setText(simpleDateFormat.format(model.getLogCreationDate()));
        holder.notesContentTextView.setText(model.getNotes());
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTime;
        TextView headTextView;
        TextView heightTextView;
        TextView weightTextView;
        TextView notesContentTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            weightTextView = (TextView) itemView.findViewById(R.id.weight_value);
            heightTextView = (TextView) itemView.findViewById(R.id.height_value);
            headTextView = (TextView) itemView.findViewById(R.id.headValue);
            textViewTime = (TextView) itemView.findViewById(R.id.time);
            notesContentTextView = (TextView) itemView.findViewById(R.id.notes_content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.d(TAG, "onClick v ");
            int position = getLayoutPosition();
            Growth growth = (Growth) mObjects.get(position);
//            Toast.makeText(context, "hello ", Toast.LENGTH_LONG).show();
            scopedBus.post(new GrowthItemClicked(growth, position));

        }
    }
}
