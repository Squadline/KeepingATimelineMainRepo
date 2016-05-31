package com.keepingatimeline.kat;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Dana on 5/26/2016.
 * Rotates, flips, and compresses Bitmaps.
 */
public class BitmapManip {

    private static final double EVENT_MAX_WIDTH = 1024;
    private static final double EVENT_MAX_HEIGHT = 1024;
    private static final double ICON_MAX_WIDTH = 200;
    private static final double ICON_MAX_HEIGHT = 200;
    private BitmapManip () { }

    public static Bitmap flipHorizontal(Bitmap original) {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        return Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
    }

    public static Bitmap flipVertical(Bitmap original) {
        Matrix matrix = new Matrix();
        matrix.postScale(1, -1);
        return Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
    }

    public static Bitmap rotateClockwise(Bitmap original, int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
    }

    public static Bitmap transpose(Bitmap original) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        matrix.postScale(-1, 1);
        return Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
    }

    public static Bitmap transverse(Bitmap original) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        matrix.postScale(1, -1);
        return Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
    }

    // rename to
    public static Bitmap shrinkToEvent(Bitmap original) {
        double newWidth = original.getWidth();
        double newHeight = original.getHeight();
        if(newWidth > EVENT_MAX_WIDTH) {
            double wScale = EVENT_MAX_WIDTH/newWidth;
            newWidth = EVENT_MAX_WIDTH;
            newHeight = newHeight * wScale;
        }
        if(newHeight > EVENT_MAX_HEIGHT) {
            double wScale = EVENT_MAX_HEIGHT/newHeight;
            newHeight = EVENT_MAX_HEIGHT;
            newWidth = newWidth * wScale;
        }

        original = Bitmap.createScaledBitmap(original, (int) newWidth, (int) newHeight, false);

        return original.copy(Bitmap.Config.RGB_565, false);
    }

    // shrink to icon
    // shrink bitmap before setting rgb
    public static Bitmap shrinkToIcon(Bitmap orig) {
        double newWidth = orig.getWidth();
        double newHeight = orig.getHeight();
        if( newWidth > ICON_MAX_WIDTH) {
            double wScale = ICON_MAX_WIDTH/newWidth;
            newWidth = ICON_MAX_WIDTH;
            newHeight = newHeight * wScale;
        }

        if( newWidth > ICON_MAX_WIDTH) {
            double wScale = ICON_MAX_HEIGHT/newHeight;
            newHeight = ICON_MAX_HEIGHT;
            newWidth = newWidth * wScale;
        }

        orig = Bitmap.createScaledBitmap(orig, (int) newWidth, (int) newHeight, false);

        return orig.copy(Bitmap.Config.RGB_565, false);
    }
}
