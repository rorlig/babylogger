package com.rorlig.babyapp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dao.FeedDao;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author gaurav gupta
 */
public class NursingFeedView extends RelativeLayout {

    private FeedDao model;
    private SimpleDateFormat simpleDateFormat;


    TextView textViewTime;
    TextView notesTextView;
    private TextView left;
    private TextView right;


    public NursingFeedView(Context context) {
        super(context);
        init();
    }

    public NursingFeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public NursingFeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    protected void onFinishInflate() {

        left = (TextView) findViewById(R.id.txtLeft);
        right = (TextView) findViewById(R.id.txtRight);
        textViewTime = (TextView) findViewById(R.id.diaperChangeTime);
        notesTextView = (TextView) findViewById(R.id.notes_content);

    }

    public void init() {
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }

    public void setModel(FeedDao model) {
        this.model = model;
        bindModel();
    }

    private void bindModel() {
        left.setText("Left Breast: " + toHoursandMinutes(model.getLeftBreastTime()) );
        right.setText("Right Breast: " +  toHoursandMinutes(model.getRightBreastTime()) );
        textViewTime.setText(simpleDateFormat.format(model.getDate()));
        notesTextView.setText(model.getNotes());
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
