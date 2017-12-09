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

    }
    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
    private fun viewActions(){
        get_started_button.setOnClickListener{
            val intent = Intent(applicationContext, SignUpActivity:: class.java)
            startActivity(intent)
        }
        sign_in_textView.setOnClickListener{
            val intent = Intent(applicationContext, SignInActivity:: class.java)
            startActivity(intent)
        }
    }
}
