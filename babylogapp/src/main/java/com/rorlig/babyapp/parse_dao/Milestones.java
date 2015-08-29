package com.rorlig.babyapp.parse_dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.rorlig.babyapp.dao.BaseDao;

import java.util.Date;

/**
 * @author gaurav gupta
 * Milestone Dao
 */
@ParseClassName("Milestone")
public class Milestones extends ParseObject {



    //milestone title
    String title;

    //notes
    String notes;

    //image path for the file...
    String imagePath;


    //time at which the log was changed ...
    Date logCreationDate;



    public Milestones() {


    }
    public Milestones(String title, String notes,
                      Date time, String imagePath) {
        this.title = title;
        this.notes  = notes;
        this.logCreationDate = new Date(time.getTime());
        this.imagePath = imagePath;
    }


    public String getTitle() {
        return getString("title");
    }


    public void setTitle(String title) {
        put("title", title);
    }

    public String getNotes() {
        return getString("notes");
    }

    public void setNotes(String notes) {
        put("notes", notes);
    }


    public String getImagePath() {
        return getString("imagePath");
    }

    public void setImagePath(String imagePath) {
        put("imagePath", imagePath);
    }

    public Date getLogCreationDate() {
        return getDate("logCreationDate");
    }

    public void setLogCreationDate(Date logCreationDate) {
        put("logCreationDate", logCreationDate);
    }

    @Override
    public String toString() {
        return "Milestones{" +
                "title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", logCreationDate=" + logCreationDate +
                "} " + super.toString();
    }
}
