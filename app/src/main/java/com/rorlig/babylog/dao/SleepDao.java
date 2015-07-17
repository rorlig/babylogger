package com.rorlig.babylog.dao;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author gaurav gupta
 * Sleep Dao
 */
@DatabaseTable
public class SleepDao extends BaseDao {


    @DatabaseField(generatedId = true)
    int id;



    @DatabaseField
    Date sleepStartTime;

    //right breast date
    @DatabaseField
    Long duration;



    public SleepDao() {
    }

    public SleepDao(Date sleepStartTime,
                    Long duration, Date date) {
        this.sleepStartTime = sleepStartTime;
        this.duration = duration;
        this.date = date;
    }

    @Override
    public String toString() {
        return "SleepDao{" +
                "id=" + id +
                ", sleepStartTime=" + sleepStartTime +
                ", duration=" + duration +
                "} " + super.toString();
    }

    public Date getSleepStartTime() {
        return sleepStartTime;
    }

    public Long getDuration() {
        return duration;
    }
}
