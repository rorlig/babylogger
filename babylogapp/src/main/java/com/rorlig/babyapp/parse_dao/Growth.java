package com.rorlig.babyapp.parse_dao;

import com.parse.ParseClassName;

import java.util.Date;

/**
 * @author gaurav gupta
 * Growth Dao
 */
@ParseClassName("Growth")
public class Growth extends BabyLogBaseParseObject {

    //weight of the baby
    Double weight;

    //head measurement
    Double headMeasurement;

    //height measurement
    Double height;

    //notes
    String notes;

//    //time at which the log was changed ...
//    Date logCreationDate;


    public Growth() {
    }

    public Growth(Double weight, Double height, Double headMeasurement, String notes, Date date) {
//        this.weight = weight;
        setWeight(weight);
//        this.height = height;
        setHeight(height);
//        this.headMeasurement = headMeasurement;
        setHeadMeasurement(headMeasurement);
//        this.logCreationDate = new Date(date.getTime());
        setLogCreationDate(date);
//        this.notes = notes;
        setNotes(notes);
    }


    @Override
    public String toString() {
        return "GrowthDao{" +
                "weight=" + getWeight() +
                ", headMeasurement=" + getHeadMeasurement() +
                ", height=" + getHeight() +
                ", notes='" + getNotes() + '\'' +
                ", logCreationDate=" + getLogCreationDate() +
                "} " + super.toString();
    }

    public Double getWeight() {
        return getDouble("weight");
    }


    public void setWeight(Double weight) {
        put("weight", weight);
    }

    public Double getHeadMeasurement() {
        return getDouble("headMeasurement");
    }

    public void setHeadMeasurement(Double headMeasurement) {
        put("headMeasurement", headMeasurement);
    }

    public Double getHeight() {
        return getDouble("height");
    }

    public void setHeight(Double height) {
        put("height", height);
    }

    public String getNotes() {
      return getString("notes");
    }

    public void setNotes(String notes){
        put("notes", notes);
    }

//
//    public Date getLogCreationDate() {
//        return getDate("logCreationDate");
//    }
//
//    public void setLogCreationDate(Date logCreationDate) {
//        put("logCreationDate", logCreationDate);
//    }
}
