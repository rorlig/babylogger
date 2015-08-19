package com.rorlig.babyapp.otto.events.camera;

import android.net.Uri;

/**
 * Created by rorlig on 6/18/15.
 */
public class PictureSelectEvent {
    private final Uri imageUri;

    public PictureSelectEvent(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
