package com.service.khdmaa.data.models

data class SellFormModel(
    val url: String? = null,
    val form: List<RequireModel>? = null
)

data class RequireModel(
    val type: String? = null,
    val required: Boolean? = false,
    val multi: Boolean = false,
    val name: String? = null,
    val label: String = "",
    val label_ar: String = "",
    var url: String? = null,
    val value: String? = null,
    val show: List<ShowConditionModel>? = null,
    val requiredcond: String? = null,
    val options: List<OptionsModel>? = null

)

data class ShowConditionModel(
    val key: String,
    val value: List<String>
)

data class OptionsModel(
    val code: String?,
    val label: String?
)

data class SaveformModelRequest(
    val type: String?,
    val userAds: List<UserAds>,
    val external: Boolean = true,

    )

data class UserAds(val id: String?, val value: Any?)