package com.rorlig.babyapp.ui.widget;

/**
 * @author gaurav gupta
 */
public interface DrawableClickListener {
    enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT }
    void onClick(DrawablePosition target);
}