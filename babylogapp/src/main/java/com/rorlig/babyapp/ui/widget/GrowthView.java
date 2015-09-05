package com.rorlig.babyapp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.parse_dao.Growth;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author gaurav gupta
 */
public class GrowthView extends RelativeLayout {

    private Growth model;
    private SimpleDateFormat simpleDateFormat;


    TextView textViewTime;
    private TextView headTextView;
    private TextView heightTextView;
    private TextView weightTextView;
    private TextView notesContentTextView;


    public GrowthView(Context context) {
        super(context);
        init();
    }

    public GrowthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public GrowthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    protected void onFinishInflate() {

        weightTextView = (TextView) findViewById(R.id.weight_value);
        heightTextView = (TextView) findViewById(R.id.height_value);
        headTextView = (TextView) findViewById(R.id.headValue);
        textViewTime = (TextView) findViewById(R.id.time);
        notesContentTextView = (TextView) findViewById(R.id.notes_content);


    }

    public void init() {
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }
    public void setModel(ParseObject model) {
        this.model = (Growth) model;
        bindModel();
    }

    private void bindModel() {
        headTextView.setText(model.getHeadMeasurement()!=-1?model.getHeadMeasurement() + " inches": "");
        heightTextView.setText(model.getHeight() + " inches");
        weightTextView.setText(model.getWeight() + " pounds");
        textViewTime.setText(simpleDateFormat.format(model.getLogCreationDate()));
        notesContentTextView.setText(model.getNotes());

    }

}
