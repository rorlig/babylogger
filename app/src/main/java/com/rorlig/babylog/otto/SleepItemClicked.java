package com.rorlig.babylog.otto;

import com.rorlig.babylog.dao.SleepDao;

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
