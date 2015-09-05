package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.DiaperChange;

/**
 * @author gaurav gupta
 */
public class DiaperChangeItemClickedEvent {

    private final DiaperChange diaperChange;

    public DiaperChangeItemClickedEvent(DiaperChange diaperChange) {
        this.diaperChange = diaperChange;
    }

    public DiaperChange getDiaperChange() {
        return diaperChange;
    }
}
