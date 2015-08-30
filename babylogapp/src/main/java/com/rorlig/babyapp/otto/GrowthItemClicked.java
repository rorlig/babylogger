package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.dao.GrowthDao;
import com.rorlig.babyapp.parse_dao.Growth;

/**
 * @author gaurav gupta
 */
public class GrowthItemClicked {

    private final Growth growthDao;

    public GrowthItemClicked(Growth growthDao) {
        this.growthDao = growthDao;
    }

    public Growth getGrowthDao() {
        return growthDao;
    }
}
