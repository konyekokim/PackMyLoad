package com.chokus.konye.packmyload

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_items_moving.*

class ItemsMovingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_moving)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.items_moving_activity)
        viewActions()
    }

    private fun viewActions(){
        camera_icon.setOnClickListener {
            //add needed action here
        }
        add_photo_layout.setOnClickListener {
            val intent = Intent(this, PickupDateActivity::class.java)
            startActivity(intent)
        }
    }
}
