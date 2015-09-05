package com.rorlig.babyapp.parse_dao;

import com.parse.ParseObject;

import java.util.Date;

/**
 * @author gaurav gupta
 * Base Parse Object class..
 */
public class BabyLogBaseParseObject extends ParseObject {
    //time at which the diaper was changed ...
    Date logCreationDate;

    public void setLogCreationDate(Date date) {
        put("logCreationDate", date);
    }

    public Date getLogCreationDate() {
        return getDate("logCreationDate");
    }

}
