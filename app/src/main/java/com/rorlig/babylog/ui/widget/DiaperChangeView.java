package com.rorlig.babylog.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.model.diaper.DiaperChangeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author gaurav gupta
 */
public class DiaperChangeView extends RelativeLayout {


    private final Context context;
    private SimpleDateFormat simpleDateFormat;

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

    private DiaperChangeDao diaperChangeDao;


//    public ViewHolder(View view){
//
//    }

    public DiaperChangeView(Context context) {
        super(context);
        this.context = context;
        init();




//        ButterKnife.inject(this, view);
//        view.setTag(this);
    }

    private void init() {
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }


    public DiaperChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DiaperChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    protected void onFinishInflate() {
        this.diaperWetChecked = (ImageView) findViewById(R.id.diaperWetChecked);
        this.diaperPoopChecked = (ImageView) findViewById(R.id.diaperPoopChecked);
        this.textViewTime = (TextView) findViewById(R.id.diaperChangeTime);
        this.poopColor = (ImageView) findViewById(R.id.poopColor);
        this.textViewPoopTexture = (TextView) findViewById(R.id.poopTexture);
        this.notesContent = (TextView) findViewById(R.id.notes_content);
        this.incidentDetails = (TextView) findViewById(R.id.incidentDetails);
        this.row2 = (LinearLayout) findViewById(R.id.row2);

    }


    public void setModel(DiaperChangeDao diaperChangeDao) {
        this.diaperChangeDao = diaperChangeDao;
        bindModel();
    }

    private void bindModel() {

        textViewTime.setText(simpleDateFormat.format(new Date(diaperChangeDao.getDate().getTime())));
        setPoopColor();
        setPoopTexture();
        setDiaperIncidentType();
//        notesContent.setText("" + diaperChangeDao.getDiaperChangeN);

    }

    @SuppressLint("NewApi")
    private void setDiaperChangeType() {

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            if (diaperChangeDao.getDiaperChangeEventType()== DiaperChangeEnum.BOTH) {
                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
            } else if (diaperChangeDao.getDiaperChangeEventType()==DiaperChangeEnum.WET){
                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_unselected));
                row2.setVisibility(View.GONE);
            } else {
                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_unselected));
                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
            }
        } else {
            if (diaperChangeDao.getDiaperChangeEventType()== DiaperChangeEnum.BOTH) {
                diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
                diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
            } else if (diaperChangeDao.getDiaperChangeEventType()==DiaperChangeEnum.WET){
                diaperWetChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_selected));
                diaperPoopChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_tick_unselected));
                row2.setVisibility(View.GONE);
            } else {
                diaperWetChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_unselected));
                diaperPoopChecked.setImageDrawable(context.getDrawable(R.drawable.ic_action_tick_selected));
            }
        }

    }


    private void setPoopColor() {
        if (diaperChangeDao.getPoopColor()!=null) {
            switch (diaperChangeDao.getPoopColor()) {
                case COLOR_1:
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_1));
                    break;
                case COLOR_2:
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_2));
                    break;
                case COLOR_3:
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_3));
                    break;
                case COLOR_4:
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_4));
                    break;
                case COLOR_5:
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_5));
                    break;
                case COLOR_6:
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_6));
                    break;
                case COLOR_7:
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_7));
                    break;
                case COLOR_8:
                    poopColor.setBackgroundColor(context.getResources().getColor(R.color.poop_color_8));
                    break;

            }
        }

    }

    private void setPoopTexture() {
        if (diaperChangeDao.getPoopTexture()!=null) {
            textViewPoopTexture.setText(diaperChangeDao.getPoopTexture().toString());
        }
    }

    private void setDiaperIncidentType() {
        if (diaperChangeDao.getDiaperChangeIncidentType()!=null) {
            incidentDetails.setText(diaperChangeDao.getDiaperChangeIncidentType().toString());
        }

    }


}
