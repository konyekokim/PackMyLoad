package com.chokus.konye.packmyload.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.chokus.konye.packmyload.R
import com.chokus.konye.packmyload.application.MyApplication
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject

class ProfileActivity : AppCompatActivity(){

    private var progressDialog : ProgressDialog? = null
    private val URL ="put in the valid URLs here"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.profile_title)
        progressDialog = ProgressDialog(this)
    }

    private fun checkViews(){
        //checking if edit text widgets are empty or not
        when {
            firstName_editText.text.toString().isEmpty() -> toastMethod("please enter your first name")
            lastName_editText.text.toString().isEmpty() -> toastMethod("please enter your last name")
            emailAddress_editText.text.toString().isEmpty() -> toastMethod("please enter your email address")
            phoneNumber_editText.text.toString().isEmpty() -> toastMethod("please enter your phone number")
            else -> sendData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save_profile,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.save){
            //do saving action here wait for backend to be ready and collect from widgets here
            //checkViews()
            //remember to remove this block of code
            val intent = Intent(applicationContext, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendData(){
        progressDialog!!.setMessage("Saving data please wait...")
        progressDialog!!.show()
        val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener<String>{response ->
                    progressDialog!!.dismiss()
                    val obj = JSONObject(response)
                    //create a toast or whatever function for whatever you want to do on from here
                    val intent = Intent(applicationContext, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()

                }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                progressDialog!!.dismiss()
                toastMethod(error?.message)
            }
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                //need better API to add the information from this activity to the database e.g. shown  below
                //params.put("name", firstName_editText.text.toString())
                return params
            }
        }
        MyApplication.instance?.addToRequestQueue(stringRequest)
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
