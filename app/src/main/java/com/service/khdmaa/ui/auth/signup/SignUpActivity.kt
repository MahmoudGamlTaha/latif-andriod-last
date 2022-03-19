package com.service.khdmaa.ui.auth.signup

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.Navigation
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.service.khdmaa.R
import com.service.khdmaa.databinding.ActivitySignUpBinding
import com.service.khdmaa.ui.base.BaseActivity
import com.service.khdmaa.utiles.GpsUtils
import com.service.khdmaa.utiles.MyContextWrapper
import com.service.khdmaa.utiles.Permissions
import com.service.khdmaa.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<SignUpViewModel, ActivitySignUpBinding>() {


    private var isGpsTurned: Boolean = false

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        Navigation.findNavController(
            this,
            R.id.fragment_container
        )
        binding.backBtn.setOnClickListener({
            onBackPressed()
        })

        viewModel.getPolices()

        var mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

    }

    override fun onResume() {
        super.onResume()
        Utiles.log_D("mgmgmmgmg", isGpsTurned)
        if (!isGpsTurned)
            turnGPSOn()
    }
   override  fun checkReg() : Boolean{
       return false;
   }
    private fun turnGPSOn() {
        if (!Permissions.checkLocationPermissions(this)) {
            Permissions.showPermissionsDialog(
                this,
                "Request Location permission Is Needed",
                Permissions.locationManifestPermissionsList,
                0
            )
        } else
            GpsUtils(this).turnGPSOn { isGPSEnable, mlocation -> // turn on GPS
                Utiles.log_D("fnnfnfnfnnf", isGPSEnable)
                if (isGPSEnable)
                    setLocation()
                isGpsTurned = isGPSEnable


            }
    }

    @SuppressLint("MissingPermission")
    private fun setLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        val task = fusedLocationProviderClient.lastLocation

        task.addOnCompleteListener {
            val location = task.result
            if (location != null) {
                viewModel.lat = location.latitude
                viewModel.lag = location.longitude
            }
        }
    }


    override fun setBindingView(inflater: LayoutInflater): ActivitySignUpBinding {
        return ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }
}