package com.rorlig.babylog.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rorlig.babylog.R;
import com.rorlig.babylog.dao.GrowthDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author gaurav gupta
 */
public class GrowthView extends RelativeLayout {

    private GrowthDao model;
    private SimpleDateFormat simpleDateFormat;


    TextView txtType;
    TextView textViewTime;
    TextView notesTextView;
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
        textViewTime = (TextView) findViewById(R.id.diaperChangeTime);
        notesContentTextView = (TextView) findViewById(R.id.notes_content);

    }

    public void init() {
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }
    public void setModel(GrowthDao model) {
        this.model = model;
        bindModel();
    }

    private void bindModel() {
        headTextView.setText(model.getHeadMeasurement() + " inches");
        heightTextView.setText(model.getHeight() + " inches");
        weightTextView.setText(model.getWeight() + " pounds");
        textViewTime.setText(simpleDateFormat.format(new Date(model.getDate().getTime())));
        notesContentTextView.setText(model.getNotes());

    }

}
