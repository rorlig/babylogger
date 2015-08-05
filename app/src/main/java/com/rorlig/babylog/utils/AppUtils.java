package com.rorlig.babylog.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rorlig on 6/17/15.
 */
public class AppUtils {

    public static final int RESULT_CAMERA_IMAGE_CAPTURE = 1;
    public static final int RESULT_LOAD_IMAGE = 2;
    public static final int RESULT_CROP_IMAGE = 3;

    public static final int PROFILE_ACTIVITY = 1;

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


    public static Spannable getSpannable(CharSequence charSequence, int color) {
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        };
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        Spannable spannable =  Spannable.Factory.getInstance().newSpannable(charSequence);
        spannable.setSpan(cs, 0 , charSequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(foregroundColorSpan, 0 , charSequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }


    public static Drawable getDrawableFromUri(Uri uri, Context context) throws FileNotFoundException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return Drawable.createFromStream(inputStream, uri.toString() );
    }

}
