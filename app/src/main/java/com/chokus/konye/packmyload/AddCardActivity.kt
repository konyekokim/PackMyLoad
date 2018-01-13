package com.chokus.konye.packmyload

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class AddCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.add_card_title)
    }
}
