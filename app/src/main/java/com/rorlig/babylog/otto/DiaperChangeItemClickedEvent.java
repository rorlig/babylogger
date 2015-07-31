package com.rorlig.babylog.otto;

import com.rorlig.babylog.dao.DiaperChangeDao;

/**
 * @author gaurav gupta
 */
public class DiaperChangeItemClickedEvent {

    private final DiaperChangeDao diaperChangeDao;

    public DiaperChangeItemClickedEvent(DiaperChangeDao diaperChangeDao) {
        this.diaperChangeDao = diaperChangeDao;
    }

    public DiaperChangeDao getDiaperChangeDao() {
        return diaperChangeDao;
    }
}
