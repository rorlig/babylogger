package com.rorlig.babylog.ui.widget;

/**
 * @author gaurav gupta
 */
public interface DrawableClickListener {
    enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT }
    void onClick(DrawablePosition target);
}