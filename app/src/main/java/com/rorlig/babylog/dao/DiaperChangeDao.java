package com.rorlig.babylog.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rorlig.babylog.model.diaper.DiaperChangeColorType;
import com.rorlig.babylog.model.diaper.DiaperChangeEnum;
import com.rorlig.babylog.model.diaper.DiaperChangeTextureType;
import com.rorlig.babylog.model.diaper.DiaperIncident;

import java.util.Date;

/**
 * @author gaurav gupta
 * Diaper Change Dao
 */
@DatabaseTable
public class DiaperChangeDao extends BaseDao  {

    //generated id
    @DatabaseField(generatedId = true)
    int id;

    //diaper change event type
    @DatabaseField
    DiaperChangeEnum diaperChangeEventType;

    //diaper change poop texture
    @DatabaseField
    DiaperChangeTextureType poopTexture;

    //diaper change color
    @DatabaseField
    DiaperChangeColorType poopColor;

    //diaper change indicident type
    @DatabaseField
    DiaperIncident diaperChangeIncidentType;

    //diaper change notes
    @DatabaseField
    java.lang.String diaperChangeNotes;


    public DiaperChangeDao() {
    }


    public DiaperChangeDao(DiaperChangeEnum diaperChangeEventType,
                           DiaperChangeTextureType poopTexture,
                           DiaperChangeColorType poopColor,
                           DiaperIncident diaperChangeIncidentType,
                           java.lang.String diaperChangeNotes, Date time) {
        this.diaperChangeEventType = diaperChangeEventType;
        this.poopTexture = poopTexture;
        this.poopColor = poopColor;
        this.diaperChangeIncidentType = diaperChangeIncidentType;
        this.diaperChangeNotes = diaperChangeNotes;
        this.date = new Date(time.getTime());
    }

    @Override
    public java.lang.String toString() {
        return "DiaperChangeDao{" +
                "id=" + id +
                ", diaperChangeEventType=" + diaperChangeEventType +
                ", poopTexture=" + poopTexture +
                ", poopColor=" + poopColor +
                ", diaperChangeIncidentType=" + diaperChangeIncidentType +
                ", diaperChangeNotes='" + diaperChangeNotes + '\'' +
                ", date=" + date +
                '}';
    }




    public DiaperChangeEnum getDiaperChangeEventType() {
        return diaperChangeEventType;
    }

    public DiaperChangeTextureType getPoopTexture() {
        return poopTexture;
    }

    public DiaperChangeColorType getPoopColor() {
        return poopColor;
    }

    public DiaperIncident getDiaperChangeIncidentType() {
        return diaperChangeIncidentType;
    }

    public java.lang.String getDiaperChangeNotes() {
        return diaperChangeNotes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
