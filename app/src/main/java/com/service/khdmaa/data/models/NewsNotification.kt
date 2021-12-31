package com.service.khdmaa.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



@Parcelize
data class NewsNotification(
    @SerializedName("click_action")
    val click_action: String?="",
    @SerializedName("image")
    val image: String?="",
    @SerializedName("message")
    val message: String,
    @SerializedName("prod_id")
    val prod_id: String,
    @SerializedName("prod_name")
    val prod_name: String?,
    @SerializedName("createAt")
    val createAt: String="",
) : Parcelable