package com.rorlig.babylog.otto;

import android.graphics.drawable.Drawable;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author gaurav gupta
 */
public class UpdateActionBarEvent {

    private final Drawable drawable;

    public UpdateActionBarEvent(CircleImageView babyImageView) {
        this.drawable = babyImageView.getDrawable();
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
