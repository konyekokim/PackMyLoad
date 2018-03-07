package com.chokus.konye.packmyload.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chokus.konye.packmyload.R

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.history_title)
    }
}
