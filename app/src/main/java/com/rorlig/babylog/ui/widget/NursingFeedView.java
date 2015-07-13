package com.rorlig.babylog.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dao.FeedDao;

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
        left.setText("Left Breast: " + model.getLeftBreastTime() + " seconds");
        right.setText("Right Breast: " +  model.getRightBreastTime() + " seconds ");
        textViewTime.setText(simpleDateFormat.format(model.getDate()));
        notesTextView.setText(model.getNotes());
    }

}
