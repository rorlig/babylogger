package com.rorlig.babylog.ui.adapter;

import com.mobsandgeeks.adapters.Sectionizer;
import com.ocpsoft.pretty.time.PrettyTime;
import com.rorlig.babylog.dao.BaseDao;

/**
 * Created by rorlig on 8/24/14.
 * @author gaurav gupta
 * diaper change sectionizer
 */
public class DateSectionizer implements Sectionizer<BaseDao> {
    private static final String TODAY = "Today";
    private static final String TWO_DAYS = "Two days ago";
    private static final String WEEK = "This Week";
    private static final String TWO_WEEK = "Two weeks ago";
    private static final String THREE_WEEK = "Three weeks ago";
    private static final String MONTH = "Last Month";
    private static final String TWO_MONTH = "Two Months ago";
    private static final String SIX_MONTH = "Six Months ago";
    private static final String YEAR = "Last Year";

    private final long LENGTH_DAY = 24*60*60*1000;

    private final long LENGTH_MONTH = 24*60*60;
    private String TAG = "DiaperChangeSectionizer";



    /**
     * Returns the title for the given instance from the data source.
     *
     * @param instance The instance obtained from the data source of the decorated list adapter.
     * @return section title for the given instance.
     */
    @Override
    public String getSectionTitleForItem(BaseDao instance) {
        PrettyTime prettyTime = new PrettyTime();

        Long currentTime = System.currentTimeMillis();
//        Log.d(TAG, " currentTime " + currentTime + " diaper change time " + instance.getDate());
        Long diff = currentTime - instance.getDate().getTime();
//        Log.d(TAG, "diff " + diff);
        if (diff< LENGTH_DAY) {
            return TODAY;
        } else if (diff<2*LENGTH_DAY) {
            return TWO_DAYS;
        } else if (diff<LENGTH_DAY*7) {
            return WEEK;
        } else if (diff<LENGTH_DAY*7*2) {
            return TWO_WEEK;
        } else if (diff<LENGTH_DAY*7*3) {
            return THREE_WEEK;
        } else if (diff<LENGTH_DAY*7*4) {
            return MONTH;
        } else if (diff<LENGTH_DAY*7*4*2){
            return TWO_MONTH;
        } else if (diff<LENGTH_DAY*7*4*6) {
            return SIX_MONTH;
        } else {
            return YEAR;
        }    }
}
