package com.rorlig.babylog.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rorlig.babylog.model.diaper.DiaperChangeColorType;
import com.rorlig.babylog.model.diaper.DiaperChangeEnum;
import com.rorlig.babylog.model.diaper.DiaperChangeTextureType;
import com.rorlig.babylog.model.diaper.DiaperIncident;
import com.rorlig.babylog.model.feed.FeedItem;
import com.rorlig.babylog.model.feed.FeedType;

/**
 * table of feeds both bottle and breast feeds
 */
@DatabaseTable
public class FeedDao extends BaseDao {


    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    FeedType feedType;

    @DatabaseField
    String feedItem;

    @DatabaseField
    Double quantity;

    @DatabaseField
    Long leftBreastTime;

    @DatabaseField
    Long rightBreastTime;



    public FeedDao() {
    }

    public FeedDao(FeedType feedType, String feedItem, Double quantity, Long leftBreastTime, Long rightBreastTime, Long time) {
        this.feedType = feedType;
        this.feedItem = feedItem;
        this.quantity = quantity;
        this.leftBreastTime = leftBreastTime;
        this.rightBreastTime = rightBreastTime;
        this.time = time;
    }




    @Override
    public String toString() {
        return "FeedDao{" +
                "id=" + id +
                ", feedType=" + feedType +
                ", feedItem=" + feedItem +
                ", quantity=" + quantity +
                ", leftBreastTime=" + leftBreastTime +
                ", rightBreastTime=" + rightBreastTime +
                ", time=" + time +
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



}
