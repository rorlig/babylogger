package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.dao.DiaperChangeDao;

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
