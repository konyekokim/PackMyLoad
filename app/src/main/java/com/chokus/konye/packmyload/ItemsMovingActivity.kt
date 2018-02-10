package com.chokus.konye.packmyload

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ItemsMovingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_moving)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.items_moving_activity)
    }
}
