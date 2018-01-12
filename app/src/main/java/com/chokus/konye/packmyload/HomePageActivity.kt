package com.chokus.konye.packmyload

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.GridView
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.grid_item.view.*

class HomePageActivity : AppCompatActivity() {
    var adapter : ServiceGridAdapter? = null
    var serviceList= ArrayList<ServiceClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportActionBar!!.hide()

        adapter = ServiceGridAdapter(this, serviceList)
        grid_view.adapter = adapter
    }

    private fun addGridContent(){
        //here perform serviceList.add when you are ready
    }

    class ServiceGridAdapter : BaseAdapter{
        var serviceList = ArrayList<ServiceClass>()
        var context: Context

        constructor(context: Context, serviceList: ArrayList<ServiceClass>): super(){
            this.context = context
            this.serviceList = serviceList
        }

        override fun getItem(p0: Int): Any {
            return serviceList[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
           return serviceList.size
        }

        @SuppressLint("ViewHolder")
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val service = this.serviceList[p0]
            val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val serviceGridView = inflator.inflate(R.layout.grid_item, null)
            serviceGridView.service_name_textView.text = service.name
            serviceGridView.service_img.setImageResource(service.img!!)

            return serviceGridView
        }

    }

}
