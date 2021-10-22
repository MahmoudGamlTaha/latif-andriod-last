package com.latifapp.latif.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryModel(val nameAr:String,val nameEn:String,val id:String): Parcelable
@Parcelize
data class CityModel(val cityAr:String?,val cityEn:String?,val id:String): Parcelable