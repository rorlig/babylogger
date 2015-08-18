package com.rorlig.babyapp.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author gaurav gupta
 * Growth Dao
 */
@DatabaseTable
public class GrowthDao extends BaseDao {



    @DatabaseField(generatedId = true)
    int id;

    //weight of the baby
    @DatabaseField
    Double weight;

    //head measurement
    @DatabaseField
    Double headMeasurement;


    //height measurement
    @DatabaseField
    Double height;


    //notes
    @DatabaseField
    String notes;

    public GrowthDao() {
    }

    public GrowthDao(Double weight,  Double height, Double headMeasurement,String notes, Date time) {
        this.weight = weight;
        this.height = height;
        this.headMeasurement = headMeasurement;
        this.date = new Date(time.getTime());
        this.notes = notes;
    }


    @Override
    public String toString() {
        return "GrowthDao{" +
                "id=" + id +
                ", weight=" + weight +
                ", headMeasurement=" + headMeasurement +
                ", height=" + height +
                ", notes='" + notes + '\'' + super.toString() +
                '}';
    }

    public Double getWeight() {
        return weight;
    }

    public Double getHeadMeasurement() {
        return headMeasurement;
    }

    public Double getHeight() {
        return height;
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
