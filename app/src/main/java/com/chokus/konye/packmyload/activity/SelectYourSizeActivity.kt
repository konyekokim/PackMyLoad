package com.chokus.konye.packmyload.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.chokus.konye.packmyload.R
import kotlinx.android.synthetic.main.activity_select_your_size.*

class SelectYourSizeActivity : AppCompatActivity() {
    private var progressDialog : ProgressDialog? = null
    private val URL = "put in API url here"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_your_size)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.select_size_activity_name)
        viewActions()
        progressDialog = ProgressDialog(this)
    }

    private fun viewActions(){
        continue_layout.setOnClickListener {
            val intent = Intent(this, SelectPickupTimeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
