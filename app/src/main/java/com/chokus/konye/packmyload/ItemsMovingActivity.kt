package com.chokus.konye.packmyload

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_items_moving.*
import java.io.File

class ItemsMovingActivity : AppCompatActivity() {
    private var file : File? = null
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
    }

    private fun viewActions(){
        camera_icon.setOnClickListener {
            //remember to add dialog that asks if the want to take photo or take from gallery.
        }
        add_photo_layout.setOnClickListener {
            val intent = Intent(this, PickupDateActivity::class.java)
            startActivity(intent)
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
}
