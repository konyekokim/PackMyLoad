package com.chokus.konye.packmyload.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.chokus.konye.packmyload.R
import com.chokus.konye.packmyload.application.MyApplication
import kotlinx.android.synthetic.main.activity_select_pickup_time.*
import kotlinx.android.synthetic.main.time_list_dialog.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SelectPickupTimeActivity : AppCompatActivity() {
    private var calendar : Calendar? = null
    private var timePickingDialog : Dialog? = null
    private var progressDialog : ProgressDialog? = null
    private val URL = "put in your API string here"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pickup_time)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.select_pickup_time)
        viewActions()
        calendar = Calendar.getInstance()
        progressDialog = ProgressDialog(this)
    }

    private fun viewActions(){
        continue_layout.setOnClickListener {
            //checkViews()
            //remember to remove this block of code
            val intent = Intent(applicationContext, ItemsMovingActivity::class.java)
            startActivity(intent)
        }
        choose_date_layout.setOnClickListener {
            datePickerAction()
        }
        choose_time_layout.setOnClickListener {
            //do time picking  function here
            timeListDialog()
            timePickingDialog!!.show()
        }
    }

    private fun checkViews(){
        when{
            chosen_date_textView.text.toString().equals(R.string.choose_date_text) ->
                    toastMethod("please choose date")
            chosen_time_textView.text.toString().equals(R.string.choose_time_text) ->
                    toastMethod("please choose time")
            else -> sendData()
        }
    }

    private fun datePickerAction(){
        val date = object: DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                calendar!!.set(Calendar.YEAR,year)
                calendar!!.set(Calendar.MONTH,month)
                calendar!!.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                chosen_date_textView.text = SimpleDateFormat("MMM-d", Locale.ENGLISH).format(calendar!!.time)
            }
        }
        DatePickerDialog(this, R.style.DialogTheme, date,calendar!!.get(Calendar.YEAR), calendar!!.get(Calendar.MONTH), calendar!!.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun timeListDialog(){
        timePickingDialog = Dialog(this)
        timePickingDialog!!.setContentView(R.layout.time_list_dialog)
        timePickingDialog!!.setCancelable(true)
        //set up listView widget in dialog
        val timeListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeListItems())
        timePickingDialog!!.time_listView.adapter = timeListAdapter
        timePickingDialog!!.time_listView.setOnItemClickListener { parent, view, position, id ->
            timePickingDialog!!.cancel()
            val timeChosenText = timeListItems().get(position)
            chosen_time_textView.text = timeChosenText
        }
    }

    private fun timeListItems() : kotlin.Array<String>{
        val items = arrayOf("9:00 AM - 10:OO AM", "10:00 AM - 11:00 AM", "11:00 AM - 12:00 PM",
                "12:00 PM - 1:00 PM", "1:00 PM - 2:00 PM", "2:00 PM - 3:00 PM", "3:00 PM - 4:00PM", "4:00PM - 5:00PM",
                "5:00PM - 6:00PM")
        return items
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
                    val intent = Intent(this, ItemsMovingActivity::class.java)
                    startActivity(intent)

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
