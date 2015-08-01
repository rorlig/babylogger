package com.rorlig.babylog.dao;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rorlig.babylog.model.feed.FeedType;

import java.util.Date;

/**
 * @author gaurav gupta
 * Feed Dao
 */
@DatabaseTable
public class FeedDao extends BaseDao {


    @DatabaseField(generatedId = true)
    int id;

    //feed type - breast or bottle
    @DatabaseField
    FeedType feedType;

    //feed item
    @DatabaseField
    String feedItem;

    //quantity
    @DatabaseField
    Double quantity;

    //left breast date
    @DatabaseField
    Long leftBreastTime;

    //right breast date
    @DatabaseField
    Long rightBreastTime;

    //notes about the feed
    @DatabaseField
    String notes;


    public FeedDao() {
    }

    public FeedDao(FeedType feedType, String feedItem, Double quantity, Long leftBreastTime, Long rightBreastTime, String notes, Date time) {
        this.feedType = feedType;
        this.feedItem = feedItem;
        this.quantity = quantity;
        this.leftBreastTime = leftBreastTime;
        this.rightBreastTime = rightBreastTime;
        this.date = new Date(time.getTime());
        this.notes = notes;
    }


    @Override
    public String toString() {
        return "FeedDao{" +
                "id=" + id +
                ", feedType=" + feedType +
                ", feedItem='" + feedItem + '\'' +
                ", quantity=" + quantity +
                ", leftBreastTime=" + leftBreastTime +
                ", rightBreastTime=" + rightBreastTime +
                ", notes='" + notes + '\'' +
                '}';
    }

    public FeedType getFeedType() {
        return feedType;
    }

    public String getFeedItem() {
        return feedItem;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Long getLeftBreastTime() {
        return leftBreastTime;
    }

    public Long getRightBreastTime() {
        return rightBreastTime;
    }

    public String getNotes() {
        return notes;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }
}
