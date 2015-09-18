package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.Milestones;
import com.rorlig.babyapp.parse_dao.Sleep;

/**
 * @author gaurav gupta
 */
public class MilestoneItemClicked {
    private final Milestones model;
    private final int position;

    public MilestoneItemClicked(Milestones milestonesDao, int position) {
        this.model = milestonesDao;
        this.position = position;
    }

    public Milestones getModel() {
        return model;
    }

    public int getPosition() {
        return position;
    }
}
