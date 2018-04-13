package com.chokus.konye.packmyload.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.DrawerLayout
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
import com.chokus.konye.packmyload.models.DrawerClass
import com.chokus.konye.packmyload.models.ServiceClass
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.drawer_list_item.view.*
import kotlinx.android.synthetic.main.grid_item.view.*
import org.json.JSONArray
import org.json.JSONException

class HomePageActivity : AppCompatActivity() {
    var gridAdapter : ServiceGridAdapter? = null
    var listAdapter : DrawerListAdapter? = null
    var serviceList = ArrayList<ServiceClass>()
    var iconList = ArrayList<DrawerClass>()
    private var progressDialog : ProgressDialog? = null
    private val URL = "http://packmyload.com/api/serviceslist"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportActionBar!!.hide()
        checkNetworkConnection()
        viewActions()
        gridViewActions()
        listViewActions()
        progressDialog = ProgressDialog(this)
        getDataForServices()
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
        gridAdapter = ServiceGridAdapter(this, serviceList)
        grid_view.adapter = gridAdapter
        grid_view.setOnItemClickListener { parent, view, position, id ->
            //depending on the API requirement we might need the title  below
            val title : String = serviceList[position].name!!
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
            serviceGridView.service_img.setImageUrl(service.img.toString(), MyApplication.instance?.imageLoader)

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

    private fun getDataForServices(){
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
                            val serviceClass = ServiceClass(jsonObject.getString("title"), jsonObject.getString("image"))
                            serviceList.add(serviceClass)
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
        val backgroundLayout = findViewById(R.id.pml_drawer_layout) as DrawerLayout
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

    private fun getUserTokenId(){
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser?.getIdToken(true)?.addOnCompleteListener(this){ task: Task<GetTokenResult> ->
            if(task.isSuccessful){
                val idToken = task.getResult().token
                // you can add token to backend
            }else{
                //Handle error here..
                toastMethod("Error getting user token Id")
            }
        }
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
