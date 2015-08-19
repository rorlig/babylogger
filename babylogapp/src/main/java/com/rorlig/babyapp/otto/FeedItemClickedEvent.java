package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.dao.FeedDao;

/**
 * @author gaurav gupta
 */
public class FeedItemClickedEvent {

    private final FeedDao feedDao;

    public FeedItemClickedEvent(FeedDao feedDao) {
       this.feedDao = feedDao;
    }

    public FeedDao getFeedDao() {
        return feedDao;
    }
}
