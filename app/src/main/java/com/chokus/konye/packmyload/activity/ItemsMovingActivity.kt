package com.chokus.konye.packmyload.activity

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.chokus.konye.packmyload.R
import com.chokus.konye.packmyload.application.MyApplication
import kotlinx.android.synthetic.main.activity_items_moving.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class ItemsMovingActivity : AppCompatActivity() {
    private var file : File? = null
    private var progressDialog : ProgressDialog? = null
    private val URL = "put in your string here"
    companion object {
        val MY_REQUEST_CAMERA = 101
        val MY_REQUEST_WRITE_CAMERA = 102
        val CAPTURE_CAMERA = 103

        val MY_REQUEST_READ_GALLERY = 104
        val MY_REQUEST_WRITE_GALLERY = 105
        val MY_REQUEST_GALLERY = 106
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_moving)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.items_moving_activity)
        viewActions()
        progressDialog = ProgressDialog(this)
    }

    private fun viewActions(){
        camera_icon.setOnClickListener {
            pickImageSourceDialog()
        }
        add_photo_layout.setOnClickListener {
            //checkViews()
        }
    }

    private fun checkViews(){
        when{
            about_item_editText.text.toString().isEmpty() -> toastMethod("please enter description of item")
            else -> sendData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //with when we select the request code we asked for in the StartActivityForResult
        when(requestCode){
            CAPTURE_CAMERA ->
                    item_imgView.setImageURI(Uri.parse("file:///" + file))

            MY_REQUEST_GALLERY ->
                    getImageFromGallery(data)
        }
    }

    private fun getImageFromGallery(data: Intent?){
        try {
            val inputStream = contentResolver.openInputStream(data?.data)
            file = getFile()
            val fileOutputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var bytesRead : Int
            while (true){
                bytesRead = inputStream.read(buffer)
                if (bytesRead == -1) break
                fileOutputStream.write(buffer, 0, bytesRead)
            }
            fileOutputStream.close()
            inputStream.close()
            item_imgView.setImageURI(Uri.parse("file:///" + file))
        }catch (e : Exception){
            Log.e("", "Error while creating temp file", e)
        }
    }

    private fun checkPermissionCamera(){
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.CAMERA), MY_REQUEST_CAMERA)
        }else{
            //do something about camera here
            catchPhoto()
        }
    }

    private fun checkPermissionCWES(){
        //CWES meaning camera write external storage
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_REQUEST_WRITE_CAMERA)
        }else{
            // check permission for camera cos there is already permission to write to external storage
            checkPermissionCamera()
        }
    }

    private fun catchPhoto(){
        file = getFile()
        if (file != null){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try{
                //val photoUri = Uri.fromFile(file)
                val photoUri = FileProvider.getUriForFile(this,this.applicationContext.packageName + ".my.package.name.provider", file!!)
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,photoUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivityForResult(intent, CAPTURE_CAMERA)
            }catch (e : ActivityNotFoundException){
                //do nothing
            }
        }else{
            toastMethod("please check your sd card")
        }
    }

    fun getFile() : File?{
        val fileDir = File(""+ Environment.getExternalStorageDirectory() + "/Android/data/" + applicationContext.packageName + "/Files")
        if(!fileDir.exists()){
            if(!fileDir.mkdir()){
                return null
            }
        }
        val mediaFile = File(fileDir.path + File.separator + "temp.jpg")
        return mediaFile
    }

    private fun checkPermissionRG(){
        //RG meaning read gallery
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), MY_REQUEST_READ_GALLERY)
        }else{
            checkPermissionWG()
        }
    }

    private fun checkPermissionWG(){
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_REQUEST_WRITE_GALLERY)
        }else{
            getPhotos()
        }
    }

    private fun getPhotos(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, MY_REQUEST_GALLERY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            MY_REQUEST_CAMERA -> catchPhoto()
            MY_REQUEST_WRITE_CAMERA -> checkPermissionCamera()
            MY_REQUEST_READ_GALLERY -> checkPermissionWG()
            MY_REQUEST_WRITE_GALLERY -> getPhotos()
        }
    }

    private fun pickImageSourceDialog(){
        val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Pick Image Source")
        alertDialog.setMessage("Where do you want to Select Item Image  from?")
        alertDialog.setPositiveButton("Take Photo", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
            checkPermissionCWES()
        })
        alertDialog.setNegativeButton("Gallery", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
            checkPermissionRG()
        })
        val dialog : AlertDialog = alertDialog.create()
        dialog.show()

        //get the alertDialog button references
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        //change color of button text
        positiveButton.setTextColor(resources.getColor(R.color.colorPrimary))
        negativeButton.setTextColor(resources.getColor(R.color.colorPrimary))
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
                    val intent = Intent(this, PickupDateActivity::class.java)
                    startActivity(intent)

                }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                progressDialog!!.dismiss()
                toastMethod(error?.message)
                Toast.makeText(applicationContext, error?.message, Toast.LENGTH_SHORT).show()
            }
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                //need better API to add the information from this activity to the database e.g. shown  below
                //params.put("name", firstName_editText.text.toString())
                return super.getParams()
            }
        }
        MyApplication.instance?.addToRequestQueue(stringRequest)
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
