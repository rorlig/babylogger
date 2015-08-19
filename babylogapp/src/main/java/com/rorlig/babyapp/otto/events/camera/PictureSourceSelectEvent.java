package com.rorlig.babyapp.otto.events.camera;

import com.rorlig.babyapp.ui.fragment.profile.PictureSourceItem;

/**
 * Created by rorlig on 6/17/15.
 */
public class PictureSourceSelectEvent {


    private final PictureSourceItem pictureSourceItem;

    public PictureSourceSelectEvent(PictureSourceItem pictureSourceItem) {
        this.pictureSourceItem = pictureSourceItem;
    }

    public PictureSourceItem getPictureSourceItem() {
        return pictureSourceItem;
    }
}
