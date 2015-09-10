package com.rorlig.babyapp.parse_dao;

import com.parse.ParseObject;

import java.util.Date;
import java.util.UUID;

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

    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put("uuid", uuid.toString());
    }

    public String getUuidString() {
        return getString("uuid");
    }

    @Override
    public String toString() {
        return "BabyLogBaseParseObject{" +
                "logCreationDate=" + getLogCreationDate() + " uuid=" + getUuidString() +
                "} " + super.toString();
    }
}
