package com.rorlig.babylog.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.dao.FeedDao;

import java.sql.SQLException;
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
     * Returns the analytics data access object
     */
    public Dao<DiaperChangeDao, Integer> getDiaperChangeDao() throws SQLException {
        if (diaperChangeDao == null) {
            diaperChangeDao = getHelper().getDiaperChangeDao();
        }
        return diaperChangeDao;
    }


    /*
     * Returns the analytics data access object
     */
    public Dao<FeedDao, Integer> getFeedDao() throws SQLException {
        if (feedDao == null) {
            feedDao = getHelper().getFeedDao();
        }
        return feedDao;
    }




    public List<DiaperChangeDao> getDiaperChangeList() throws SQLException {
        QueryBuilder<DiaperChangeDao, Integer> queryBuilder = getDiaperChangeDao().queryBuilder().orderBy("time", false);
//        queryBuilder.where().eq("isSend", false);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }


    public List<FeedDao> getFeedList() throws SQLException {
        QueryBuilder<FeedDao, Integer> queryBuilder = getFeedDao().queryBuilder().orderBy("time", false);
//        queryBuilder.where().eq("isSend", false);
        Log.d(TAG, " query size " + queryBuilder.query().size());
        return queryBuilder.query();
    }






}
