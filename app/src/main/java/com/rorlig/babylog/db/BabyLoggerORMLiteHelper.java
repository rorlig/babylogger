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
import com.rorlig.babylog.dao.MilestonesDao;
import com.rorlig.babylog.dao.SleepDao;
import com.rorlig.babylog.model.diaper.DiaperChangeEnum;
import com.rorlig.babylog.model.diaper.DiaperIncident;
import com.rorlig.babylog.utils.FileUtils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.joda.time.DateTime;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author gaurav gupta
 * ORMLite Helper class...
 */
public class BabyLoggerORMLiteHelper extends OrmLiteSqliteOpenHelper {


    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "com.rorlig.babylog.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 3;
    private final Context context;
    private Dao<DiaperChangeDao, Integer> diaperChangeDao;
    private Dao<FeedDao, Integer> feedDao;
    private Dao<GrowthDao, Integer> growthDao;
    private Dao<MilestonesDao, Integer> milestonesDao;
    private Dao<SleepDao, Integer> sleepDao;

    private final String TAG = "BabyLoggerORMLiteHelper";


    //    // the DAO object we use to access the SimpleData table
//    private Dao<AnalyticsModel, Integer> analyticsDao = null;
//    private Dao<NotesModel, Integer> notesDao = null;
//    private Dao<MessageDetailsModel, Integer> messageDetailsDao = null;
    public BabyLoggerORMLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
            TableUtils.createTable(connectionSource, MilestonesDao.class);
            TableUtils.createTable(connectionSource, SleepDao.class);
//            populateDiaperDao();
//            populateFeedDao();
//            populateGrowthDao();
//            populateMilestoneDao();



        } catch (SQLException e) {
            Log.e(BabyLoggerORMLiteHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    private void populateGrowthDao() {

        Log.d(TAG, "populateGrowthDao");
        ArrayList<GrowthDao> growthDaoArrayList;
        final String growthResponse = new FileUtils().loadJSONFromAsset("growth.json", context);
        if (growthResponse!=null){
            Log.d(TAG , growthResponse);
            ObjectMapper objectMapper = new ObjectMapper();
            try {

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                objectMapper.setDateFormat(df);
                JavaType type = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, GrowthDao.class);
                growthDaoArrayList = objectMapper.readValue(growthResponse, type);
                Log.d(TAG, "growthDaoArrayList(): " + growthDaoArrayList.size());
                for (GrowthDao growthDao: growthDaoArrayList) {
                    getGrowthDao().create(growthDao);
                }
            } catch (IOException exception){
                Log.d(TAG, "exception " + exception);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateFeedDao() {

    }

    private void populateDiaperDao() {

        try {
            for (int i = 0; i < 1000; i++) {
                getDiaperChangeDao().create(new DiaperChangeDao(DiaperChangeEnum.WET, null, null, DiaperIncident.NONE, "", new Date()));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private void populateMilestoneDao() {
////        MilestonesDao milestonesDao = new MilestonesDao("Week 0-4", false, "Responds to Voice", -1L, -1L);
////        DateTime today = new DateTime().withTi();
//
//        try {
//            getMilestonesDao().create(new MilestonesDao("Month 0-2", false, "Responds to Voice", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 0-2", false, "Smile to Mom", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 0-2", false, "Lifts Head", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 2-5", false, "Follows objects with eyes", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 2-5", false, "Reaches for things", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 2-5", false, "Starts Babbling", null,new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 5-8", false, "Searches for objects", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 5-8", false, "Sits without support", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 5-8", false, "Responds to name", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 8-12", false, "Stands up with support", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 8-12", false, "Stands up without support", null, new Date(0)));
//            getMilestonesDao().create(new MilestonesDao("Month 12-15", false, "Walks", null, new Date(0)));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

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

    public Dao<SleepDao, Integer> getSleepDao() throws SQLException {
        if (sleepDao==null) {
            sleepDao = getDao(SleepDao.class);
        }
        return sleepDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        diaperChangeDao = null;
        feedDao = null;
        growthDao = null;
        milestonesDao = null;
        sleepDao = null;
    }


    public Dao<MilestonesDao, Integer> getMilestonesDao() throws SQLException {
        if (milestonesDao==null) {
            milestonesDao = getDao(MilestonesDao.class);
        }
        return milestonesDao;
    }


}
