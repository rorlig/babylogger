package com.rorlig.babylog.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by admin on 3/22/14.
 */
public class HackyDrawerLayout extends DrawerLayout {


    public HackyDrawerLayout(Context context) {
        super(context);
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean mIsDisallowIntercept = false;

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // keep the info about if the innerViews do requestDisallowInterceptTouchEvent
        mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        // the incorrect array size will only happen in the multi-touch scenario.
        if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false);
            boolean handled = super.dispatchTouchEvent(ev);
            requestDisallowInterceptTouchEvent(true);
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

//    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        final int childCount = getChildCount();
//        float scrimOpacity = 0;
//        for (int i = 0; i < childCount; i++) {
//            final float onscreen = ((LayoutParams) getChildAt(i).getLayoutParams()).onScreen;
//            scrimOpacity = Math.max(scrimOpacity, onscreen);
//        }
//        mScrimOpacity = scrimOpacity;
//
//        // "|" used on purpose; both need to run.
//        if (mLeftDragger.continueSettling(true) | mRightDragger.continueSettling(true)) {
//            ViewCompat.postInvalidateOnAnimation(this);
//        }
//    }
}