package com.chokus.konye.packmyload

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_get_started.*

import kotlinx.android.synthetic.main.activity_phone_number.*

class PhoneNumberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        setSupportActionBar(toolbar)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setTitle(R.string.phone_numb)
    }

    private fun viewActions(){
        //do stuff here
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_phone_number,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.next){
            val intent : Intent = Intent(applicationContext, NumberVerificationActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
