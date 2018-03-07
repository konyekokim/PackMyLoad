package com.chokus.konye.packmyload.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chokus.konye.packmyload.R

class AddCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.add_card_title)
    }
}
