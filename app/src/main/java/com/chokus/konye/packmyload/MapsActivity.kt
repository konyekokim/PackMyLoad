package com.chokus.konye.packmyload

import android.Manifest
import android.app.AlertDialog
import android.app.Service
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.security.Provider

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener{

    private lateinit var mMap: GoogleMap
    private var  location : Location? = null
    private var locationManager : LocationManager? = null
    private var isGPS : Boolean = false
    private var isNetwork : Boolean = false
    private var canGetLocation : Boolean = true
    private var userLatitude : Double? = null
    private var userLongitude : Double? = null
    companion object {
        val MY_PERMISSION_FINE_LOCATION = 101
        val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.toFloat()
        val MIN_TIME_BETWEEN_UPDATES = 1000*60*1.toLong()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.map_activity_name)
        viewActions()
        prepareLocationManager()
    }

    override fun onLocationChanged(location: Location?) {
        // update location details and animate to the current location of the user
        userLatitude = location!!.latitude
        userLongitude = location.longitude
        animatedCamera(positionFunction(userLatitude!!,userLongitude!!),25f,0f,0f)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        //leave empty for now
    }

    override fun onProviderEnabled(provider: String?) {
        //put getLocation() here
        getLocation()
    }

    override fun onProviderDisabled(provider: String?) {
        if(locationManager != null){
            locationManager!!.removeUpdates(this)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.mapType(GoogleMap.MAP_TYPE_TERRAIN)
        //userCurrentLocation()
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Somewhere in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        setUpMap()
    }

    private fun userCurrentLocation(){
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }
    }

    private fun viewActions(){
        continue_layout.setOnClickListener {
            val intent = Intent(this, SelectYourSizeActivity::class.java)
            startActivity(intent)
        }
        pickup_layout.setOnClickListener {
            val intent = Intent(this, SelectLocationActivity::class.java)
            startActivity(intent)
        }
        destination_layout.setOnClickListener {
            val intent = Intent(this, SelectLocationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun prepareLocationManager(){
        locationManager = getSystemService(Service.LOCATION_SERVICE) as LocationManager
        isGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetwork = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(!isGPS && !isNetwork){
            Log.d("No Connection","Connection off")
            showSettingsAlert()
            getLastLocation()
        }else{
            Log.d("Connection Availble", "Connection on")
            //check permissions
            getLocation()
        }
    }

    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_FINE_LOCATION)
            }
        }
    }


    private fun getLocation(){
        try {
            if (canGetLocation) {
                Log.d("checking", "Can get location")
                if (isGPS) {
                    // from GPS
                    Log.d("checking", "GPS on")
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)

                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (location != null){
                            //get lat and long here
                            userLatitude = location!!.latitude
                            userLongitude = location!!.longitude
                        }
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d("checking", "NETWORK_PROVIDER on")
                    locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)

                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null){
                            userLatitude = location!!.latitude
                            userLongitude = location!!.longitude
                        }
                    }
                } else {
                    location!!.latitude = 0.toDouble()
                    location!!.longitude = 0.toDouble()
                    //set Lat and Long to 0...there for the userLat and userLong
                }
            } else {
                Log.d("fault", "Can't get location")
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

    }

    private fun getLastLocation(){
        try{
            val criteria : Criteria = Criteria()
            val provider : String = locationManager!!.getBestProvider(criteria,false)
            val location : Location = locationManager!!.getLastKnownLocation(provider)
            //here use the location gotten above to get last known location
            userLatitude = location.latitude
            userLongitude = location.longitude
        }catch (e: SecurityException){
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            MY_PERMISSION_FINE_LOCATION -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        canGetLocation = true
                    }else{
                        Toast.makeText(this,"This application requires location permission",Toast.LENGTH_LONG).show()
                        canGetLocation = false
                        finish()
                    }
                }
            }
        }
    }

    private fun positionFunction(latitude : Double, longitude : Double) : LatLng{
        return(LatLng(latitude,longitude))
    }

    private fun animatedCamera(position: LatLng, zoom: Float, bearing: Float, tilt: Float){
        val cameraPosition = CameraPosition.Builder()
                .target(position)
                .zoom(zoom)
                .bearing(bearing)
                .tilt(tilt)
                .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun showSettingsAlert(){
        val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("GPS is not Enabled")
        alertDialog.setMessage("Do you want to turn on GPS?")
        alertDialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        })
        alertDialog.setNegativeButton("No",DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null) {
            locationManager!!.removeUpdates(this);
        }
    }

}
