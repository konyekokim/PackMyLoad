package com.chokus.konye.packmyload

import android.app.Application
import android.support.multidex.MultiDex
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by omen on 20/02/2018.
 */
class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Fresco.initialize(this)
    }
}