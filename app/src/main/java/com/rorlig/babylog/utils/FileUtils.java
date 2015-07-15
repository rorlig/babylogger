package com.rorlig.babylog.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by admin on 12/21/13.
 * @author gaurav gupta
 * Simple File Utils
 */
public class FileUtils {

    private static String TAG = "FileUtils";

    /*
     * loads json files for testing purposes..
     */
    public String loadJSONFromAsset(String fileName, Context context) {
        String json;
        try {
            Log.d(TAG, "Context is " + context);
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
