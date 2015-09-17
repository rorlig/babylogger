package com.rorlig.babyapp.otto;

import com.rorlig.babyapp.parse_dao.Feed;

/**
 * @author gaurav gupta
 */
public class FeedItemClickedEvent {

    private final Feed feedDao;
    private final int position;

    public FeedItemClickedEvent(Feed feedDao,
                                int position) {
       this.feedDao = feedDao;
       this.position = position;
    }

    public Feed getFeedDao() {
        return feedDao;
    }

    public int getPosition() {
        return position;
    }
}
