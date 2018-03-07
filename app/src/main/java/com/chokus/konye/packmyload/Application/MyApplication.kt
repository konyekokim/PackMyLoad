package com.chokus.konye.packmyload.Application

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.FirebaseApp

/**
 * Created by omen on 20/02/2018.
 */
class MyApplication : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Fresco.initialize(this)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        FirebaseApp.initializeApp(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}