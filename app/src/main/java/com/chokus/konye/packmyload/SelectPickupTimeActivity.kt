package com.chokus.konye.packmyload

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_select_pickup_time.*
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class SelectPickupTimeActivity : AppCompatActivity() {
    private var calendar : Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pickup_time)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.select_pickup_time)
        viewActions()
        calendar = Calendar.getInstance()
    }

    private fun viewActions(){
        continue_layout.setOnClickListener {
            val intent = Intent(this, ItemsMovingActivity::class.java)
            startActivity(intent)
        }
        choose_date_layout.setOnClickListener {
            datePickerAction()
        }
        choose_time_layout.setOnClickListener {
            //do time picking  function here
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

    }
}
