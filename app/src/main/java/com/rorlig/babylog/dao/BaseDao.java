package com.rorlig.babylog.dao;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * @author gaurav gupta
 * BaseDao class.
 */
public class BaseDao {

//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm a z")
    @DatabaseField
    Date date;

    public Date getDate() {
        return new Date(date.getTime());
    }

    @Override
    public String toString() {
        return "BaseDao{" +
                "date=" + date +
                '}';
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }
}
