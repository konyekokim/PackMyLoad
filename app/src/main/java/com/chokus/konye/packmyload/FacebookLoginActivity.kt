package com.chokus.konye.packmyload

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_fb_login.*

class FacebookLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fb_login)
        setFullScreen()
        //viewActions()
    }

    private fun viewActions(){
        continue_button.setOnClickListener {
            val intent : Intent = Intent(applicationContext, HomePageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
