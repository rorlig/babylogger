package com.rorlig.babyapp.parse_dao;

/**
 * @author gaurav gupta
 */

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONObject;

/**
 * @author gaurav gupta
 */
@ParseClassName("Baby")
public class Baby extends ParseObject {

    private String name;
    private String dob;
    private String imagePath;
    private ParseFile parseFile;

    public Baby(){

    }

    public Baby(String name, String dob, String imagePath, ParseFile parseFile) {
        setName(name);
        setDob(dob);
        setImagePath(imagePath);
        setParseFile(parseFile);
    }



    public void setParseFile(final ParseFile parseFile) {
        if (parseFile!=null)
            put("imageFile", parseFile);
        else put("imageFile", JSONObject.NULL);
    }

    public ParseFile getParseFile() {
        return getParseFile("imageFile");
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getDob() {
        return getString("dob");
    }

    public void setDob(String dob) {
        put("dob", dob);
    }

    public String getImagePath() {

        return getString("imagePath");
    }

    public void setImagePath(String imagePath) {
        put("imagePath", imagePath);
    }
}
