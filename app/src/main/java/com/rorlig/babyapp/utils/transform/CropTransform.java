package com.rorlig.babyapp.utils.transform;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

/**
 * @author gaurav gupta
 */
public class CropTransform implements Transformation {

    private int mWidth;
    private int mHeight;
    private CropTransform.CropType mCropType;

    public CropTransform() {
        this.mCropType = CropTransform.CropType.CENTER;
    }

    public CropTransform(float width, float height) {
        this();
        this.mWidth = (int) width;
        this.mHeight = (int) height;
    }

    public CropTransform(int width, int height, CropTransform.CropType cropType) {
        this.mWidth = width;
        this.mHeight = height;
        this.mCropType = cropType;
    }

    public Bitmap transform(Bitmap source) {
        if(this.mWidth == 0) {
            this.mWidth = source.getWidth();
        }

        if(this.mHeight == 0) {
            this.mHeight = source.getHeight();
        }

        float scaleX = (float)this.mWidth / (float)source.getWidth();
        float scaleY = (float)this.mHeight / (float)source.getHeight();
        float scale = Math.max(scaleX, scaleY);
        float scaledWidth = scale * (float)source.getWidth();
        float scaledHeight = scale * (float)source.getHeight();
        float left = ((float)this.mWidth - scaledWidth) / 2.0F;
        float top = this.getTop(scaledHeight);
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        Bitmap bitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(source, (Rect)null, targetRect, (Paint)null);
        source.recycle();
        return bitmap;
    }

    public String key() {
        return "CropTransformation(width=" + this.mWidth + ", height=" + this.mHeight + ", cropType=" + this.mCropType + ")";
    }

    private float getTop(float scaledHeight) {
        switch(mCropType) {
            case TOP:
                return 0.0F;
            case CENTER:
                return ((float)this.mHeight - scaledHeight) / 2.0F;
            case BOTTOM:
                return (float)this.mHeight - scaledHeight;
            default:
                return 0.0F;
        }
    }

    public static enum CropType {
        TOP,
        CENTER,
        BOTTOM;

        private CropType() {
        }
    }
}