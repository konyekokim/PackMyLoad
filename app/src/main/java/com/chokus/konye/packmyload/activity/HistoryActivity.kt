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
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.chokus.konye.packmyload.R
import com.chokus.konye.packmyload.adapter.HistoryRecyclerAdapter
import com.chokus.konye.packmyload.application.MyApplication
import com.chokus.konye.packmyload.models.HistoryClass
import com.chokus.konye.packmyload.models.ServiceClass
import kotlinx.android.synthetic.main.activity_history.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class HistoryActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager : LinearLayoutManager
    private var historyRecyclerAdapter : HistoryRecyclerAdapter? = null
    private var historyList = ArrayList<HistoryClass>()
    private var progressDialog : ProgressDialog? = null
    private var userID : String? = null
    private var userEmail : String? = null
    private var loginToken : String? = null
    private val URL = "http://packmyload.com/api/movementhistory"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.history_title)
        checkNetworkConnection()
        linearLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        viewActions()
        //remember to get intents for the userid, useremail and logintoken used in sendData function
        //sendData()
    }

    private fun viewActions(){
        history_recyclerView.layoutManager = linearLayoutManager
        historyRecyclerAdapter = HistoryRecyclerAdapter(historyList, this)
        history_recyclerView.adapter = historyRecyclerAdapter
        historyRecyclerAdapter!!.notifyDataSetChanged()
        showRecyclerView()
    }

    private fun sendData(){
        progressDialog!!.setMessage("Loading")
        progressDialog!!.show()
        val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener<String>{ response ->
                    progressDialog!!.dismiss()
                    //here we will put in method to get JSON Array
                    getDataForHistoryList()

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
                params.put("userid", userID!!)
                params.put("useremail", userEmail!!)
                params.put("logintoken", loginToken!!)
                return params
            }
        }
        MyApplication.instance?.addToRequestQueue(stringRequest)
    }

    private fun getDataForHistoryList(){
        progressDialog!!.setMessage("Loading")
        progressDialog!!.show()
        val request = JsonArrayRequest(Request.Method.POST, URL, null,
                Response.Listener<JSONArray>{ response ->
                    //use this to get hte response from the backend
                    progressDialog!!.dismiss()
                    var count = 0
                    while(count < response.length()){
                        try{
                            val jsonObject = response.getJSONObject(count)
                            val historyClass = HistoryClass(jsonObject.getString("type"), jsonObject.getString("posteddate"))
                            historyList.add(historyClass)
                            count++
                        }catch (e:JSONException){
                            e.printStackTrace()
                        }

                    }

                },  Response.ErrorListener{
            toastMethod("something went wrong...")
        })
        MyApplication.instance?.addToRequestQueue(request)
    }

    private fun checkNetworkConnection() {
        val backgroundLayout = findViewById(R.id.history_activity_layout) as RelativeLayout
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            //Snackbar.make(backgroundLayout, "Connection successful", Snackbar.LENGTH_SHORT).show()
            network_layout.visibility = View.GONE
        } else {
            //we are not connected to a network
            Snackbar.make(backgroundLayout, "Oops! No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY") {
                        val intent = intent
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }.setActionTextColor(resources.getColor(R.color.colorPrimary)).show()
            network_layout.visibility = View.VISIBLE
        }
    }

    private fun showRecyclerView(){
        if(historyList.isEmpty()){
            history_recyclerView.visibility = View.GONE
            no_pml_textView.visibility = View.VISIBLE
        }else{
            history_recyclerView.visibility = View.VISIBLE
            no_pml_textView.visibility = View.GONE
        }
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
