package com.rorlig.babyapp.parse_dao;

import com.parse.ParseClassName;
import com.rorlig.babyapp.model.diaper.DiaperChangeColorType;
import com.rorlig.babyapp.model.diaper.DiaperChangeEnum;
import com.rorlig.babyapp.model.diaper.DiaperChangeTextureType;
import com.rorlig.babyapp.model.diaper.DiaperIncident;

import java.util.Date;

/**
 * @author gaurav gupta
 */
@ParseClassName("Diaper")
public class DiaperChange extends BabyLogBaseParseObject {

    //diaper change event type
    DiaperChangeEnum diaperChangeEventType;

    //diaper change poop texture
    DiaperChangeTextureType poopTexture;

    //diaper change color
    DiaperChangeColorType poopColor;

    //diaper change indicident type
    DiaperIncident diaperChangeIncidentType;

    //diaper change notes
    String diaperChangeNotes;

//    //time at which the diaper was changed ...
//    Date logCreationDate;


    public DiaperChange() {
    }


    public DiaperChange(DiaperChangeEnum diaperChangeEventType,
                           DiaperChangeTextureType poopTexture,
                           DiaperChangeColorType poopColor,
                           DiaperIncident diaperChangeIncidentType,
                           String diaperChangeNotes,
                           Date date) {
        setDiaperChangeEventType(diaperChangeEventType);
        setPoopTexture(poopTexture);
        setPoopColor(poopColor);
        setDiaperChangeIncidentType(diaperChangeIncidentType);
        setDiaperChangeNotes(diaperChangeNotes);
        setLogCreationDate(date);
        setUuidString();
    }


    public String getDiaperChangeNotes() {
        return getString("diaperChangeNotes");
    }

    public void setDiaperChangeNotes(String diaperChangeNotes){
        put("diaperChangeNotes", diaperChangeNotes);
    }

    public String getDiaperChangeEventType() {
        return (getString("diaperChangeEventType"));
    }

    public void setDiaperChangeEventType(DiaperChangeEnum diaperChangeEventType) {
        put("diaperChangeEventType", diaperChangeEventType.toString());
    }

    public void setDiaperChangeEventType(String diaperChangeEventType) {
        put("diaperChangeEventType", diaperChangeEventType);
    }

    public String getPoopTexture() {
        return getString("poopTexture");
    }



    public void setPoopTexture(DiaperChangeTextureType poopTexture) {
        if (poopTexture!=null)
        put("poopTexture", poopTexture.toString());
    }

    public void setPoopTexture(String poopTexture) {
        put("poopTexture", poopTexture);
    }

    public String getPoopColor() {
        return getString("poopColor");
    }

    public void setPoopColor(DiaperChangeColorType poopColor) {
        if (poopColor!=null)
        put("poopColor", poopColor.toString());
    }

    public void setPoopColor(String poopColor) {
        if (poopColor!=null)
            put("poopColor", poopColor);
    }

    public String getDiaperChangeIncidentType() {
        return getString("diaperChangeIncidentType");
    }

    public void setDiaperChangeIncidentType(DiaperIncident diaperChangeIncidentType) {

        put("diaperChangeIncidentType", diaperChangeIncidentType.toString());
    }

    public void setDiaperChangeIncidentType(String diaperChangeIncidentType) {
        put("diaperChangeIncidentType", diaperChangeIncidentType);
    }


    @Override
    public String toString() {
        return "DiaperChange{" +
                "diaperChangeEventType=" + getDiaperChangeEventType() +
                ", poopTexture=" + getPoopTexture() +
                ", poopColor=" + getPoopColor() +
                ", diaperChangeIncidentType=" + getDiaperChangeIncidentType() +
                ", diaperChangeNotes='" + getDiaperChangeNotes() + '\'' +
                ", logCreationDate=" + getLogCreationDate() +
                "} " + super.toString();
    }



}
