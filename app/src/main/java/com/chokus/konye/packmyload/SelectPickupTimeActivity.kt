package com.chokus.konye.packmyload

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_select_pickup_time.*
import kotlinx.android.synthetic.main.time_list_dialog.*
import java.lang.reflect.Array
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class SelectPickupTimeActivity : AppCompatActivity() {
    private var calendar : Calendar? = null
    private var timePickingDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pickup_time)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.select_pickup_time)
        viewActions()
        calendar = Calendar.getInstance()
    }

    private fun viewActions(){
        val chosenTimeTextView =
        continue_layout.setOnClickListener {
            val intent = Intent(this, ItemsMovingActivity::class.java)
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

    private fun datePickerAction(){
        val date = object: DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                calendar!!.set(Calendar.YEAR,year)
                calendar!!.set(Calendar.MONTH,month)
                calendar!!.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                chosen_date_textView.text = SimpleDateFormat("MMM-d", Locale.ENGLISH).format(calendar!!.time)
            }
        }
        DatePickerDialog(this,R.style.DialogTheme, date,calendar!!.get(Calendar.YEAR), calendar!!.get(Calendar.MONTH), calendar!!.get(Calendar.DAY_OF_MONTH)).show()
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
        val items = arrayOf("8:00 AM - 9:00 AM", "9:00 AM - 10:OO AM", "10:00 AM - 11:00 AM", "11:00 AM - 12:00 PM",
                "12:00 PM - 1:00 PM", "1:00 PM - 2:00 PM", "2:00 PM - 3:00 PM", "3:00 PM - 4:00PM", "4:00PM - 5:00PM",
                "5:00PM - 6:00PM")
        return items
    }
}
