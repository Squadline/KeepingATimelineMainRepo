package com.keepingatimeline.kat;

//both
import android.util.Base64;
import android.graphics.Bitmap;
import java.lang.String;
import android.util.Log;
//BTSB64
import java.io.ByteArrayOutputStream;

//SB64TB
import android.graphics.BitmapFactory;

/**
 * Created by Dana on 5/22/2016.
 */
public final class PictureCompactor {

    private PictureCompactor() {}

    public static String BitmapToStringB64(Bitmap in)
    {
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.JPEG, 25, bAOS);
        byte[] bArray = bAOS.toByteArray();
        String s = Base64.encodeToString(bArray, Base64.DEFAULT);
        Log.d("BitmapToStringB64", "Orig String " + bAOS.toByteArray().length);
        Log.d("BitmapToStringB64", "B64 String: " + s.length());
        return s;
    }

    public static Bitmap StringB64ToBitmap(String in)
    {
        byte[] bArray = Base64.decode(in, Base64.DEFAULT);
        Bitmap bMap = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);

        return bMap;
    }


    public static String BitmapToString(Bitmap in)
    {
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.JPEG, 85, bAOS);
        String s = bAOS.toString();
        Log.d("BitmapToString", "Orig String" + bAOS.toByteArray().length);
        Log.d("BitmapToString", "bAOS String: " + s.length());
        return s;
    }

    public static Bitmap StringToBitmap(String in)
    {
        byte[] bArray = in.getBytes();
        Bitmap bMap = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
        return bMap;
    }
}
