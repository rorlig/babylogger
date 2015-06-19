package com.rorlig.babylog.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * table of growth measurements purchased..
 */
@DatabaseTable
public class MilestonesDao extends BaseDao {


    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    String completionDateRange;

    @DatabaseField
    boolean completed;

    @DatabaseField
    String title;

    @DatabaseField
    Long completionDate;

    public MilestonesDao() {
    }


    public MilestonesDao(String completionDateRange, boolean completed, String title, Long completionDate) {
        this.completionDateRange = completionDateRange;
        this.completed = completed;
        this.title = title;
        this.completionDate = completionDate;
    }

    @Override
    public String toString() {
        return "MilestonesDao{" +
                "id=" + id +
                ", completionDateRange='" + completionDateRange + '\'' +
                ", completed=" + completed +
                ", title='" + title + '\'' +
                ", notes='" + completionDate + '\'' +
                '}';
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
