package com.latifapp.latif.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class BlogsModel(
    val id: Int?,
    val title: String?,
    val category: String?,
    val categoryAr: String?,
    val description: String?,
    val image: String?,
    val images: List<String>?,
    val path: String?,
    val createdDate: String?,
    val externalLink: Boolean,
    val user: UserModel?,
)

data class CreateBlogsModel(
    val userId: String,
    val title: String?,
    val category: Int?,
    val description: String?,
    val images: List<String>?=null,
    val extrnImage: List<String>?,
    val _external: Boolean=true,

)
