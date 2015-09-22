package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.Milestones;

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
