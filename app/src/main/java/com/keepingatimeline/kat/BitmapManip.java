package com.keepingatimeline.kat;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Dana on 5/26/2016.
 * Rotates, flips, and compresses Bitmaps.
 */
public class BitmapManip {

    private static final double MAX_WIDTH = 1024;
    private static final double MAX_HEIGHT = 1024;

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

    public static Bitmap shrink(Bitmap original) {
        double newWidth = original.getWidth();
        double newHeight = original.getHeight();
        if(newWidth > MAX_WIDTH) {
            double wScale = MAX_WIDTH/newWidth;
            newWidth = MAX_WIDTH;
            newHeight = newHeight * wScale;
        }
        if(newHeight > MAX_HEIGHT) {
            double wScale = MAX_HEIGHT/newHeight;
            newHeight = MAX_HEIGHT;
            newWidth = newWidth * wScale;
        }

        original = Bitmap.createScaledBitmap(original, (int) newWidth, (int) newHeight, false);

        return original.copy(Bitmap.Config.RGB_565, false);
    }
}
