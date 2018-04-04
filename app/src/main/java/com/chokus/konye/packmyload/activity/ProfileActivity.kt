package com.chokus.konye.packmyload.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.chokus.konye.packmyload.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private var progressDialog : ProgressDialog? = null
    private val URL ="put in the valid URLs here"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.profile_title)
        progressDialog = ProgressDialog(this)
    }

    private fun onViewActions(){
        //checking if edit text widgets are empty or not
        if(firstName_editText.toString().isEmpty()){
            Toast.makeText(this, "Please enter your fist naeme", Toast.LENGTH_SHORT).show()
        }
        if(lastName_editText.toString().isEmpty()){
            Toast.makeText(this, "Plesse enter your last name", Toast.LENGTH_SHORT).show()
        }
        if(emailAddress_editText.toString().isEmpty()){
            Toast.makeText(this, "Plesse enter your last name", Toast.LENGTH_SHORT).show()
        }
        if(phoneNumber_editText.toString().isEmpty()){
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save_profile,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.save){
            //do saving action here wait for backend to be ready and collect from widgets here
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendData(){
        progressDialog!!.setMessage("Saving data please wait...")
        progressDialog!!.show()

    }
}
