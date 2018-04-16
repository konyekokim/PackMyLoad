package com.chokus.konye.packmyload.activity

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.model.Route
import com.akexorcist.googledirection.util.DirectionConverter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.chokus.konye.packmyload.R
import com.chokus.konye.packmyload.application.MyApplication
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONObject
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener, DirectionCallback {

    private lateinit var mMap: GoogleMap
    private var  location : Location? = null
    private var locationManager : LocationManager? = null
    private var isGPS : Boolean = false
    private var isNetwork : Boolean = false
    private var canGetLocation : Boolean = true
    private var pickupLocation : Boolean = false
    private var destinationLocation :Boolean = false
    private var userLatitude : Double? = null
    private var userLongitude : Double? = null
    private var startLocation : Location? = null
    private var endLocation : Location? = null
    private var startLatLng : LatLng? = null
    private var endLatLng : LatLng? = null
    private var pickUpSelectedAddress : String? = null
    private var destSelectedAddress : String? = null
    private var userCurrentAddress : String? = null
    private var progressDialog : ProgressDialog? = null
    private val URL ="put your URL here"
    companion object {
        val MY_PERMISSION_FINE_LOCATION = 101
        val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.toFloat()
        val MIN_TIME_BETWEEN_UPDATES = 1000*60*1.toLong()
        val PLACE_PICKER_REQUEST = 103
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
        checkNetworkConnection()
        viewActions()
        prepareLocationManager()
        progressDialog = ProgressDialog(this)
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
        //mMap.addMarker(MarkerOptions().position(sydney).title("Somewhere in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        setUpMap()
        //pickup_location_textView.text = userCurrentAddress
    }

    /*private fun userCurrentLocation(){
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }
    }*/

    private fun viewActions(){
        continue_layout.setOnClickListener {
            calculateDistanceBetweenLocations()
        }
        pickup_layout.setOnClickListener {
            pickupLocation = true
            destinationLocation = false
            loadPlacePicker()
        }
        destination_layout.setOnClickListener {
            destinationLocation = true
            pickupLocation = false
            loadPlacePicker()
        }
    }

    private fun checkViews(){
        when{
            pickup_location_textView.text.toString().equals("Enter Location") ->
                    toastMethod("Please enter valid location")
            dest_location_textView.text.toString().equals("Enter Destination") ->
                    toastMethod("Please enter valid Destination")
            else -> sendData()
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
            //placeMarkerOnMap(userLatitude!!,userLongitude!!)
            //userCurrentAddress = getAddress(userLatitude!!, userLongitude!!)
        }else{
            Log.d("Connection Availble", "Connection on")
            //check permissions
            getLocation()
            //placeMarkerOnMap(userLatitude!!,userLongitude!!)
            //userCurrentAddress = getAddress(userLatitude!!, userLongitude!!)
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
                        val intent = Intent(this, MapsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        overridePendingTransition(0,0)
                    }else{
                        toastMethod("This application requires location permission")
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

    private fun getAddress(placeLat : Double, placeLong : Double) : String{
        val geocoder = Geocoder(this)
        val addresses : List<Address>?
        val address : Address?
        var addressText = ""

        try{
            addresses = geocoder.getFromLocation(placeLat,placeLong,1)
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        }catch (e : IOException){
            Log.e("MapsActivity", e.localizedMessage)
        }
        return addressText
    }

    private fun placeMarkerOnMap(placeLat: Double, placeLong : Double) {
        val markerOptions = MarkerOptions().position(LatLng(placeLat, placeLong))

        val titleStr = getAddress(placeLat, placeLong)  // add these two lines
        markerOptions.title(titleStr)

        mMap.addMarker(markerOptions)
    }

    private fun loadPlacePicker() {
        val builder = PlacePicker.IntentBuilder()
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                var addressText = place.name.toString()
                addressText += "\n" + place.address.toString()
                if(pickupLocation){
                    pickUpSelectedAddress = addressText
                    pickup_location_textView.text = pickUpSelectedAddress
                }
                if(destinationLocation){
                    destSelectedAddress = addressText
                    dest_location_textView.text = destSelectedAddress
                }
                placeMarkerOnMap(place.latLng.latitude, place.latLng.longitude)

            }
        }
    }

    private fun getCoordinatesFromAddress(locationAddress : String) : LatLng?{
        val geocoder : Geocoder = Geocoder(this)
        val address : List<Address>
        var position : LatLng? = null
        try{
            //May throw an IOException
            address = geocoder.getFromLocationName(locationAddress, 5)
            if(address == null){
                return null
            }
            if (address.isNotEmpty()){
                val location : Address = address.get(0)
                position = LatLng(location.latitude, location.longitude)
            }
        }catch (x : IOException){
            x.printStackTrace()
        }
        return position
    }

    private fun getStartAndEndLocation(startLatLng: LatLng, endLatLng: LatLng){
        //for the start location
        startLocation = Location("")
        startLocation!!.latitude(startLatLng.latitude)
        startLocation!!.longitude(startLatLng.longitude)
        //for the end location
        endLocation = Location("")
        endLocation!!.latitude(endLatLng.latitude)
        endLocation!!.longitude(endLatLng.longitude)

    }

    private fun distanceBetweenLocations(startLoc : Location, endLoc : Location) : Float{
      val distance : Float = endLoc.distanceTo(startLoc)/1000
        return distance
    }

    private fun calculateDistanceBetweenLocations(){
        if(pickup_location_textView.text != "Enter Location" && pickup_location_textView.text != ""){
            startLatLng = getCoordinatesFromAddress(pickup_location_textView.text.toString())
        }
        if(dest_location_textView.text != "Enter Destination" && dest_location_textView.text != ""){
            endLatLng = getCoordinatesFromAddress(dest_location_textView.text.toString())
        }
        if(startLatLng != null && endLatLng != null){
            getStartAndEndLocation(startLatLng!!, endLatLng!!)
            //checkViews()
        }
        if(startLocation != null && endLocation != null){
            Toast.makeText(this,
                    "" + distanceBetweenLocations(startLocation!!, endLocation!!).toString() + " km",
                    Toast.LENGTH_LONG).show()
        }
        //remember to remove this block of code  as it is just for testing
        val intent = Intent(applicationContext, SelectYourSizeActivity::class.java)
        startActivity(intent)
    }

    private fun checkLocations(){
        if(startLatLng != null && endLatLng != null){
            requestDirection()
        }
    }

    private fun requestDirection(){
        toastMethod("Loading Direction...")
        GoogleDirection.withServerKey(resources.getResourceName(R.string.google_maps_key))
                .from(startLatLng)
                .to(endLatLng)
                .transportMode(TransportMode.DRIVING)
                .execute(this)
    }

    override fun onDirectionSuccess(direction: Direction?, rawBody: String?) {
        if(direction!!.isOK){
            val route = direction.routeList.get(0)
            mMap.addMarker(MarkerOptions().position(startLatLng!!))
            mMap.addMarker(MarkerOptions().position(endLatLng!!))

            val directionPositionList : ArrayList<LatLng> = route.legList.get(0).directionPoint
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList,5, resources.getColor(R.color.colorPrimaryDark)))
            setCameraWithCoordinationBounds(route)
        }
    }

    override fun onDirectionFailure(t: Throwable?) {
        toastMethod("Error in getting direction between locations")
    }

    private fun setCameraWithCoordinationBounds(route : Route){
        val southwest : LatLng = route.bound.southwestCoordination.coordination
        val northeast : LatLng = route.bound.northeastCoordination.coordination
        val bounds = LatLngBounds(southwest, northeast)
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    private fun sendData(){
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.show()
        val stringRequest = object : StringRequest(Request.Method.POST, URL,
                Response.Listener<String>{ response ->
                    //use this to get hte response from the backend
                    progressDialog!!.dismiss()
                    val obj = JSONObject(response)
                    //Toast.makeText(applicationContext, obj.getString("what ever the string return " +
                    //        "in backend"), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SelectYourSizeActivity::class.java)
                    startActivity(intent)

                }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                progressDialog!!.dismiss()
                toastMethod(error?.message)
            }
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                //need better API to add the information from this activity to the database e.g. shown  below
                //params.put("name", firstName_editText.text.toString())
                return params
            }
        }
        MyApplication.instance?.addToRequestQueue(stringRequest)
    }

    private fun checkNetworkConnection() {
        val backgroundLayout = findViewById(R.id.maps_activity_layout) as RelativeLayout
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            //Snackbar.make(backgroundLayout, "Connection successful", Snackbar.LENGTH_SHORT).show()
            checkLocations()
        } else {
            //we are not connected to a network
            Snackbar.make(backgroundLayout, "Oops! No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY") {
                        val intent = intent
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }.setActionTextColor(resources.getColor(R.color.colorPrimary)).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null) {
            locationManager!!.removeUpdates(this)
        }
    }

    private fun toastMethod(message : String?){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}

private operator fun Int.invoke(maP_TYPE_TERRAIN: Int) {}
private operator fun Double.invoke(latitude: Double) {}
