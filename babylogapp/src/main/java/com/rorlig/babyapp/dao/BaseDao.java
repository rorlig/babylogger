package com.rorlig.babyapp.dao;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * @author gaurav gupta
 * BaseDao class for the objects stored in the database
 */
public class BaseDao {

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
