package com.chokus.konye.packmyload

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.drawer_list_item.view.*
import kotlinx.android.synthetic.main.grid_item.view.*

class HomePageActivity : AppCompatActivity() {
    var gridAdapter : ServiceGridAdapter? = null
    var listAdapter : DrawerListAdapter? = null
    var serviceList= ArrayList<ServiceClass>()
    var iconList = ArrayList<ServiceClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportActionBar!!.hide()
        viewActions()
        gridViewActions()
        listViewActions()
    }

    private fun viewActions(){
        user_img.setOnClickListener {
            pml_drawer_layout.openDrawer(home_page_drawer_layout)
        }
        gift_img.setOnClickListener {
            Toast.makeText(this,"This action will take you to the website",Toast.LENGTH_LONG).show()
        }
    }

    private fun gridViewActions(){
        gridAdapter = ServiceGridAdapter(this, serviceList)
        grid_view.adapter = gridAdapter
    }

    private fun listViewActions(){
        addListContent()
        listAdapter = DrawerListAdapter(this, iconList)
        left_drawer_listView.adapter = listAdapter
        left_drawer_listView.setOnItemClickListener { adapterView, view, i, l ->
            drawerListActions(i)
        }
    }

    private fun drawerListActions(pos : Int){
        if(pos == 0){
            //history
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        if(pos == 1){
            //profile
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        if(pos == 2){
            //payment
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
        if(pos==3){
            //free pml
            Toast.makeText(this,"This action will take you to the website",Toast.LENGTH_LONG).show()
        }
        if(pos==4){
            //become a packer
            Toast.makeText(this,"This action will take you to the website",Toast.LENGTH_LONG).show()
        }
        if(pos==5){
            //send feedback
            Toast.makeText(this,"This action will take you to the website",Toast.LENGTH_LONG).show()
        }
        if(pos==6){
            //review on google play
            Toast.makeText(this,"This action will take you to play store",Toast.LENGTH_LONG).show()
        }
        if(pos==7){
            //have questions?
            Toast.makeText(this,"This action will take you to play store",Toast.LENGTH_LONG).show()
        }
        if(pos==8){
            //about
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addGridContent(){
        //here perform serviceList.add when you are ready
    }

    private fun addListContent(){
        //here perform iconList.add when you are ready
        iconList.add(ServiceClass("Your PML", R.drawable.refresh_arrow))
        iconList.add(ServiceClass("Profile", R.drawable.avatar_inside_a_circle))
        iconList.add(ServiceClass("Payment", R.drawable.credit_card_white))
        iconList.add(ServiceClass("Free PML", R.drawable.anniversary_gift_box_outline))
        iconList.add(ServiceClass("Become a Packer", R.drawable.dollar_in_circular_button))
        iconList.add(ServiceClass("Send Feedback", R.drawable.postcard_or_email_envelope_front))
        iconList.add(ServiceClass("Review on Google Play", R.drawable.heart))
        iconList.add(ServiceClass("Have Questions?", R.drawable.question_in_circular_button))
        iconList.add(ServiceClass("About", R.drawable.information_circular_button_symbol))
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

    class DrawerListAdapter : BaseAdapter{
        var iconList = ArrayList<ServiceClass>()
        var context: Context

        constructor(context: Context, iconList: ArrayList<ServiceClass>){
            this.iconList = iconList
            this.context = context
        }

        override fun getCount(): Int {
            return iconList.size
        }

        override fun getItem(p0: Int): Any {
            return iconList[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val icon = this.iconList[p0]
            val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val iconListView = inflator.inflate(R.layout.drawer_list_item,null)
            iconListView.icon_imageView.setImageResource(icon.img!!)
            iconListView.icon_nameTextView.text = icon.name

            return iconListView
        }

    }

}
