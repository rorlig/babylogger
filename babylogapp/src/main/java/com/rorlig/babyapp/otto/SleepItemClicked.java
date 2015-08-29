package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.Sleep;

/**
 * @author gaurav gupta
 */
public class SleepItemClicked {
    private final Sleep sleep;

    public SleepItemClicked(Sleep sleep) {
        this.sleep = sleep;
    }

    public Sleep getSleepDao() {
        return sleep;
    }
}
