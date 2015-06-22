package com.rorlig.babylog.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
    Long completionDate;


    public MilestonesDao() {
    }


    public MilestonesDao(String completionDateRange,
                         boolean completed,
                         String title,
                         Long completionDate,
                         Long time) {
        this.completionDateRange = completionDateRange;
        this.completed = completed;
        this.title = title;
        this.completionDate = completionDate;
        this.time = time;
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

    public Long getCompletionDate() {
        return completionDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }




}
