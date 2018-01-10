package com.chokus.konye.packmyload

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_phone_numbers.*

class PhoneNumbersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_numbers)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.phone_numb)
        resend_code_butn.text = null
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
            val intent = Intent(applicationContext, NumberVerificationActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
