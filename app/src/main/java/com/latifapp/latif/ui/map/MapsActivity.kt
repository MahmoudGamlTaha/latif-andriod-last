package com.latifapp.latif.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.latifapp.latif.R
import com.latifapp.latif.utiles.*
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latLng: LatLng? = null
    private var placeName: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Utiles.setMyLocationPositionInBottom(mapFragment.view)
        turnGPSOn()

        backBtn.setOnClickListener {
            onBackPressed()
        }
        selectBtn.setOnClickListener {
            if (latLng!=null){
                 intent.putExtra("lat",latLng?.latitude)
                 intent.putExtra("lng",latLng?.longitude)
                 intent.putExtra("placeName",placeName)
                setResult(RESULT_OK,intent)
                finish()
            }
        }

    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        if (!Permissions.checkLocationPermissions(this)) {
            Permissions.showPermissionsDialog(
                this,
                "Request Location permission Is Needed",
                Permissions.locationManifestPermissionsList,
                0
            )
        } else {
            mMap.setMyLocationEnabled(true)
        }
        mMap.setOnCameraIdleListener {
            locTxt.setText(R.string.loading)
            latLng = mMap.cameraPosition.target
            MapsUtiles.getMyLocationName(this, latLng!!)
                .observe(this, Observer {
                    if (!it.isNullOrEmpty()) {
                        locTxt.setText("$it")
                        placeName = "$it"

                    }

                })
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == AppConstants.GPS_REQUEST) {
            // flag maintain before get location
        }

    }

    @SuppressLint("MissingPermission")
    private fun turnGPSOn() {
        GpsUtils(this).turnGPSOn { isGPSEnable, mlocation -> // turn on GPS
            if (isGPSEnable)setupmap()

        }
    }

    @SuppressLint("MissingPermission")
    private fun setupmap() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        val task =
            fusedLocationProviderClient.lastLocation
        if (task!=null)
            task.addOnCompleteListener{
                val location=task.result
                if (location != null)
                    moveToLocation(location)
            }
    }

    private fun moveToLocation(location: Location) {
        Utiles.log_D("ddnndndndndn", "${location}\n $mMap")
        mMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location!!.latitude,
                    location!!.longitude
                ), 15f
            )
        )
    }
}