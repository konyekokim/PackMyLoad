package com.chokus.konye.packmyload

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_get_started.*

class GetStartedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        setFullScreen()
        viewActions()

    }
    private fun viewActions(){
        login_with_fb_button.setOnClickListener{
            val intent = Intent(applicationContext, FacebookLoginActivity:: class.java)
            startActivity(intent)
        }
        use_phone_number_button.setOnClickListener{
            val intent = Intent(applicationContext, PhoneNumbersActivity:: class.java)
            startActivity(intent)
        }
    }
    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
