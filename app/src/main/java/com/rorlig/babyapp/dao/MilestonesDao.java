package com.rorlig.babyapp.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author gaurav gupta
 * Milestone Dao
 */
@DatabaseTable
public class MilestonesDao extends BaseDao {


    @DatabaseField(generatedId = true)
    int id;




    //milestone title
    @DatabaseField
    String title;

    @DatabaseField
    String notes;


    @DatabaseField
    String imagePath;


    public MilestonesDao() {

    }
    public MilestonesDao(String title, String notes,
                         Date time, String imagePath) {
        this.title = title;
        this.notes  = notes;
        this.date = new Date(time.getTime());
        this.imagePath = imagePath;
    }


    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getNotes() {
        return notes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }
}
