package com.chokus.konye.packmyload.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.KeyEvent
import android.widget.RelativeLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.chokus.konye.packmyload.R
import com.chokus.konye.packmyload.application.MyApplication
import kotlinx.android.synthetic.main.activity_add_card.*
import org.json.JSONObject
import java.util.HashMap

class AddCardActivity : AppCompatActivity() {
    private var progressDialog : ProgressDialog? = null
    private val  URL = "put in your API string  here"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.add_card_title)
        checkNetworkConnection()
        progressDialog = ProgressDialog(this)
    }

    override fun onBackPressed() {
        //checkViews()
        //remember to remove this code
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //checkViews()
            //remember to remove this code
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun checkViews(){
        when{
            name_editTextView.text.toString().isEmpty() -> toastMethod("Please enter name on card")
            card_number_editText.text.toString().isEmpty() -> toastMethod("Please enter number on card")
            month_editText.text.toString().isEmpty() -> toastMethod("Please enter month on card")
            year_editText.text.toString().isEmpty() -> toastMethod("Please enter month on card")
            cvc_editText.text.toString().isEmpty() -> toastMethod("Enter your cvc number")
            zip_editText.text.toString().isEmpty() -> toastMethod("Enter your ZIP code")
            else -> sendData()
        }
    }

    private fun sendData(){
        progressDialog!!.setMessage("Loading")
        progressDialog!!.show()
        val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener<String>{ response ->
                    //use this to get hte response from the backend
                    progressDialog!!.dismiss()
                    val obj = JSONObject(response)
                    //Toast.makeText(applicationContext, obj.getString("what ever the string return " +
                    //        "in backend"), Toast.LENGTH_SHORT).show()
                    //we are using  finish so it would just go back to previous activity
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

    private fun checkNetworkConnection() {
        val backgroundLayout = findViewById(R.id.add_card_activity_layout) as RelativeLayout
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Snackbar.make(backgroundLayout, "Connection successful", Snackbar.LENGTH_SHORT).show()
        } else {
            //we are not connected to a network
            Snackbar.make(backgroundLayout, "Oops! No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY") {
                        val intent = intent
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }.setActionTextColor(Color.parseColor("#FC7900")).show()
        }
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
