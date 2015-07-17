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
import com.rorlig.babylog.dao.SleepDao;
import com.rorlig.babylog.model.diaper.DiaperChangeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        sleepDuration.setText(Long.toString(sleepDao.getDuration()));
    }






}
