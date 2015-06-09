package com.rorlig.babylog.scheduler;

import android.content.Context;
import android.graphics.Typeface;

import com.rorlig.babylog.dagger.ForApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by gaurav.
 */
@Singleton
public class  TypeFaceManager {
    @ForApplication
    @Inject
    Context context;

    public TypeFaceManager(Context context) {
        this.context = context;
    }


    public Typeface getTypeFace(String fileName){
        return Typeface.createFromAsset(context.getAssets(),
                                        fileName);

    }

}
