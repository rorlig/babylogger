package com.rorlig.babyapp.ui.adapter.parse;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.ocpsoft.pretty.time.PrettyTime;
import com.parse.ParseObject;
import com.rorlig.babyapp.ui.activity.InjectableActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * @author gaurav gupta
 */
public class BaseParseAdapter<T extends ParseObject> extends ArrayAdapter<ParseObject> {

    private final SimpleDateFormat simpleDateFormat;
    private final String TAG = "BaseParseAdapter";
    private Context context;
    protected List<ParseObject> parseObjectList;


    public BaseParseAdapter(Activity activity,  int textViewResourceId, List<ParseObject> parseObjectList) {
        super(activity, textViewResourceId);
        this.parseObjectList = parseObjectList;
        this.context = activity.getApplicationContext();
        ((InjectableActivity)activity).inject(this);
        PrettyTime prettyTime = new PrettyTime();
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        Log.d(TAG, "default time zone 0" + TimeZone.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

    }

    @Override
    public int getCount() {
        return parseObjectList.size();
    }





    public void update(List<ParseObject> parseObjectList) {
        Log.d(TAG, "update");
//        this.parseObjectList = new ArrayList<>(parseObjectList);
        notifyDataSetInvalidated();
        notifyDataSetChanged();
    }


}
