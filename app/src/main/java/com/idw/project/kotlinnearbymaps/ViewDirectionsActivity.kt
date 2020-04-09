package com.idw.project.kotlinnearbymaps

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.idw.project.kotlinnearbymaps.Common.Common
import com.idw.project.kotlinnearbymaps.Helper.DirectionJSONParser
import com.idw.project.kotlinnearbymaps.Remote.IGoogleAPIService
import dmax.dialog.SpotsDialog
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewDirectionsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var mLastLocation:Location

    lateinit var mCurrentMarker:Marker
    var polyLine:Polyline?=null

    lateinit var mService: IGoogleAPIService

    companion object{
        private const val MY_PERMISSION_CODE: Int = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_directions)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //init service
        mService = Common.googleApiServiceScalars

        //request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkLocationPermission()){
                buildLocationRequest()
                buildLocationCallback()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            }

        }else{
            buildLocationRequest()
            buildLocationCallback()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {location->

            mLastLocation = location

            //add your location to map
            val markerOptions = MarkerOptions()
                .position(LatLng(mLastLocation.latitude, mLastLocation.longitude))
                .title("Your Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            mCurrentMarker = mMap.addMarker(markerOptions)

            //move camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLastLocation.latitude, mLastLocation.longitude)))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10f))

            //create marker for destination location
            val destinationLatlng = LatLng(
                Common.currentResult!!.geometry.location.lat.toDouble(),
                Common.currentResult!!.geometry.location.lng.toDouble())

            mMap.addMarker(MarkerOptions().position(destinationLatlng)
                .title(Common.currentResult!!.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))

            //get directionn
            drawPath(mLastLocation, Common.currentResult!!.geometry.location)
        }

    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), MY_PERMISSION_CODE
                )

            return false
        }else
            return true
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f

    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.lastLocation

                //add your location to map
                val markerOptions = MarkerOptions()
                    .position(LatLng(mLastLocation.latitude, mLastLocation.longitude))
                    .title("Your Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                mCurrentMarker = mMap.addMarker(markerOptions)

                //move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLastLocation.latitude, mLastLocation.longitude)))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10f))

                //create marker for destination location
                val destinationLatlng = LatLng(
                    Common.currentResult!!.geometry.location.lat.toDouble(),
                        Common.currentResult!!.geometry.location.lng.toDouble())

                mMap.addMarker(MarkerOptions().position(destinationLatlng)
                    .title(Common.currentResult!!.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))

                //get directionn
                drawPath(mLastLocation, Common.currentResult!!.geometry.location)
            }
        }

    }

    private fun drawPath(mLastLocation: Location?, location: com.idw.project.kotlinnearbymaps.Model.Location) {
        if (polyLine != null)
            polyLine!!.remove() //remove old directions

        val origin = StringBuilder(mLastLocation!!.latitude.toString())
            .append(",")
            .append(mLastLocation.longitude.toString())
            .toString()

        val destination = StringBuilder(location.lat.toString())
            .append(",")
            .append(location.lng.toString())
            .toString()

        mService.getDirections(origin, destination)
            .enqueue(object :Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("CEK", t.message)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
//                        ParserTask().execute(response.body().toString())
                    Log.d("HAIII ", response.body().toString())
                }

            })
    }

    inner class ParserTask:AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

        internal val waitingDialog: AlertDialog = SpotsDialog(this@ViewDirectionsActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            waitingDialog.show()
            waitingDialog.setMessage("Please Waiting...")
        }

        override fun doInBackground(vararg p0: String?): List<List<HashMap<String, String>>>? {
            val jsonObject:JSONObject
            var routes:List<List<HashMap<String, String>>>? = null

            try {
                jsonObject = JSONObject(p0[0])
                val parser = DirectionJSONParser()
                routes = parser.parse(jsonObject)

            }catch (e:JSONException){
                e.printStackTrace()
            }

            return routes

        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            super.onPostExecute(result)

            var points:ArrayList<LatLng>?=null
            var polylineOptions:PolylineOptions?= null

            for (i in result!!.indices){

                points = ArrayList()
                polylineOptions = PolylineOptions()

                val path = result[i]

                for (j in path.indices){
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position  =LatLng(lat, lng)

                    points.add(position)
                }

                polylineOptions.addAll(points)
                polylineOptions.width(12f)
                polylineOptions.color(Color.RED)
                polylineOptions.geodesic(true)

            }

            polyLine = mMap.addPolyline(polylineOptions)
            waitingDialog.dismiss()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            MY_PERMISSION_CODE ->{
                if (grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        if (checkLocationPermission()){
                            buildLocationRequest()
                            buildLocationCallback()

                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

                            mMap.isMyLocationEnabled = true
                        }

                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }
}
