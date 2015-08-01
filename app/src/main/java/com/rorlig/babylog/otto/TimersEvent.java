package com.rorlig.babylog.otto;

import com.rorlig.babylog.service.StopWatchService;

/**
 * @author gaurav gupta
 */
public class TimersEvent {
    private final StopWatchService.Timers tag;
    private final int value;

    public TimersEvent(StopWatchService.Timers tag, int value) {
        this.tag = tag;
        this.value = value;
    }

    public StopWatchService.Timers getTag() {
        return tag;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TimersEvent{" +
                "tag=" + tag +
                ", value=" + value +
                '}';
    }
}
