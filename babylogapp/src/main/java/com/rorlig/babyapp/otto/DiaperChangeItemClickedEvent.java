package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.DiaperChange;

/**
 * @author gaurav gupta
 */
public class DiaperChangeItemClickedEvent {

    private final DiaperChange diaperChange;
    private final int position;

    public DiaperChangeItemClickedEvent(DiaperChange diaperChange) {
        this.diaperChange = diaperChange;
        this.position = -1;
    }

    public DiaperChangeItemClickedEvent(DiaperChange diaperChange, int position) {
        this.diaperChange = diaperChange;
        this.position = position;
    }

    public DiaperChange getDiaperChange() {
        return diaperChange;
    }

    public int getPosition() {
        return position;
    }
}
