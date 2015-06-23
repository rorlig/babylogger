package com.rorlig.babylog.otto.events.stats;

/**
 * @author gaurav gupta
 */
public class StatsItemEvent {

    StatsItemTypes statsItemType;

    public StatsItemEvent(StatsItemTypes statsItemType) {
        this.statsItemType = statsItemType;
    }

    public StatsItemTypes getStatsItemType() {
        return statsItemType;
    }
}
