package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.Milestones;

/**
 * @author gaurav gupta
 */
public class MilestoneItemClicked {
    private final Milestones model;

    public MilestoneItemClicked(Milestones milestonesDao) {
        this.model = milestonesDao;
    }

    public Milestones getModel() {
        return model;
    }
}
