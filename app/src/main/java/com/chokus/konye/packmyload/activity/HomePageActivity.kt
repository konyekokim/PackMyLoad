package com.chokus.konye.packmyload.activity

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
import com.chokus.konye.packmyload.R
import com.chokus.konye.packmyload.servicemodel.ServiceClass
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
        booking_text_view.setOnClickListener {
            //this is just a tester remember to remove it
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun gridViewActions(){
        addGridContent()
        gridAdapter = ServiceGridAdapter(this, serviceList)
        grid_view.adapter = gridAdapter
        grid_view.setOnItemClickListener { parent, view, position, id ->
            //create a function like drawerListActions
            //this one is just a temporary tester
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
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
            toastMethod("This action will take you to the website")
        }
        if(pos==4){
            //become a packer
            toastMethod("This action will take you to the website")
        }
        if(pos==5){
            //send feedback
            toastMethod("This action will take you to the website")
        }
        if(pos==6){
            //review on google play
            toastMethod("This action will take you to play store")
        }
        if(pos==7){
            //have questions?
            toastMethod("This action will take you to play store")
        }
        if(pos==8){
            //about
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addGridContent(){
        //here perform serviceList.add when you are ready
        serviceList.add(ServiceClass("Store Delivery", R.drawable.pml_store_delivery))
        serviceList.add(ServiceClass("Small Move", R.drawable.pml_store_move))
        serviceList.add(ServiceClass("Packer Pickup", R.drawable.pml_packer_pickup))
        serviceList.add(ServiceClass("Storage Move", R.drawable.pml_storage_move))
        serviceList.add(ServiceClass("Donation Pickup", R.drawable.pml_donation_pickup))
        serviceList.add(ServiceClass("Junk Removal", R.drawable.pml_junk_removal))
        serviceList.add(ServiceClass("Food Delivery", R.drawable.pml_food_delivery))
        serviceList.add(ServiceClass("other", R.drawable.pml_other))
    }

    private fun addListContent(){
        //here perform iconList.add when you are ready
        iconList.add(ServiceClass("Your Packs", R.drawable.refresh_arrow))
        iconList.add(ServiceClass("Profile", R.drawable.avatar_inside_a_circle))
        iconList.add(ServiceClass("Payment", R.drawable.credit_card_white))
        iconList.add(ServiceClass("Free Packing", R.drawable.anniversary_gift_box_outline))
        iconList.add(ServiceClass("Become a Packer", R.drawable.dollar_in_circular_button))
        iconList.add(ServiceClass("Review on Google Play", R.drawable.heart))
        iconList.add(ServiceClass("Have Questions?", R.drawable.question_in_circular_button))
        iconList.add(ServiceClass("Help & Support", R.drawable.postcard_or_email_envelope_front))
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

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
