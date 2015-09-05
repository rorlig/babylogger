package com.rorlig.babyapp.otto;

import android.net.Uri;

import java.util.ArrayList;

/**
 * @author gaurav gupta
 */
public class UriCreated {
    private final ArrayList<Uri> uris;

    public UriCreated(ArrayList<Uri> uris) {
        this.uris = uris;
    }

    public ArrayList<Uri> getUris() {
        return uris;
    }
}
