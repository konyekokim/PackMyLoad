package com.chokus.konye.packmyload.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.chokus.konye.packmyload.R
import com.chokus.konye.packmyload.application.MyApplication
import com.chokus.konye.packmyload.servicemodel.DrawerClass
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.drawer_list_item.view.*
import kotlinx.android.synthetic.main.grid_item.view.*
import org.json.JSONArray

class HomePageActivity : AppCompatActivity() {
    var gridAdapter : ServiceGridAdapter? = null
    var listAdapter : DrawerListAdapter? = null
    var serviceList= ArrayList<DrawerClass>()
    var iconList = ArrayList<DrawerClass>()
    private var progressDialog : ProgressDialog? = null
    private val URL = "put in API string here"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportActionBar!!.hide()
        viewActions()
        gridViewActions()
        listViewActions()
        progressDialog = ProgressDialog(this)
    }

    private fun viewActions(){
        user_img.setOnClickListener {
            pml_drawer_layout.openDrawer(home_page_drawer_layout)
        }
        gift_img.setOnClickListener {
            toastMethod("This action will take you to the website")
        }
    }

    private fun gridViewActions(){
        addGridContent()
        gridAdapter = ServiceGridAdapter(this, serviceList)
        grid_view.adapter = gridAdapter
        grid_view.setOnItemClickListener { parent, view, position, id ->
            //depending on the API requirement we might need the title  below
            val title : String = serviceList[position].name!!
            //sendData()
            //remember to remove this block of code as it is  just for testing
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
        serviceList.add(DrawerClass("Store Delivery", R.drawable.pml_store_delivery))
        serviceList.add(DrawerClass("Small Move", R.drawable.pml_store_move))
        serviceList.add(DrawerClass("Packer Pickup", R.drawable.pml_packer_pickup))
        serviceList.add(DrawerClass("Storage Move", R.drawable.pml_storage_move))
        serviceList.add(DrawerClass("Donation Pickup", R.drawable.pml_donation_pickup))
        serviceList.add(DrawerClass("Junk Removal", R.drawable.pml_junk_removal))
        serviceList.add(DrawerClass("Food Delivery", R.drawable.pml_food_delivery))
        serviceList.add(DrawerClass("other", R.drawable.pml_other))
    }

    private fun addListContent(){
        //here perform iconList.add when you are ready
        iconList.add(DrawerClass("Your Packs", R.drawable.refresh_arrow))
        iconList.add(DrawerClass("Profile", R.drawable.avatar_inside_a_circle))
        iconList.add(DrawerClass("Payment", R.drawable.credit_card_white))
        iconList.add(DrawerClass("Free Packing", R.drawable.anniversary_gift_box_outline))
        iconList.add(DrawerClass("Become a Packer", R.drawable.dollar_in_circular_button))
        iconList.add(DrawerClass("Review on Google Play", R.drawable.heart))
        iconList.add(DrawerClass("Have Questions?", R.drawable.question_in_circular_button))
        iconList.add(DrawerClass("Help & Support", R.drawable.postcard_or_email_envelope_front))
        iconList.add(DrawerClass("About", R.drawable.information_circular_button_symbol))
    }

    class ServiceGridAdapter : BaseAdapter{
        var serviceList = ArrayList<DrawerClass>()
        var context: Context

        constructor(context: Context, serviceList: ArrayList<DrawerClass>): super(){
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
            serviceGridView.service_img.setImageUrl(service.img.toString(), VolleyS)

            return serviceGridView
        }
    }

    class DrawerListAdapter : BaseAdapter{
        var iconList = ArrayList<DrawerClass>()
        var context: Context

        constructor(context: Context, iconList: ArrayList<DrawerClass>){
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

    private fun sendData(){
        progressDialog!!.setMessage("Loading")
        progressDialog!!.show()
        val request = JsonArrayRequest(Request.Method.POST, URL, null,
                Response.Listener<JSONArray>{ response ->
                    //use this to get hte response from the backend
                    progressDialog!!.dismiss()
                    //val obj = JSONObject(response)
                    //Toast.makeText(applicationContext, obj.getString("what ever the string return " +
                    //        "in backend"), Toast.LENGTH_SHORT).show()
                    //process the JSON
                    var count = 0
                    while(count < response.length()){
                        val jsonObject = response.getJSONObject(count)
                        //add the object for the Serviceclass
                        count++
                    }
                    /*val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)*/

                },  Response.ErrorListener{
            //put in whatever error message you like here.
        })
        MyApplication.instance?.addToRequestQueue(request)
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
