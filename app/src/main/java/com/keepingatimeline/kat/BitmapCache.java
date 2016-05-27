package com.keepingatimeline.kat;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Dana on 5/26/2016.
 */
public class BitmapCache {

    private static BitmapCache BITMAP_CACHE_INSTANCE;

    private LruCache<String, Bitmap> mMemoryCache;

    private BitmapCache () {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 4;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private static BitmapCache getInstance() {
        if(BITMAP_CACHE_INSTANCE == null) BITMAP_CACHE_INSTANCE = new BitmapCache();
        return BITMAP_CACHE_INSTANCE;
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            getInstance().mMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return getInstance().mMemoryCache.get(key);
    }
}
