package com.chokus.konye.packmyload.cache

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader


/**
 * Created by omen on 10/04/2018.
 */
class LruBitmapCache constructor(sizeInKB : Int = defaultLruCacheSize):
        LruCache<String, Bitmap>(sizeInKB), ImageLoader.ImageCache{

    override fun sizeOf(key : String, value: Bitmap) : Int = value.rowBytes * value.height/1024

    override fun getBitmap(url : String) : Bitmap?{
        return get(url)
    }

    override fun putBitmap(url : String, bitmap: Bitmap){
        put(url, bitmap)
    }

    companion object {
        val defaultLruCacheSize : Int
        get(){
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            val cacheSize = maxMemory / 8
            return cacheSize
        }
    }
}