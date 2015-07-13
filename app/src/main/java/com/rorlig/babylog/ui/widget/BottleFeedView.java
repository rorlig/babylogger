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
public class BottleFeedView extends RelativeLayout {

    private FeedDao model;
    private SimpleDateFormat simpleDateFormat;


    TextView txtType;
    TextView textViewTime;
    TextView notesTextView;



    public BottleFeedView(Context context) {
        super(context);
        init();
    }

    public BottleFeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public BottleFeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    protected void onFinishInflate() {

        txtType = (TextView) findViewById(R.id.txt_type);
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
        txtType.setText(model.getFeedType().toString());
        textViewTime.setText(simpleDateFormat.format(model.getDate()));
        notesTextView.setText(model.getNotes());
    }

}
