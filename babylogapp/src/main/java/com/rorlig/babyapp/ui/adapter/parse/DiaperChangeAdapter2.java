package com.rorlig.babyapp.ui.adapter.parse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ObjectGraphUtils;
import com.rorlig.babyapp.model.diaper.DiaperChangeEnum;
import com.rorlig.babyapp.otto.DiaperChangeItemClickedEvent;
import com.rorlig.babyapp.otto.ScopedBus;
import com.rorlig.babyapp.parse_dao.DiaperChange;
import com.rorlig.babyapp.ui.activity.InjectableActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

/**
 * Created by admin on 4/22/14.
 * todo the UI requires considerable skinning....
 */
public class DiaperChangeAdapter2 extends BaseParseAdapter2<DiaperChangeAdapter2.ViewHolder> {


    private String TAG="DiaperChangeAdapter2";
    private Context context;


    ScopedBus scopedBus = new ScopedBus();


    public DiaperChangeAdapter2( List<ParseObject> parseObjectList) {
        super(parseObjectList);
        this.parseObjectList = parseObjectList;
    }






    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

//        ObjectGraphUtils.getApplicationGraph(context);
//        ((InjectableActivity)context).inject(this);

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View diaperView = inflater.inflate(R.layout.list_item_diaper_change, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(diaperView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiaperChange diaperChange = (DiaperChange) parseObjectList.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        holder.textViewTime.setText(simpleDateFormat.format(diaperChange.getLogCreationDate()));
        setPoopColor(holder, diaperChange);
        setPoopTexture(holder, diaperChange);
        setDiaperIncidentType(holder, diaperChange);
        setDiaperChangeType(holder, diaperChange);

        holder.notesContent.setText(diaperChange.getDiaperChangeNotes()!=null ? "" + diaperChange.getDiaperChangeNotes(): "");

    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //    @InjectView(R.id.diaperWetChecked)
        ImageView diaperWetChecked;

        //    @InjectView(R.id.diaperPoopChecked)
        ImageView diaperPoopChecked;

        //    @InjectView(R.id.diaperChangeTime)
        TextView textViewTime;

        //    @InjectView(R.id.poopColor)
        ImageView poopColor;

        //    @InjectView(R.id.poopTexture)
        TextView textViewPoopTexture;

        //    @InjectView(R.id.notesContent)
        TextView notesContent;

        //    @InjectView(R.id.incidentDetails)
        TextView incidentDetails;

        //    @InjectView(R.id.row2)
        LinearLayout row2;


        public ViewHolder(View itemView) {
            super(itemView);

            diaperWetChecked = (ImageView) itemView.findViewById(R.id.diaperWetChecked);
            diaperPoopChecked = (ImageView) itemView.findViewById(R.id.diaperPoopChecked);
            textViewTime = (TextView) itemView.findViewById(R.id.diaperChangeTime);
            poopColor = (ImageView) itemView.findViewById(R.id.poopColor);
            textViewPoopTexture = (TextView) itemView.findViewById(R.id.poopTexture);
            notesContent = (TextView) itemView.findViewById(R.id.notes_content);
            incidentDetails = (TextView) itemView.findViewById(R.id.incidentDetails);
            row2 = (LinearLayout) itemView.findViewById(R.id.row2);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick v ");
            int position = getLayoutPosition();
            DiaperChange diaperChange = (DiaperChange) parseObjectList.get(position);
//            Toast.makeText(context, "hello ", Toast.LENGTH_LONG).show();
            scopedBus.post(new DiaperChangeItemClickedEvent(diaperChange, position));
        }


    }



    private void setDiaperChangeType(ViewHolder viewHolder, DiaperChange diaperChange) {

//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
//            if (DiaperChange.getDiaperChangeEventType()== DiaperChangeEnum.BOTH) {
//                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
//                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
//            } else if (DiaperChange.getDiaperChangeEventType()==DiaperChangeEnum.WET){
//                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
//                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_unselected));
//                row2.setVisibility(View.GONE);
//            } else {
//                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_unselected));
//                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
//            }
//        } else {

//        Log.d(TAG, "diaperChange Type " + diaperChange.getDiaperChangeEventType());
        if (diaperChange.getDiaperChangeEventType().equals(DiaperChangeEnum.BOTH.toString())) {
            viewHolder.diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
            viewHolder.diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
        } else if (diaperChange.getDiaperChangeEventType().equals(DiaperChangeEnum.WET.toString())){
            viewHolder.diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
            viewHolder.diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_unselected));
            viewHolder.row2.setVisibility(View.GONE);
        } else {
            viewHolder.diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_unselected));
            viewHolder.diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
        }
//        }

    }


    private void setPoopColor(ViewHolder viewHolder, DiaperChange diaperChange) {
        if (diaperChange.getPoopColor()!=null) {
            switch (diaperChange.getPoopColor()) {
                case "COLOR_1":
                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_1));
                    break;
                case "COLOR_2":
                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_2));
                    break;
                case "COLOR_3":
                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_3));
                    break;
                case "COLOR_4":
                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_4));
                    break;
                case "COLOR_5":
                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_5));
                    break;
                case "COLOR_6":
                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_6));
                    break;
                case "COLOR_7":
                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_7));
                    break;
                case "COLOR_8":
                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_8));
                    break;
                default:
                    viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.white));
                    break;



            }
        } else viewHolder.poopColor.setBackgroundColor(context.getResources().getColor(R.color.white));

    }

    private void setPoopTexture(ViewHolder viewHolder, DiaperChange diaperChange) {
        if (diaperChange.getPoopTexture()!=null) {
            viewHolder.textViewPoopTexture.setText(diaperChange.getPoopTexture().toString());
        } else {
            viewHolder.textViewPoopTexture.setText("N/A");
        }
    }

    private void setDiaperIncidentType(ViewHolder viewHolder, DiaperChange diaperChange) {
        if (diaperChange.getDiaperChangeIncidentType()!=null) {
            viewHolder.incidentDetails.setText(diaperChange.getDiaperChangeIncidentType().toString());
        }

    }
}
