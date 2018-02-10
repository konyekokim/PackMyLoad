package com.chokus.konye.packmyload

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_select_pickup_time.*

class SelectPickupTimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pickup_time)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.select_pickup_time)
        viewActions()
    }

    private fun viewActions(){
        continue_layout.setOnClickListener {
            val intent = Intent(this, ItemsMovingActivity::class.java)
            startActivity(intent)
        }
    }
}
