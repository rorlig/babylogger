package com.rorlig.babylog.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * table of growth measurements purchased..
 */
@DatabaseTable
public class GrowthDao extends BaseDao {



    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    Double weight;

    @DatabaseField
    Double headMeasurement;

    @DatabaseField
    Double height;

    @DatabaseField
    String notes;

    public GrowthDao() {
    }

    public GrowthDao(Double weight, Double headMeasurement, Double height, String notes, Long time) {
        this.weight = weight;
        this.headMeasurement = headMeasurement;
        this.height = height;
        this.time = time;
        this.notes = notes;
    }


    @Override
    public String toString() {
        return "GrowthDao{" +
                "id=" + id +
                ", weight=" + weight +
                ", headMeasurement=" + headMeasurement +
                ", height=" + height +
                ", notes='" + notes + '\'' +
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
}
