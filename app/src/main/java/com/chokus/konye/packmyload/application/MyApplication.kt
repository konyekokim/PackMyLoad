package com.chokus.konye.packmyload.application

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog.TAG
import com.android.volley.toolbox.Volley
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

        //adding volley singleton instance
        instance = this
    }

    val requestQueue : RequestQueue? = null

    get(){
        if(field == null){
            return Volley.newRequestQueue(applicationContext)
        }
        return field
    }

    fun <T> addToRequestQueue(request : Request<T>){
        request.tag = TAG
        requestQueue?.add(request)
    }

    companion object {
        private val TAG = MyApplication::class.java.simpleName
        @get:Synchronized var instance : MyApplication? = null
        private set
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}