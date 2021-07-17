package com.latifapp.latif.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class SendMsgBody(
    val ad_item: String,
    val device_id: String = "mm",
    val device_model: String = "android",
    val message: String,
    val recevier: String?,
    val room: String?,
    val sender: String,
)

@Parcelize
data class MsgNotification(
    @SerializedName("reciverName")
    val sender_name: String?="",
    @SerializedName("reciverImage")
    val sender_avater: String?="",
    @SerializedName("deviceModel")
    var device_model: String? = "android",
    @SerializedName("message")
    val message: String,
    @SerializedName("itemId")
    val prod_id: String,
    @SerializedName("room")
    val chat_room: String,
    @SerializedName("senderId")
    val sender_id: String? = null,
    @SerializedName("prod_name")
    val prod_name: String?,
    @SerializedName("createAt")
    val createAt: String="",
) : Parcelable

data class ChatResponseModel(
    val id: String? = null,
    val reciverId: String? = null,
    val senderId: String,
    val message: String,
    val room: String? = null,

    )