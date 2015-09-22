package com.rorlig.babyapp.ui.adapter.parse;

import com.parse.ParseObject;
import com.rorlig.babyapp.utils.AppConstants;

import java.util.List;

/**
 * Created by Gaurav on 9/21/15.
 * factory method return the correct adapter
 */
public class ArrayAdapterFactory {

    public static ArrayAdapter getAdapter(String parseName, List<ParseObject> parseObjectList){
        switch (parseName) {
            case AppConstants.PARSE_CLASS_DIAPER:
                return new DiaperChangeAdapter2(parseObjectList);
            case AppConstants.PARSE_CLASS_FEED:
                return new FeedAdapter2(parseObjectList);
            case AppConstants.PARSE_CLASS_GROWTH:
                return new GrowthAdapter2(parseObjectList);
            case AppConstants.PARSE_CLASS_MILESTONE:
                return new MilestonesItemAdapter2(parseObjectList);
            case AppConstants.PARSE_CLASS_SLEEP:
                return new SleepAdapter2(parseObjectList);
        }
        return new DiaperChangeAdapter2(parseObjectList);
    }


}
