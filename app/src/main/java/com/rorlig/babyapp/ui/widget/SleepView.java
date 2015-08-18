package com.rorlig.babyapp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dao.SleepDao;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author gaurav gupta
 */
public class SleepView extends RelativeLayout {


    private final Context context;
    private SimpleDateFormat simpleDateFormat;

//    @InjectView(R.id.diaperChangeTime)
    TextView textViewTime;




//    @InjectView(R.id.row2)
    private TextView sleepStartTime;
    private TextView sleepDuration;
    private SleepDao sleepDao;


//    public ViewHolder(View view){
//
//    }

    public SleepView(Context context) {
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


    public SleepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SleepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    protected void onFinishInflate() {
        this.textViewTime = (TextView) findViewById(R.id.sleep_time);
        this.sleepStartTime = (TextView) findViewById(R.id.sleep_start_time);
        this.sleepDuration = (TextView) findViewById(R.id.sleep_duration);

    }


    public void setModel(SleepDao sleepDao) {
        this.sleepDao = sleepDao;
        bindModel();
    }

    private void bindModel() {
        textViewTime.setText(simpleDateFormat.format(sleepDao.getDate()));
        sleepStartTime.setText(simpleDateFormat.format(sleepDao.getSleepStartTime()));
        sleepDuration.setText(SleepView.toHoursandMinutes(sleepDao.getDuration()));
    }

    private static String toHoursandMinutes(Long duration) {
        long hours = duration/60;
        long minutes = duration % 60;
        if (hours==0) {
            return minutes + " minutes";
        } else  {
            return hours + " hours and " + minutes + " minutes";
        }
    }


}
