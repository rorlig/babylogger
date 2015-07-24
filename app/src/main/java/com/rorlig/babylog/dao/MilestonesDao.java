package com.rorlig.babylog.dao;

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


    //range in which milestone is typically done
    @DatabaseField
    String completionDateRange;

    //is the milestone completed or not..
    @DatabaseField
    boolean completed;


    //milestone title
    @DatabaseField
    String title;


    //date when milestone was completed...
    @DatabaseField
    Date completionDate;


    public MilestonesDao() {
    }


    public MilestonesDao(String completionDateRange,
                         boolean completed,
                         String title,
                         Date completionDate,
                         Date time) {
        this.completionDateRange = completionDateRange;
        this.completed = completed;
        this.title = title;
        this.completionDate = completionDate;
        this.date = new Date(time.getTime());
    }

    @Override
    public String toString() {
        return "MilestonesDao{" +
                "id=" + id +
                ", completionDateRange='" + completionDateRange + '\'' +
                ", completed=" + completed +
                ", title='" + title + '\'' +
                ", completionDate=" + completionDate +
                "} " + super.toString();
    }

    public String getCompletionDateRange() {
        return completionDateRange;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getTitle() {
        return title;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }




}
