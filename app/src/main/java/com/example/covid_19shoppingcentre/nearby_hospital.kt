package com.example.covid_19shoppingcentre

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationProvider
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.covid_19shoppingcentre.Common.Common
import com.example.covid_19shoppingcentre.Remote.IGoogleAPIService
import com.example.covid_19shoppingcentre.models.MyPlaces
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_nearby_hospital.*
import retrofit2.Call
import retrofit2.Response
import java.lang.StringBuilder
import java.net.CacheRequest
import java.util.jar.Manifest
import javax.security.auth.callback.Callback

class nearby_hospital : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var latitude : Double = 0.toDouble()
    private var longtitude : Double = 0.toDouble()

    private lateinit var mLastLocation:Location
    private var mMarker: Marker?=null

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object{
        private const val  MY_PERMISSION_CODE: Int = 1000
    }

    lateinit var mService:IGoogleAPIService

    internal lateinit var currentPlace:MyPlaces

    //pass current location lat and lng
    public var  currentLat = 0.0
    public var  currentLng = 0.0

    var currentMember: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_hospital)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
        currentMember = intent.getStringExtra("MemberID")
        //Init Service
        mService = Common.googleApiService
        setActionBar()
        //Request runtime permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkLocationPermission()){
                buildLocationRequest();
                buildLocationCallBack();

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
            }
           }else{
            buildLocationRequest();
            buildLocationCallBack();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());

        }

    }

    private fun nearByPlace(typePlace: String) {
        //Clear all marker on Map
        //mMap.clear()
        //build URL request base on location
        val url = getUrl(latitude,longtitude,typePlace)

        mService.getNearbyPlaces(url)
            .enqueue(object :retrofit2.Callback<MyPlaces> {
                override fun onResponse(call: Call<MyPlaces>?,response: Response<MyPlaces>?) {

                    currentPlace=response!!.body()!!
                    if(response!!.isSuccessful){

                        for(i in 0 until response!!.body()!!.results!!.size)
                        {
                            val markerOptions = MarkerOptions()
                            val googlePlace = response.body()!!.results!![i]
                            val lat = googlePlace.geometry!!.location!!.lat
                            val lng = googlePlace.geometry!!.location!!.lng
                            val placeName = googlePlace.name
                            val latLng = LatLng(lat,lng)

                            markerOptions.position(latLng)
                            markerOptions.title(placeName)
                            if(typePlace.equals("hospital"))
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker())
                            else if(typePlace.equals("market"))
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker())
                            else if(typePlace.equals("restaurant"))
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker())
                            else if(typePlace.equals("school"))
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker())

                            markerOptions.snippet(i.toString())//Assign index for Market
                            //Add marker to map
                            mMap!!.addMarker(markerOptions)
                            //moveCamera
                            //mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            //mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                        }

                    }

                }

                override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                    Toast.makeText(baseContext, ""+t!!.message,Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun getUrl(latitude: Double, longtitude: Double, typePlace: String): String {

        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longtitude")
        googlePlaceUrl.append("&radius=10000") //10km
        googlePlaceUrl.append("&type=$typePlace")
        googlePlaceUrl.append("&key=AIzaSyBTfgVQ6JTfOgGYDTrwiL6opabIEOwXCSs")

        Log.d("URL_DEBUG",googlePlaceUrl.toString())
        return googlePlaceUrl.toString()

    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.lastLocation//Get Last Location

                if(mMarker !=null){
                    mMarker!!.remove()
                }

                latitude = mLastLocation.latitude
                longtitude = mLastLocation.longitude

                val latLng = LatLng(latitude,longtitude)
                //save lat & lng to public variable
                currentLat = latitude
                currentLng = longtitude
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Your position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mMarker = mMap!!.addMarker(markerOptions)

                //Move Camera
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.moveCamera(CameraUpdateFactory.zoomTo(13f))
                nearByPlace("hospital")
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }


    private fun checkLocationPermission(): Boolean {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            else
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            return false
        }else
            return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            MY_PERMISSION_CODE->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        if(checkLocationPermission()){
                            buildLocationRequest();
                            buildLocationCallBack();

                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());

                            mMap!!.isMyLocationEnabled=true
                        }
                }else{
                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback)

        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Init Google Play Services
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                mMap!!.isMyLocationEnabled=true
            }
        }else
            mMap!!.isMyLocationEnabled=true

        //Enable Zoom Control
        mMap.uiSettings.isZoomControlsEnabled=true

        //Make event click on Marker
        mMap!!.setOnMarkerClickListener { marker ->
            if(marker.snippet!=null){
                //When user select marker, just get result of the place assign to static variable
                Common.currentResult = currentPlace!!.results!![Integer.parseInt(marker.snippet)]
                //Start new Activity
                val intent = Intent(this@nearby_hospital,ViewPlace::class.java)
                intent.putExtra("lat", currentLat.toString())
                intent.putExtra("lng",currentLng.toString())
                startActivity(intent)

            }
             true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = intent.getStringExtra("MemberID")

        val intent1 = Intent(this, MainActivity::class.java).apply {
            putExtra("MemberID", id)
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Hospital Near Me"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}