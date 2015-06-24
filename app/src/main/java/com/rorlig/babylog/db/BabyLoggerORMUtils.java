package com.rorlig.babylog.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.dao.GrowthDao;
import com.rorlig.babylog.dao.MilestonesDao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author gaurav gupta
 */
public class BabyLoggerORMUtils {

    private BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;
    private String TAG = "BabyLoggerORMUtils";



    private Context context;
    private Dao<DiaperChangeDao, Integer> diaperChangeDao;
    private Dao<FeedDao, Integer> feedDao;
    private Dao<GrowthDao, Integer> growthDao;
    private Dao<MilestonesDao, Integer> milestonesDao;

    public BabyLoggerORMUtils(Context context) {
        Log.d(TAG, "context is " + context);
        this.context = context;
    }

    /*
     * Returns the ORMLiteHelper object
    */
    private BabyLoggerORMLiteHelper getHelper() {
        if (babyLoggerORMLiteHelper == null) {
            babyLoggerORMLiteHelper = OpenHelperManager.getHelper(context, BabyLoggerORMLiteHelper.class);
        }
        return babyLoggerORMLiteHelper;
    }



    /*
     * Returns the diaper data access object
     */
    public Dao<DiaperChangeDao, Integer> getDiaperChangeDao() throws SQLException {
        if (diaperChangeDao == null) {
            diaperChangeDao = getHelper().getDiaperChangeDao();
        }
        return diaperChangeDao;
    }


    /*
     * Returns the feed data access object
     */
    public Dao<FeedDao, Integer> getFeedDao() throws SQLException {
        if (feedDao == null) {
            feedDao = getHelper().getFeedDao();
        }
        return feedDao;
    }


    /*
   * Returns the growth data access object
   */
    public Dao<GrowthDao, Integer> getGrowthDao() throws SQLException {
        if (growthDao == null) {
            growthDao = getHelper().getGrowthDao();
        }
        return growthDao;
    }



    /*
   * Returns the analytics data access object
   */
    public Dao<MilestonesDao, Integer> getMilestonesDao() throws SQLException {
        if (milestonesDao == null) {
            milestonesDao = getHelper().getMilestonesDao();
        }
        return milestonesDao;
    }

    /*
     * Return the diaper changes by month...
     * jan/feb/march/
     */
    public List<String[]> getDiaperChangeByMonth() throws SQLException {

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        c.add(Calendar.DATE, 2);
        String endTime = sdf.format(c.getTime());


        c.add(Calendar.DATE, -(7*52+1));

        String startTime = sdf.format(c.getTime());

        Log.d(TAG, " startTime " + startTime + " endTime " + endTime);

        String rawSelectSql = "select strftime('%Y-%m', date), count(*) from diaperchangedao "
//                + " where date <'" + endTime
//                + "'and date>'" + startTime
                + "group by strftime('%Y-%m', date)";

        Log.d(TAG, "rawSelectSql " + rawSelectSql);

        final List<String[]> result =  getDiaperChangeDao().queryRaw(rawSelectSql).getResults();
        return result;

    }


    /*
    * Return the diaper changes by Week of the Month
    * @param null
    * @returns List<String[]>: - result array with week and diaper changes by the week number...
    */
    public List<String[]> getDiaperChangeByWeek() throws SQLException {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        c.add(Calendar.DATE, 2);
        String endTime = sdf.format(c.getTime());


        c.add(Calendar.DATE, -(7*4+1));

        String startTime = sdf.format(c.getTime());

        Log.d(TAG, " startTime " + startTime + " endTime " + endTime);

        String rawSelectSql = "select strftime('%W', date), count(*) from diaperchangedao "
//                + " where date <'" + endTime
//                + "'and date>'" + startTime
                + "group by strftime('%W', date)";

        Log.d(TAG, "rawSelectSql " + rawSelectSql);

        final List<String[]> result =  getDiaperChangeDao().queryRaw(rawSelectSql).getResults();
        return result;
    }

    /*
     * Return the diaper changes for the day of the week
     *
     */
    public List<String[]> getDiaperChangeByDay() throws SQLException {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        c.add(Calendar.DATE, 2);
        String endTime = sdf.format(c.getTime());


        c.add(Calendar.DATE, -8);

        String startTime = sdf.format(c.getTime());

        Log.d(TAG, " startTime " + startTime + " endTime " + endTime);

        String rawSelectSql = "select strftime('%Y-%m-%d', date), count(*) from diaperchangedao where date <'" + endTime
                + "'and date>'" + startTime + "'group by strftime('%Y-%m,-%d', date)";

        Log.d(TAG, "rawSelectSql " + rawSelectSql);

        final List<String[]> result =  getDiaperChangeDao().queryRaw(rawSelectSql).getResults();


////        Log.d(TAG, " result 0 " + );
//        for (String[] strArr: result)
//            for (String str: strArr)
//                Log.d(TAG, "str " + str);
//        Log.d(TAG , "result " + result);

        return result;
//        QueryBuilder<DiaperChangeDao, Integer> queryBuilder = getDiaperChangeDao().queryBuilder().orderBy("date", false);
//        queryBuilder.where().lt("date", endTime)
//                .and().gt("date", startTime);
//        return queryBuilder.query();
    }

    /*
     * Helper method to get a list of diapers in the selected time range....
     */
    public List<DiaperChangeDao> getDiaperChangeList(Long startTime, Long endTime) throws SQLException {
        QueryBuilder<DiaperChangeDao, Integer> queryBuilder = getDiaperChangeDao().queryBuilder().orderBy("date", false);
//        queryBuilder.where().eq("isSend", false);

        queryBuilder.where().lt("callStartTime", endTime)
                .and().gt("callStartTime", startTime);
//        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }


    public List<DiaperChangeDao> getDiaperChangeList() throws SQLException {
        QueryBuilder<DiaperChangeDao, Integer> queryBuilder = getDiaperChangeDao().queryBuilder().orderBy("date", false);
//        queryBuilder.where().eq("isSend", false);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }


    public List<MilestonesDao> getMilestoneList() throws SQLException {
        QueryBuilder<MilestonesDao, Integer> queryBuilder = getMilestonesDao().queryBuilder();
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }


    public List<FeedDao> getFeedList() throws SQLException {
        QueryBuilder<FeedDao, Integer> queryBuilder = getFeedDao().queryBuilder().orderBy("date", false);
//        queryBuilder.where().eq("isSend", false);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }

    public List<GrowthDao> getGrowthList(boolean order) throws SQLException {
        QueryBuilder<GrowthDao, Integer> queryBuilder = getGrowthDao().queryBuilder().orderBy("date", order);
//        queryBuilder.where().eq("isSend", false);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }






}
