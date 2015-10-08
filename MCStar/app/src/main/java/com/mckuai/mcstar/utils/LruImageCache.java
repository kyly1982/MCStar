package com.mckuai.mcstar.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by kyly on 2015/10/8.
 */
public class LruImageCache implements ImageLoader.ImageCache {
    LruCache<String,Bitmap> mMemoryCache;

    public LruImageCache(){
        mMemoryCache = new LruCache<String,Bitmap>(4*1024*1024){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.put(url,bitmap);
    }
}
