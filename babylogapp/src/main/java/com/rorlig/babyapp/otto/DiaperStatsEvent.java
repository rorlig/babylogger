package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.ui.fragment.diaper.DiaperChangeStatsType;

import java.util.List;

/**
 * @author gaurav gupta
 */
public class DiaperStatsEvent {
    private final List<String[]> list;
    private final DiaperChangeStatsType diaperChangeStatsType;

    public DiaperStatsEvent(List<String[]> list, DiaperChangeStatsType diaperChangeStatsType) {

        this.list = list;
        this.diaperChangeStatsType = diaperChangeStatsType;
    }

    public List<String[]> getList() {
        return list;
    }

    public DiaperChangeStatsType getDiaperChangeStatsType() {
        return diaperChangeStatsType;
    }
}
