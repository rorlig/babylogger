package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.dao.SleepDao;

/**
 * @author gaurav gupta
 */
public class SleepItemClicked {
    private final SleepDao sleepDao;

    public SleepItemClicked(SleepDao sleepDao) {
        this.sleepDao = sleepDao;
    }

    public SleepDao getSleepDao() {
        return sleepDao;
    }
}
