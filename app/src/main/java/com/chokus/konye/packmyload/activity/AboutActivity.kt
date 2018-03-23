package com.chokus.konye.packmyload.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chokus.konye.packmyload.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.about_title)
    }
}