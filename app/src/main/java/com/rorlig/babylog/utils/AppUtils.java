package com.rorlig.babylog.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by rorlig on 6/17/15.
 */
public class AppUtils {

    public static final int RESULT_CAMERA_IMAGE_CAPTURE = 1;
    public static final int RESULT_LOAD_IMAGE = 2;
    public static final int RESULT_CROP_IMAGE = 3;

    public static String getCameraDirectory() {
        String dirPath = "";
        //Case for Moto X
        File cameraDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if(cameraDir.exists()) {
            dirPath = cameraDir.getAbsolutePath();
        }
        else {
            //Generic case
            cameraDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if(cameraDir.exists()) {
                dirPath = cameraDir.getAbsolutePath();
            }
            else {
                // If there's no DCIM directory create picutre directory on SDCARD
                cameraDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if(cameraDir.exists()) {
                    dirPath = cameraDir.getAbsolutePath();
                }
                else {

                    try {
                        cameraDir.createNewFile();
                    }
                    catch(IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

        return dirPath;
    }

}
