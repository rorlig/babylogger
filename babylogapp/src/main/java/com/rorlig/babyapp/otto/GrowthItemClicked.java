package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.Growth;

/**
 * @author gaurav gupta
 */
public class GrowthItemClicked {

    private final Growth growthDao;

    private final int position;
    public GrowthItemClicked(Growth growthDao, int position) {
        this.growthDao = growthDao;
        this.position = position;
    }

    public Growth getGrowthDao() {
        return growthDao;
    }

    public int getPosition() {
        return position;
    }
}
