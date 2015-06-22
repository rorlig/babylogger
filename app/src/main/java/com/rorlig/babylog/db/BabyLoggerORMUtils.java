package com.rorlig.babylog.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.dao.GrowthDao;
import com.rorlig.babylog.dao.MilestonesDao;

import java.sql.SQLException;
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
    public List<DiaperChangeDao> getDiaperChangeByMonth() {
        return null;
    }

    /*
    * Return the diaper changes by month...
    * week 1/2/3/4
    */
    public List<DiaperChangeDao> getDiaperChangeByWeek() {
        return null;
    }

    /*
     * Return the diaper changes by day
     * sun/mon/....
     */
    public List<DiaperChangeDao> getDiaperChangeByDay() {
        return null;
    }

    /*
     * Helper method to get a list of diapers in the selected time range....
     */
    public List<DiaperChangeDao> getDiaperChangeList(Long startTime, Long endTime) throws SQLException {
        QueryBuilder<DiaperChangeDao, Integer> queryBuilder = getDiaperChangeDao().queryBuilder().orderBy("time", false);
//        queryBuilder.where().eq("isSend", false);

        queryBuilder.where().lt("callStartTime", endTime)
                .and().gt("callStartTime", startTime);
//        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }


    public List<DiaperChangeDao> getDiaperChangeList() throws SQLException {
        QueryBuilder<DiaperChangeDao, Integer> queryBuilder = getDiaperChangeDao().queryBuilder().orderBy("time", false);
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
        QueryBuilder<FeedDao, Integer> queryBuilder = getFeedDao().queryBuilder().orderBy("time", false);
//        queryBuilder.where().eq("isSend", false);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }

    public List<GrowthDao> getGrowthList() throws SQLException {
        QueryBuilder<GrowthDao, Integer> queryBuilder = getGrowthDao().queryBuilder().orderBy("time", false);
//        queryBuilder.where().eq("isSend", false);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }






}
