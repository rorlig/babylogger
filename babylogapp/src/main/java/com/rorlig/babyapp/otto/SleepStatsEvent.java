package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.ui.fragment.sleep.SleepStatsFragment;

import java.util.List;

/**
 * @author gaurav gupta
 */
public class SleepStatsEvent {


    private final List<String[]> list;
    private final SleepStatsFragment.SleepStatsType sleepStatsType;

    public SleepStatsEvent(List<String[]> list, SleepStatsFragment.SleepStatsType sleepStatsType) {
        this.list = list;
        this.sleepStatsType = sleepStatsType;
    }

    public List<String[]> getList() {
        return list;
    }

    public SleepStatsFragment.SleepStatsType getSleepStatsType() {
        return sleepStatsType;
    }
}
