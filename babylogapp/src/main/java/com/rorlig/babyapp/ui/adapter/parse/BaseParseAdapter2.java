package com.rorlig.babyapp.ui.adapter.parse;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ocpsoft.pretty.time.PrettyTime;
import com.parse.ParseObject;
import com.rorlig.babyapp.ui.activity.InjectableActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * @author gaurav gupta
 */
public abstract class BaseParseAdapter2<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private SimpleDateFormat simpleDateFormat;
    private final String TAG = "BaseParseAdapter";
    private Context context;
    protected List<ParseObject> parseObjectList;



    public BaseParseAdapter2(List<ParseObject> parseObjectList)  {
        this.parseObjectList = parseObjectList;
    }



    public BaseParseAdapter2(Activity activity, int textViewResourceId, List<ParseObject> parseObjectList) {
        this.parseObjectList = parseObjectList;
        this.context = activity.getApplicationContext();
        ((InjectableActivity)activity).inject(this);
        PrettyTime prettyTime = new PrettyTime();
        simpleDateFormat = new SimpleDateFormat("MMM d, ''yy h:mm a");
        Log.d(TAG, "default time zone 0" + TimeZone.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

    }

    @Override
    public int getItemCount() {
        return parseObjectList.size();
    }


}
