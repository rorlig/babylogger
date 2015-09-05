package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.Feed;

/**
 * @author gaurav gupta
 */
public class FeedItemClickedEvent {

    private final Feed feedDao;

    public FeedItemClickedEvent(Feed feedDao) {
       this.feedDao = feedDao;
    }

    public Feed getFeedDao() {
        return feedDao;
    }
}
