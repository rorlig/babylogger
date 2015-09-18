package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.Sleep;

/**
 * @author gaurav gupta
 */
public class SleepItemClicked {
    private final Sleep sleep;
    private final int position;

    public SleepItemClicked(Sleep sleep, int position) {
        this.sleep = sleep;
        this.position = position;
    }

    public Sleep getSleepDao() {
        return sleep;
    }


    public int getPosition() {
        return position;
    }
}
