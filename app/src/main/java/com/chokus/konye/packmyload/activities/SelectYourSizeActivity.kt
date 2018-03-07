package com.chokus.konye.packmyload.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chokus.konye.packmyload.R
import kotlinx.android.synthetic.main.activity_select_your_size.*

class SelectYourSizeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_your_size)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.select_size_activity_name)
        viewActions()
    }

    private fun viewActions(){
        continue_layout.setOnClickListener {
            val intent = Intent(this, SelectPickupTimeActivity::class.java)
            startActivity(intent)
        }
    }
}
