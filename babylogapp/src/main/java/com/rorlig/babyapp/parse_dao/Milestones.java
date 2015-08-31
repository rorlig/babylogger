package com.rorlig.babyapp.parse_dao;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.File;
import java.util.Date;

/**
 * @author gaurav gupta
 * Milestone Dao
 */
@ParseClassName("Milestone")
public class Milestones extends BabyLogBaseParseObject {



    //milestone title
    String title;

    //notes
    String notes;

    //image path for the file...
    String imagePath;


//    //time at which the log was changed ...
//    Date logCreationDate;

    ParseFile parseFile;


    public Milestones() {


    }
    public Milestones(String title, String notes,
                      Date time, String imagePath, ParseFile parseFile) {
        setTitle(title);
        setNotes(notes);
        setLogCreationDate(time);
        setImagePath(imagePath);
        setParseFile(parseFile);
//        this.title = title;
//        this.notes  = notes;
//        this.logCreationDate = new Date(time.getTime());
//        this.imagePath = imagePath;
    }

    public void setParseFile(final ParseFile parseFile) {
        if (parseFile!=null)
        put("imageFile", parseFile);
    }

    public ParseFile getParseFile() {
       return getParseFile("imageFile");
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

//    public Date getLogCreationDate() {
//        return getDate("logCreationDate");
//    }
//
//    public void setLogCreationDate(Date logCreationDate) {
//        put("logCreationDate", logCreationDate);
//    }

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
