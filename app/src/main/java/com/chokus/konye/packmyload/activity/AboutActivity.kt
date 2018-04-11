package com.chokus.konye.packmyload.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
import android.view.WindowManager
import android.widget.RelativeLayout
import com.chokus.konye.packmyload.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    private val instagramProfileLink = "http://instagram.com/_u/packmyload"
    private val instagramProfileWebLink = "http://instagram.com/packmyload"
    private val instaPackageName = "com.instagram.android"
    private val twitter_user_name = "packmyload"
    private val FACEBOOK_URL = "https://www.facebook.com/packmyload"
    private val FACEBOOK_PAGE_ID = "packmyload"
    private val facebookPackageName = "com.facebook.katana"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.about_title)
        checkNetworkConnection()
    }

    private fun viewActions(){
        instagram_relativeLayout.setOnClickListener {
            linkToInstagram()
        }
        twitter_relativeLayout.setOnClickListener {
            linkToTwitter()
        }
        facebook_relativeLayout.setOnClickListener {
            linkToFacebook()
        }
    }

    private fun linkToInstagram(){
        val uri = Uri.parse(instagramProfileLink)
        val linkToIgIntent = Intent(Intent.ACTION_VIEW, uri)
        linkToIgIntent.`package` = instaPackageName
        try{
            startActivity(linkToIgIntent)
        }catch (e : ActivityNotFoundException){
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(instagramProfileWebLink)))
        }
    }

    private fun linkToTwitter(){
        try{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("twitter:user?screen_name=$twitter_user_name")))
        }catch (e : Exception){
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https:twitter.com/#!/$twitter_user_name")))
        }
    }

    private fun linkToFacebook(){
        val facebookIntent = Intent(Intent.ACTION_VIEW)
        val facebookUrl = getFacebookPageURL(this)
        facebookIntent.data = Uri.parse(facebookUrl)
        startActivity(facebookIntent)
    }

    private fun getFacebookPageURL(context : Context) : String{
        val packageManager = context.packageManager
        try{
            val versionCode = packageManager.getPackageInfo(facebookPackageName , 0).versionCode
            val activated : Boolean = packageManager.getApplicationInfo( facebookPackageName , 0).enabled
            if(activated){
                if(versionCode >= 3002850){//newer versions of the facebook app
                    return "fb://facewebmodal/f?href=$FACEBOOK_URL"
                }else{
                    return "fb://page/$FACEBOOK_PAGE_ID"
                }
            }else{
                return FACEBOOK_URL //normal web url
            }
        }catch (e : PackageManager.NameNotFoundException){
            return FACEBOOK_URL
        }
    }

    private fun checkNetworkConnection() {
        val backgroundLayout = findViewById<RelativeLayout>(R.id.about_activity_layout)
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Snackbar.make(backgroundLayout, "Connection successful", Snackbar.LENGTH_SHORT).show()
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            //we are not connected to a network
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            Snackbar.make(backgroundLayout, "Oops! No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY") {
                        val intent = intent
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }.setActionTextColor(resources.getColor(R.color.colorPrimary)).show()
        }
    }
}
