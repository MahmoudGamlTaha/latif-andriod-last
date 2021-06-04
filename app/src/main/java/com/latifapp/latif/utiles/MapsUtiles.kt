package com.latifapp.latif.utiles

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import com.latifapp.latif.R
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.databinding.CustomMarkserBinding
import com.latifapp.latif.utiles.MapsUtiles.getMarker
import com.latifapp.latif.utiles.picasso.PicassoMarker
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception
import java.util.*


object MapsUtiles {
    fun getMyLocationName(context: Context, latlng: LatLng): MutableLiveData<String> {
        val liveData = MutableLiveData<String>("")
        CoroutineScope(Dispatchers.IO).launch {
            val geocoder: Geocoder = Geocoder(context, Locale("ar"));

            try {
                val addresses: List<Address> =
                    geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
                val obj: Address? = addresses?.get(0)
                // val add = obj?.getSubThoroughfare()+","+obj?.

                var add = obj?.getAddressLine(0)


                Log.e("IGA", "Address" + add)
                liveData.postValue(add)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return liveData
    }

    fun getCityName(context: Context, latlng: LatLng, lang: String): MutableLiveData<String> {
        val liveData = MutableLiveData<String>("")
        CoroutineScope(Dispatchers.IO).launch {
            val geocoder: Geocoder = Geocoder(context, Locale(lang));

            try {
                val addresses: List<Address> =
                    geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val obj: Address? = addresses?.get(0)
                    // val add = obj?.getSubThoroughfare()+","+obj?.

                    var add = obj?.adminArea


                    Log.e("IGA", "Address" + add)
                    liveData.postValue(add)
                }
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return liveData
    }


    fun GoogleMap.getMarker(
        adsModel: AdsModel,
        context: Context,
        layoutInflater: LayoutInflater
    ) {
        val pet = LatLng(adsModel.latitude, adsModel.longitude)
        val iconGenerator = IconGenerator(context)
        val inflatedViewBinding = CustomMarkserBinding.inflate(layoutInflater)
        val imageView = inflatedViewBinding.image
       // addImage(imageView, adsModel.image, context)
        val TRANSPARENT_DRAWABLE: Drawable = ColorDrawable(Color.TRANSPARENT)
        iconGenerator.setBackground(TRANSPARENT_DRAWABLE)
        iconGenerator.setContentView(inflatedViewBinding.root)



        Picasso.get()
            .load(adsModel.image)
            .placeholder(R.drawable.placeholder)
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                    imageView.setImageBitmap(bitmap)
                    var marker = MarkerOptions().position(pet)
                        .title(adsModel.name)
                        .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))

                    val mm = this@getMarker?.addMarker(
                        marker
                    )
                    mm?.tag = (adsModel)
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    Utiles.log_D("onBitmapLoadedE", e )
                    Utiles.log_D("onBitmapLoadedE", adsModel.image )
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
            })
        // customPicasso.shutdown()
        //Picasso.get().load(adsModel.image).into(picassoMarker);
    }

    private fun addImage(imageView: ImageView, image: String?, context: Context) {
        if (!image.isNullOrEmpty()) {
            context?.let {

                Glide.with(it).load("$image")
                    .placeholder(R.drawable.ic_defaultpet).error(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)


            }
            imageView.visibility = View.VISIBLE
        }


    }
}