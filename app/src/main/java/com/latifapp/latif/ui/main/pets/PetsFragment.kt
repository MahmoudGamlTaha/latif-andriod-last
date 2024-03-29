package com.latifapp.latif.ui.main.pets

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import com.latifapp.latif.R
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.databinding.CustomMarkserBinding
import com.latifapp.latif.databinding.FragmentPetsBinding
import com.latifapp.latif.ui.auth.login.LoginActivity
import com.latifapp.latif.ui.base.BaseFragment
import com.latifapp.latif.ui.main.blogs.blogsDetails.BolgDetailsViewModel
import com.latifapp.latif.ui.main.home.MainActivity
import com.latifapp.latif.ui.main.home.MainViewModel
import com.latifapp.latif.ui.main.pets.bottomDialog.BottomDialogFragment
import com.latifapp.latif.utiles.MapsUtiles
import com.latifapp.latif.ui.sell.SellActivity
import com.latifapp.latif.utiles.*
import com.latifapp.latif.utiles.MapsUtiles.getMarker
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class PetsFragment : BaseFragment<MainViewModel, FragmentPetsBinding>() {
    override val viewModel by activityViewModels<MainViewModel>()
    private val mapSets = mutableSetOf<AdsModel>()
    private var category: Int? = null
    private var mapFragment: SupportMapFragment? = null
    private var mMap: GoogleMap? = null
    private var isGpsTurned = false

    private var categoryType = 0
    private var itemsType = ""
    private var listOfPetsIsEmpty = false

    companion object {
        var Latitude_ = 0.0
        var Longitude_ = 0.0
    }

    var latitude_map = 0.0
    var longitude_map = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mapFragment == null) {
            mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
            Utiles.setMyLocationPositionInBottom(mapFragment?.view)
            binding.sellBtn.setOnClickListener {
                if (AppPrefsStorage.token.isNullOrEmpty())
                    startActivity(Intent(activity, LoginActivity::class.java))
                else
                    startActivity(Intent(activity, SellActivity::class.java))
            }
            getTypeOfCategorAndItems()


        }
    }

    private fun getTypeOfCategorAndItems() {
        lifecycleScope.launchWhenStarted {
            viewModel.typeFlow.collect {
                categoryType = it.categoryType
                itemsType = it.itemType

                selectedCategory(-1)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.categoryFlow.collect {
                selectedCategory(it)
            }
        }
    }


    private fun getPetsList() {
        var distance = 50_000f
        if (latitude_map != 0.0) {
            // get distance
            val loc1 = Location("loc1")
            loc1.latitude = Latitude_
            loc1.longitude = Longitude_

            val lo2 = Location("loc2")
            lo2.latitude = latitude_map
            lo2.longitude = longitude_map

            distance = loc1.distanceTo(lo2)

            latitude_map = Latitude_
            longitude_map = Longitude_
        }
        if (distance >= 50_000)
            viewModel.page = 0
        else {
            if (listOfPetsIsEmpty)
                return
            viewModel.page++
        }
        lifecycleScope.launchWhenStarted {

            viewModel.getItems(itemsType, category).collect {

                if (it != null) {
                    if (it.isEmpty())
                        listOfPetsIsEmpty = true
                    else      listOfPetsIsEmpty = false
                    val set = mutableSetOf<AdsModel>()
                    set.addAll(it)
                    var list = mutableSetOf<AdsModel>()

                    if (!mapSets.isEmpty()) {
                        for (model in set) {
                            if (!mapSets.contains(model)) {
                                list.add(model)
                            }
                        }
                    } else {
                        list = set

                    }
                    mapSets.addAll(set)
                    Utiles.log_D("ncncncnncncncn", "${list.size}")
                    Utiles.log_D("ncncncnncncncn", "${it.size}")
                    if (!list.isEmpty()) {
                        setLPetsLocations(list)
                    }
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun setupmap() {

        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val task =
            fusedLocationProviderClient.lastLocation

        task.addOnCompleteListener {
            val location = task.result
            if (location != null)
                moveToLocation(location)
        }


    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap

        if (!Permissions.checkLocationPermissions(requireActivity())) {
            Permissions.showPermissionsDialog(
                activity,
                "Request Location permission Is Needed",
                Permissions.locationManifestPermissionsList,
                0
            )
        } else {
            mMap?.setMyLocationEnabled(true)
            //setupmap()
        }

        mMap?.setOnCameraIdleListener {
            var latLng = mMap?.cameraPosition?.target
             if (latLng != null && latLng.latitude != 0.0) {
                Latitude_ = latLng.latitude
                Longitude_ = latLng.longitude
                getCityName_(latLng)
                getPetsList()
            }
        }
    }

    private fun getCityName_(latLng: LatLng) {
        var Changed = false;
        MapsUtiles.getCityName(requireContext(), latLng, "en").observe(
            viewLifecycleOwner,
            Observer {
                if (!it.isNullOrEmpty()) {
                    val city = it.replace("Governorate", "")
                    if ((activity as MainActivity).toolBarTitle.text.equals(city) == false) {
                        Changed = true;


                    }
                    (activity as MainActivity).toolBarTitle.text = city

                }

            })

    }

    fun setLPetsLocations(list: Set<AdsModel>) {
        if (mMap != null) {
            activity?.runOnUiThread(Runnable {
                list.forEach { adsModel ->
                    mMap?.getMarker(adsModel, requireContext(), layoutInflater)
                    mMap?.setOnMarkerClickListener { marker ->
                        val model = marker.tag as AdsModel
                        if (model != null) {
                            val bundle = Bundle()
                            bundle.putParcelable("model", model)
                            childFragmentManager.let {
                                BottomDialogFragment().apply {
                                    arguments = bundle
                                    show(it, tag)
                                }
                            }
                        }
                        true
                    }
                }

            })
        }

    }


    override fun onResume() {
        super.onResume()
        Utiles.log_D("mgmgmmgmg", isGpsTurned)
        if (!isGpsTurned)
            turnGPSOn()
        mapFragment?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapFragment?.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapFragment?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapFragment?.onDestroy()
    }

    private fun moveToLocation(location: Location?) {
        if (location != null) {
            Utiles.log_D("ddnndndndndn", "${location}\n $mMap")
            mMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location!!.latitude,
                        location!!.longitude
                    ), 12f
                )
            )
        }
    }


    private fun turnGPSOn() {
        if (!Permissions.checkLocationPermissions(requireContext())) {
            Permissions.showPermissionsDialog(
                requireContext(),
                "Request Location permission Is Needed",
                Permissions.locationManifestPermissionsList,
                0
            )
        } else
            GpsUtils(activity).turnGPSOn { isGPSEnable, mlocation -> // turn on GPS
                Utiles.log_D("fnnfnfnfnnf", isGPSEnable)
                if (isGPSEnable)
                    setupmap()
                isGpsTurned = isGPSEnable


            }
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentPetsBinding {
        return FragmentPetsBinding.inflate(inflater)
    }

    override fun showLoader() {

    }

    override fun hideLoader() {

    }

    private fun selectedCategory(id: Int) {
        category = if (id == -1) null else id
        mMap?.clear()
        mapSets.clear()
        viewModel.page = -1
        listOfPetsIsEmpty=false
        getPetsList()
    }


}
