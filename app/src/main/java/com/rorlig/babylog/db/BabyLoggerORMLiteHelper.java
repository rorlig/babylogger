package com.rorlig.babylog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rorlig.babylog.dao.DiaperChangeDao;
import com.rorlig.babylog.dao.FeedDao;
import com.rorlig.babylog.dao.GrowthDao;

import java.sql.SQLException;

/**
 * @author gaurav gupta
 * ORMLite Helper class...
 */
public class BabyLoggerORMLiteHelper extends OrmLiteSqliteOpenHelper {


    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "gifting.com.rorlig.babylog.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 3;
    private Dao<DiaperChangeDao, Integer> diaperChangeDao;
    private Dao<FeedDao, Integer> feedDao;
    private Dao<GrowthDao, Integer> growthDao;

    //    // the DAO object we use to access the SimpleData table
//    private Dao<AnalyticsModel, Integer> analyticsDao = null;
//    private Dao<NotesModel, Integer> notesDao = null;
//    private Dao<MessageDetailsModel, Integer> messageDetailsDao = null;
    public BabyLoggerORMLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(BabyLoggerORMLiteHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, DiaperChangeDao.class);
            TableUtils.createTable(connectionSource, FeedDao.class);
            TableUtils.createTable(connectionSource, GrowthDao.class);

//
//            TableUtils.createTable(connectionSource, NotesModel.class);
//            TableUtils.createTable(connectionSource, MessageDetailsModel.class);
//
//            // here we try inserting data in the on-create as a test
//            Dao<AnalyticsModel, Integer> dao = getAnalyticsDataDao();
//            long millis = System.currentTimeMillis();
//            // create some entries in the onCreate
//            AnalyticsModel data = new AnalyticsModel();
//            Log.i(GiftingORMLiteHelper.class.getName(), "created new entries in onCreate: " + millis);

        } catch (SQLException e) {
            Log.e(BabyLoggerORMLiteHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(database, connectionSource);
    }


    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<DiaperChangeDao, Integer> getDiaperChangeDao() throws SQLException {
        if (diaperChangeDao == null) {
            diaperChangeDao = getDao(DiaperChangeDao.class);
        }
        return diaperChangeDao;
    }


    public Dao<FeedDao, Integer> getFeedDao() throws SQLException {
        if (feedDao == null) {
            feedDao = getDao(FeedDao.class);
        }
        return feedDao;
    }

    public Dao<GrowthDao, Integer> getGrowthDao() throws SQLException {
        if (growthDao==null) {
            growthDao = getDao(GrowthDao.class);
        }
        return growthDao;
    }
    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        diaperChangeDao = null;

        feedDao = null;
    }


}
