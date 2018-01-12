package com.chokus.konye.packmyload

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.history_title)
    }
}
