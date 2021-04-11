package com.latifapp.latif.data.models

data class SellFormModel(val url: String?=null,
                     val form:List<RequireModel>?=null)
data class RequireModel(
    val type: String?=null,
    val required: Boolean?=false,
    val multi: Boolean=false,
    val name: String?=null,
    val label: String?=null,
    val url: String?=null,
    val options: List<OptionsModel>?=null

)
data class OptionsModel(val code: String?,
                        val label: String?)
data class SaveformModelRequest(
    val type: String?,
    val userAds: List<UserAds>

)
data class UserAds( val id: String?, val value: Any?)