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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.databinding.CustomMarkserBinding
import com.latifapp.latif.databinding.FragmentPetsBinding
import com.latifapp.latif.ui.base.BaseFragment
import com.latifapp.latif.ui.main.home.MainActivity
import com.latifapp.latif.ui.main.pets.bottomDialog.BottomDialogFragment
import com.latifapp.latif.ui.map.MapsUtiles
import com.latifapp.latif.ui.sell.SellActivity
import com.latifapp.latif.utiles.*
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class PetsFragment : BaseFragment<PetsViewModel, FragmentPetsBinding>(),
    PetsAdapter.CategoryActions {

    private val mapSets = mutableSetOf<AdsModel>()
    private var category: Int? = null
    private var mapFragment: SupportMapFragment? = null
    private var mMap: GoogleMap? = null
    private val petsAdapter = PetsAdapter()

    companion object {
        var Latitude_ = 0.0
        var Longitude_ = 0.0
    }
    var latitude_map= 0.0
    var longitude_map = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mapFragment == null) {
            mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
            Utiles.setMyLocationPositionInBottom(mapFragment?.view)
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = petsAdapter
                petsAdapter.action = this@PetsFragment
            }

//            binding.pin.setOnClickListener {
//                Utiles.log_D("dndndndndndn", "HERE")
//
//                childFragmentManager.let {
//                    BottomDialogFragment().apply {
//                        show(it, tag)
//                    }
//                }
//            }
            binding.sellBtn.setOnClickListener {
                startActivity(Intent(activity, SellActivity::class.java))
            }

            getCategoriesList()

        }
    }




    private fun getPetsList() {
        var distance =20_000f
        if (latitude_map!=0.0){
            // get distance
            val loc1=Location("loc1")
            loc1.latitude= Latitude_
            loc1.longitude= Longitude_

            val lo2=Location("loc2")
            lo2.latitude= latitude_map
            lo2.longitude= longitude_map

           distance= loc1.distanceTo(lo2)

            latitude_map= Latitude_
            longitude_map= Longitude_
        }

        if (distance>=20_000)
            lifecycleScope.launchWhenStarted {
                viewModel.getItems(AppConstants.PETS_STR, category).collect {
                    viewModel.page = 0
                    if (it != null) {
                        val set = mutableSetOf<AdsModel>()
                        set.addAll(it)
                         var list = mapSets
                        //  if list empty so mapSets is empty and it the first action on map
                        Utiles.log_D("ncncncnncncncn", "${list.size} \n ${list.minus(set)}")
                        if (list.isEmpty()) {
                            list = set
                            mapSets.addAll(list)
                        }else // if not so compare old list with new list
                            list= list.minus(set) as MutableSet<AdsModel>

                        Utiles.log_D("ncncncnncncncn", "${list.size} \n }")
                        if (!list.isEmpty()) {
                            mapSets.addAll(list)
                            setLPetsLocations(mapSets)
                        }
                    }
                }
            }
    }

    private fun getCategoriesList() {
        lifecycleScope.launchWhenStarted {
            viewModel.getCategoriesList(AppConstants.PETS).collect {
                if (!it.isNullOrEmpty()) {
                    petsAdapter.list.addAll(it)
                    petsAdapter.notifyDataSetChanged()
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
            var changedCity=true
            if (latLng != null && latLng.latitude != 0.0) {
                Latitude_ = latLng.latitude
                Longitude_ = latLng.longitude
                 getCityName_(latLng)
               // if(changedCity==true)

            }
        }
    }

    private fun getCityName_(latLng: LatLng) {
        var Changed=false;
        MapsUtiles.getCityName(requireContext(), latLng, "en").observe(
            viewLifecycleOwner,
            Observer {
                if (!it.isNullOrEmpty()) {
                    val city = it.replace("Governorate", "")
                     if(( activity as MainActivity).toolBarTitle.text.equals(city)==false)
                     {
                         Changed=true;
                         getPetsList()

                     }
                    (activity as MainActivity).toolBarTitle.text = city

                }
            })

    }

    fun setLPetsLocations(list: Set<AdsModel>) {
        if (mMap!=null) {
            activity?.runOnUiThread(Runnable {
                // mMap?.clear()
                list.forEach { adsModel ->
                    val pet = LatLng(adsModel.latitude, adsModel.longitude)
                    val iconGenerator = IconGenerator(context)
                    val inflatedViewBinding = CustomMarkserBinding.inflate(layoutInflater)
                    val imageView = inflatedViewBinding.image
                    addImage(imageView, adsModel.image)
                    val TRANSPARENT_DRAWABLE: Drawable = ColorDrawable(Color.TRANSPARENT)
                    iconGenerator.setBackground(TRANSPARENT_DRAWABLE)
                    iconGenerator.setContentView(inflatedViewBinding.root)


                    var marker = MarkerOptions().position(pet)
                        .title(adsModel.name)
                        .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))// below line is use to add custom marker on our map.
                    val mm = mMap?.addMarker(
                        marker
                    )
                    mm?.tag = (adsModel)
                    val picassoMarker = PicassoMarker(mm, iconGenerator, imageView)
                    Picasso.get().load(adsModel.image).into(picassoMarker);

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

    private fun addImage(imageView: ImageView, image: String?) {
        if (!image.isNullOrEmpty()) {
            context?.let {

                Glide.with(it).load("$image")
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)

            }
            imageView.visibility = View.VISIBLE
        }


    }

    override fun onResume() {
        super.onResume()
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


            }
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentPetsBinding {
        return FragmentPetsBinding.inflate(inflater)
    }

    override fun showLoader() {

    }

    override fun hideLoader() {

    }

    override fun selectedCategory(id: Int?) {
        category = id
        mMap?.clear()
        mapSets.clear()
        getPetsList()
    }


}
