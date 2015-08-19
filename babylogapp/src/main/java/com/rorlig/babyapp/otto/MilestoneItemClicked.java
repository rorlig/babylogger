package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.dao.MilestonesDao;

/**
 * @author gaurav gupta
 */
public class MilestoneItemClicked {
    private final MilestonesDao model;

    public MilestoneItemClicked(MilestonesDao milestonesDao) {
        this.model = milestonesDao;
    }

    public MilestonesDao getModel() {
        return model;
    }
}
