package com.rorlig.babyapp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.parse_dao.Feed;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author gaurav gupta
 */
public class BottleFeedView extends RelativeLayout {

    private Feed model;
    private SimpleDateFormat simpleDateFormat;


    TextView txtQuantity;
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
        txtQuantity = (TextView) findViewById(R.id.txt_quantity);
        textViewTime = (TextView) findViewById(R.id.diaperChangeTime);
        notesTextView = (TextView) findViewById(R.id.notes_content);
    }

    public void init() {
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }
    public void setModel(Feed model) {
        this.model = model;
        bindModel();
    }

    private void bindModel() {
        txtQuantity.setText(model.getQuantity().toString() + " oz of " + model.getFeedItem());
        textViewTime.setText(simpleDateFormat.format(model.getLogCreationDate()));
        notesTextView.setText(model.getNotes());
    }

}
