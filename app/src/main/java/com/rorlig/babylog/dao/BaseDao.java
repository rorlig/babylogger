package com.rorlig.babylog.dao;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by gaurav
 */
public class BaseDao {

    @DatabaseField
    Long time;

    public Long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "BaseDao{" +
                "time=" + time +
                '}';
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
