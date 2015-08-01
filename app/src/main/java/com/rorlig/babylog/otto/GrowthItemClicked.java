package com.rorlig.babylog.otto;

import com.rorlig.babylog.dao.GrowthDao;

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
