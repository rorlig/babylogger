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
}
