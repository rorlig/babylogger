package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.dao.GrowthDao;

/**
 * @author gaurav gupta
 */
public class GrowthItemClicked {

    private final GrowthDao growthDao;

    public GrowthItemClicked(GrowthDao growthDao) {
        this.growthDao = growthDao;
    }

    public GrowthDao getGrowthDao() {
        return growthDao;
    }
}
