package com.latifapp.latif.data.models

import android.os.Parcelable
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class AdsTypeModel(val id:Int?,val name:String?,val code:String?,val nameAr:String?)
@Parcelize
data class AdsModel(
    val id: Int?,
    val name: String?,
    val nameAr: String?,
    val code: String?,
    val city: String?,
    val created_at: String?,
    val description: String?,
    val short_description: String?,
    val price: String?,
    val image: String?,
    val images: List<ImagesModel>?,
    val breed: String?,
    val stock: String?,
    val latitude: Double=0.0,
    val longitude: Double=0.0,
    val external_link: Boolean=false,
    val createdBy: UserModel?,
    val extra: List<ExtraModel>?
):Parcelable

data class ExtraModel(
    val name: String?,
    val name_ar: String?,
    var value: Any?
):Serializable

@Parcelize
data class ImagesModel(val id:Int,val image:String,val external_link:Boolean=false):Parcelable