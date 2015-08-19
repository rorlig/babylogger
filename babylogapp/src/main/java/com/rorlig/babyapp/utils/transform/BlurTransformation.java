package com.rorlig.babyapp.utils.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Transformation;

public class BlurTransformation implements Transformation {

  RenderScript rs;

  public BlurTransformation(Context context) {
    super();
    rs = RenderScript.create(context);
  }

  @Override
  public Bitmap transform(Bitmap bitmap) {
    // Create another bitmap that will hold the results of the filter.
    Bitmap blurredBitmap = Bitmap.createBitmap(bitmap);

    // Allocate memory for Renderscript to work with
    Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SHARED);
    Allocation output = Allocation.createTyped(rs, input.getType());

    // Load up an instance of the specific script that we want to use.
    ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
    script.setInput(input);

    // Set the blur radius
    script.setRadius(25);

    // Start the ScriptIntrinisicBlur
    script.forEach(output);

    // Copy the output to the blurred bitmap
    output.copyTo(blurredBitmap);
    bitmap.recycle();

    blurredBitmap = changeBitmapContrastBrightness(blurredBitmap, 1, -10);

    return blurredBitmap;
  }

  @Override
  public String key() {
    return "blur";
  }

  private Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {
    ColorMatrix cm = new ColorMatrix(new float[]
            {
                    contrast, 0, 0, 0, brightness,
                    0, contrast, 0, 0, brightness,
                    0, 0, contrast, 0, brightness,
                    0, 0, 0, 1, 0
            });

    Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

    Canvas canvas = new Canvas(ret);

    Paint paint = new Paint();
    paint.setColorFilter(new ColorMatrixColorFilter(cm));
    canvas.drawBitmap(bmp, 0, 0, paint);

    return ret;
  }

}
