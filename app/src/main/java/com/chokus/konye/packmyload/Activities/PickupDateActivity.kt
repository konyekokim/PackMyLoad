package com.chokus.konye.packmyload.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chokus.konye.packmyload.R
import kotlinx.android.synthetic.main.activity_pickup_date.*

class PickupDateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup_date)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.date_pickup_title_example)
        viewActions()
    }

    private fun viewActions(){
        add_payment_method_layout.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivity(intent)
        }
        add_payment_relativeLayout.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivity(intent)
        }
    }
}
