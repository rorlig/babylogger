package com.rorlig.babylog.ui.widget;

/**
 * Created by admin on 4/13/14.
 */
public interface DrawableClickListener {
    public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    public void onClick(DrawablePosition target);
}