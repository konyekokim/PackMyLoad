package com.chokus.konye.packmyload

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportActionBar!!.hide()
    }

}
