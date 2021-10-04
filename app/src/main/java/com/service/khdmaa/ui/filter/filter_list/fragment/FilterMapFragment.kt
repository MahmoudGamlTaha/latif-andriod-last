package com.service.khdmaa.ui.filter.filter_list.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.service.khdmaa.R
import com.service.khdmaa.data.models.AdsModel
import com.service.khdmaa.databinding.FragmentPetsBinding
import com.service.khdmaa.ui.filter.filter_list.FilterListViewModel
import com.service.khdmaa.ui.main.pets.PetsFragment.Companion.Latitude_
import com.service.khdmaa.ui.main.pets.PetsFragment.Companion.Longitude_
import com.service.khdmaa.ui.main.pets.bottomDialog.BottomDialogFragment
import com.service.khdmaa.ui.sell.CreationFormFragment.Companion.Latitude_Filter
import com.service.khdmaa.ui.sell.CreationFormFragment.Companion.Longitude_Filter
import com.service.khdmaa.utiles.MapsUtiles
import com.service.khdmaa.utiles.MapsUtiles.getMarker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilterMapFragment : Fragment() {
    private var mMap: GoogleMap? = null
    @Inject
    lateinit var viewModel: FilterListViewModel
    lateinit var binding: FragmentPetsBinding
    private var mapFragment: SupportMapFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPetsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mapFragment == null) {

            mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)


            binding.sellBtn.visibility = GONE


        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap?.clear()
        viewModel.flow_.observe(viewLifecycleOwner, Observer {
            if (!it.response.data.isNullOrEmpty()) {
                setLPetsLocations(it.response.data)
            }
        })

        mMap?.animateCamera(

            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    Latitude_Filter,
                    Longitude_Filter
                ), 10f
            )
        )
    }


    fun setLPetsLocations(list: List<AdsModel>) {
        if (mMap != null) {
            activity?.runOnUiThread(Runnable {
                // mMap?.clear()
                list.forEach { adsModel ->
                   mMap?.getMarker(adsModel,requireContext(),layoutInflater)

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
}