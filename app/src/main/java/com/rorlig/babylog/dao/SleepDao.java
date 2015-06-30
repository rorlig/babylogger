package com.rorlig.babylog.dao;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rorlig.babylog.model.feed.FeedType;

import java.util.Date;

/**
 * @author gaurav gupta
 * Feed Dao
 */
@DatabaseTable
public class SleepDao extends BaseDao {


    @DatabaseField(generatedId = true)
    int id;



    //left breast date
    @DatabaseField
    Date sleepStartTime;

    //right breast date
    @DatabaseField
    Date sleepEndTime;

    //notes about the feed
    @DatabaseField
    String notes;


    public SleepDao() {
    }

    public SleepDao(Date sleepStartTime, Date sleepEndTime, String notes) {
        this.sleepStartTime = sleepStartTime;
        this.sleepEndTime = sleepEndTime;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "SleepDao{" +
                "id=" + id +
                ", sleepStartTime=" + sleepStartTime +
                ", sleepEndTime=" + sleepEndTime +
                ", notes='" + notes + '\'' +
                "} " + super.toString();
    }

    public Date getSleepStartTime() {
        return sleepStartTime;
    }

    public Date getSleepEndTime() {
        return sleepEndTime;
    }

    public String getNotes() {
        return notes;
    }
}
