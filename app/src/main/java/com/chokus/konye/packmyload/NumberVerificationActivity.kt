package com.chokus.konye.packmyload

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_get_started.*

import kotlinx.android.synthetic.main.activity_phone_numbers.*

class NumberVerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_numbers)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.verify_code)
        adjustWidgets()
    }

    private fun adjustWidgets(){
        number_editText.setHint(R.string.code_hint)
        number_editText.inputType(InputType.TYPE_CLASS_NUMBER)
        guide_textView.setText(R.string.verify_guide)
        //resend_code_butn.visibility(View.VISIBLE)
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
            val intent = Intent(applicationContext, HomePageActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}

operator fun Int.invoke(number: Int) {}
