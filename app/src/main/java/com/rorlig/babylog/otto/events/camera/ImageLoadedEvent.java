package com.rorlig.babylog.otto.events.camera;

import android.net.Uri;

/**
 * Created by rorlig on 6/17/15.
 */
public class ImageLoadedEvent {


    private final Uri uri;

    public ImageLoadedEvent(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }
}
