package com.rorlig.babyapp.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.rorlig.babyapp.dao.DiaperChangeDao;
import com.rorlig.babyapp.dao.FeedDao;
import com.rorlig.babyapp.dao.GrowthDao;
import com.rorlig.babyapp.dao.MilestonesDao;
import com.rorlig.babyapp.dao.SleepDao;

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
    private Dao<SleepDao, Integer> sleepDao;

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


    /**
     * @return
     * @throws SQLException
     */
    /*
     * Returns the feed data access object
     */
    private Dao<FeedDao, Integer> getFeedDao() throws SQLException {
        if (feedDao == null) {
            feedDao = getHelper().getFeedDao();
        }
        return feedDao;
    }


    /*
   * Returns the growth data access object
   */
    private Dao<GrowthDao, Integer> getGrowthDao() throws SQLException {
        if (growthDao == null) {
            growthDao = getHelper().getGrowthDao();
        }
        return growthDao;
    }



    /*
   * Returns the analytics data access object
   */
    private Dao<MilestonesDao, Integer> getMilestonesDao() throws SQLException {
        if (milestonesDao == null) {
            milestonesDao = getHelper().getMilestonesDao();
        }
        return milestonesDao;
    }

    /*
     * Returns the analytics data access object
    */
    public Dao<SleepDao, Integer> getSleepDao() throws SQLException {
        if (sleepDao == null) {
            sleepDao = getHelper().getSleepDao();
        }
        return sleepDao;
    }

    /*
     * Return the diaper changes by month...
     * jan/feb/march/
     */
    public List<String[]> getDiaperChangeByMonthofYear() throws SQLException {

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
                + "group by strftime('%Y-%m', date)";
        Log.d(TAG, "rawSelectSql " + rawSelectSql);
        return getDiaperChangeDao().queryRaw(rawSelectSql).getResults();

    }


    /*
    * Return the diaper changes by Week of the Month
    * @param null
    * @returns List<String[]>: - result array with week and diaper changes by the week number...
    */
    public List<String[]> getDiaperChangeByWeekofMonth() throws SQLException {
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
                + "group by strftime('%W', date)";

        Log.d(TAG, "rawSelectSql " + rawSelectSql);

        return getDiaperChangeDao().queryRaw(rawSelectSql).getResults();
    }

    /*
     * Return the diaper changes for the day of the week
     *
     */
    public List<String[]> getDiaperChangeByDayofWeek() throws SQLException {
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

//        Log.d(TAG, "rawSelectSql " + rawSelectSql);

        return getDiaperChangeDao().queryRaw(rawSelectSql).getResults();
    }

    /*
     * Helper method to get a list of diapers in the selected time range....
     */
    public List<DiaperChangeDao> getDiaperChangeList(Date startTime, Date endTime) throws SQLException {
        Log.d(TAG, " startTime " + startTime + " endTime " + endTime);
        QueryBuilder<DiaperChangeDao, Integer> queryBuilder = getDiaperChangeDao().queryBuilder().orderBy("date", false);
//        queryBuilder.where().eq("isSend", false);

        queryBuilder.where().lt("date", endTime)
                .and().gt("date", startTime);
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


    /*
     * Returns a list of feeds sorted by date - descending.
     * @param none
     * @return List<FeedDao> List of FeedDao @see com.rorlig.babylog.dao.FeedDao
     * @throws SQLException
     */

    public List<FeedDao> getFeedList() throws SQLException {
        QueryBuilder<FeedDao, Integer> queryBuilder = getFeedDao().queryBuilder().orderBy("date", false);
//        queryBuilder.where().eq("isSend", false);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }


    /*
     * Returns a list of feeds sorted by date - descending.
     * @param Date startTime start time for the query
     * @param Date endTime end time for the query
     * @return List<FeedDao> List of FeedDao @see com.rorlig.babylog.dao.FeedDao
     * @throws SQLException
     */

    public List<FeedDao> getFeedList(Date startTime, Date endTime) throws SQLException {
        Log.d(TAG, " startTime " + startTime + " endTime " + endTime);
        QueryBuilder<FeedDao, Integer> queryBuilder = getFeedDao().queryBuilder().orderBy("date", false);
        queryBuilder.where().lt("date", endTime)
                .and().gt("date", startTime);
        return queryBuilder.query();
    }


    /*
    * Returns a list of growth items
    * @return List<GrowthDao> List of GrowthDao @see @see com.rorlig.babylog.dao.GrowthDao
    * @throws SQLException
    */
    public List<GrowthDao> getGrowthList(boolean order) throws SQLException {
        QueryBuilder<GrowthDao, Integer> queryBuilder = getGrowthDao().queryBuilder().orderBy("date", order);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }



    /*
     * Returns a list of growth items
     * @param Date startTime start time for the query
     * @param Date endTime end time for the query
     * @return List<GrowthDao> List of GrowthDao @see @see com.rorlig.babylog.dao.GrowthDao
     * @throws SQLException
     */
    public List<GrowthDao> getGrowthList(Date startTime, Date endTime) throws SQLException {
        Log.d(TAG, " startTime " + startTime + " endTime " + endTime);
        QueryBuilder<GrowthDao, Integer> queryBuilder = getGrowthDao().queryBuilder().orderBy("date", false);
        queryBuilder.where().lt("date", endTime)
                .and().gt("date", startTime);
        return queryBuilder.query();
    }

    /*
   * Returns a list of milestone items
   * @param Date startTime start time for the query
   * @param Date endTime end time for the query
   * @return List<MilestonesDao> List of MilestonesDao @see @see com.rorlig.babylog.dao.GrowthDao
   * @throws SQLException
   */
    public List<MilestonesDao> getMilestoneList(Date startTime, Date endTime) throws SQLException {
        Log.d(TAG, " startTime " + startTime + " endTime " + endTime);
        QueryBuilder<MilestonesDao, Integer> queryBuilder = getMilestonesDao().queryBuilder().orderBy("date", false);
        queryBuilder.where().lt("completionDate", endTime)
                .and().gt("completionDate", startTime);
        return queryBuilder.query();
    }



    public List<SleepDao> getSleepList() throws SQLException {
        QueryBuilder<SleepDao, Integer> queryBuilder = getSleepDao().queryBuilder().orderBy("date", false);
//        queryBuilder.where().eq("isSend", false);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }


    /*
  * Returns a list of sleep items
  * @param Date startTime start time for the query
  * @param Date endTime end time for the query
  * @return List<SleepDao> List of SleepDao @see @see com.rorlig.babylog.dao.SleepDao
  * @throws SQLException
    */
    public List<SleepDao> getSleepList(Date startTime, Date endTime) throws SQLException {
        Log.d(TAG, " startTime " + startTime + " endTime " + endTime);
        QueryBuilder<SleepDao, Integer> queryBuilder = getSleepDao().queryBuilder().orderBy("date", false);
        queryBuilder.where().lt("date", endTime)
                .and().gt("date", startTime);
        return queryBuilder.query();
    }

    /*
   * Return the sleep changes for the day of the week
   *
   */
    public List<String[]> getSleepByDayofWeek() throws SQLException {
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

        String rawSelectSql = "select strftime('%Y-%m-%d', date), sum(duration) from sleepdao where date <'" + endTime
                + "'and date>'" + startTime + "'group by strftime('%Y-%m,-%d', date)";

        Log.d(TAG, "rawSelectSql " + rawSelectSql);

        return getDiaperChangeDao().queryRaw(rawSelectSql).getResults();
    }


    /*
  * Return the sleep changes by Week of the Month
  * @param null
  * @returns List<String[]>: - result array with week and diaper changes by the week number...
  */
    public List<String[]> getSleepByWeekofMonth() throws SQLException {
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

        String rawSelectSql = "select strftime('%W', date), sum(duration) from sleepdao "
                + "group by strftime('%W', date)";

        Log.d(TAG, "rawSelectSql " + rawSelectSql);

        return getDiaperChangeDao().queryRaw(rawSelectSql).getResults();
    }

    /*
  * Return the diaper changes by month...
  * jan/feb/march/
  */
    public List<String[]> getSleepByMonthofYear() throws SQLException {

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
        String rawSelectSql = "select strftime('%Y-%m', date), sum(duration) from sleepdao "
                + "group by strftime('%Y-%m', date)";
        Log.d(TAG, "rawSelectSql " + rawSelectSql);
        return getDiaperChangeDao().queryRaw(rawSelectSql).getResults();

    }

}
