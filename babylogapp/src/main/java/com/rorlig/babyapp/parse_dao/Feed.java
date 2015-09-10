package com.rorlig.babyapp.parse_dao;


import com.parse.ParseClassName;
import com.rorlig.babyapp.model.feed.FeedType;

import java.util.Date;

/**
 * @author gaurav gupta
 * Feed Dao
 */
@ParseClassName("Feed")
public class Feed extends BabyLogBaseParseObject {

    //Feed type - bottle or breast
    FeedType feedType;

    //Feed Item - what is the user being feed...
    String feedItem;

    //Quantity being feed
    Double quantity;

    //Time for left breast feed
    Long leftBreastTime;

    //Time for right breast feed

    Long rightBreastTime;

    //Notes for left breast feed

    String notes;

//    //time at which the log was changed ...
//    Date logCreationDate;

    public Feed() {
    }

    public Feed(FeedType feedType,
                String feedItem,
                Double quantity,
                Long leftBreastTime,
                Long rightBreastTime,
                String notes,
                Date date) {
        setFeedItem(feedItem);
        setFeedType(feedType);
        setNotes(notes);
        setQuantity(quantity);
        setLeftBreastTime(leftBreastTime);
        setRightBreastTime(rightBreastTime);
        setLogCreationDate(date);
    }


    public String getFeedType() {
        return getString("feedType");
    }

    public void setFeedType(FeedType feedType) {
        put("feedType", feedType.toString());
    }

    public String getFeedItem() {
        return getString("feedItem");
    }

    public void setFeedItem(String feedItem) {
        put("feedItem", feedItem);
    }

    public Double getQuantity() {
       return getDouble("quantity");
    }

    public void setQuantity(Double quantity) {
        put("quantity", quantity);
    }

    public Long getLeftBreastTime() {
        return getLong("leftBreastTime");
    }

    public void setLeftBreastTime(Long leftBreastTime) {
        put("leftBreastTime", leftBreastTime);
    }

    public Long getRightBreastTime() {
        return getLong("rightBreastTime");
    }

    public void setRightBreastTime(Long rightBreastTime) {
        put("rightBreastTime", rightBreastTime);
    }

    public String getNotes() {
        return getString("notes");
    }

    public void setNotes(String notes) {
        put("notes", notes);
    }

    @Override
    public String toString() {
        return "Feed{" +
                "feedType=" + getFeedType() +
                ", feedItem='" + getFeedItem() + '\'' +
                ", quantity=" + getQuantity() +
                ", leftBreastTime=" + getLeftBreastTime() +
                ", rightBreastTime=" + getRightBreastTime() +
                ", notes='" + notes + '\'' +
                "} " + super.toString();
    }

    //    public Date getLogCreationDate() {
//        return getDate("logCreationDate");
//    }
//
//    public void setLogCreationDate(Date logCreationDate) {
//        put("logCreationDate", logCreationDate);
//    }
}
