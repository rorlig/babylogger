package com.rorlig.babylog.otto.events.camera;

import com.rorlig.babylog.ui.fragment.profile.PictureSourceItem;

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
