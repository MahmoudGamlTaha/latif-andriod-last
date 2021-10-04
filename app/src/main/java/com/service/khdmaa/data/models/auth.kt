package com.service.khdmaa.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

public data class LoginRequest(val mobile: String, val password: String, val token: String)
public data class RegisterRequest(
    val phone: String,
    val password: String?,
    val passwordRepeat: String? = password,
    val name: String,
    val email: String,
    val city: String,
    val country: String,
    val address: String,
    val state: String? = null,
    var id: String? = null,
    var avatar: String? = null,
    var device: String? = "android",
    var latitude: Double? = null,
    var longitude: Double? = null
)

public data class LoginResponse(val Authorization: String)


@Parcelize
data class UserModel(
    val id: Int?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val address: String?,
    val phone: String?,
    val avatar: String?,
    val adsCount: Int?,
    val city: CityModel?,
    val state: String?,
    val country: CountryModel?,
    val registrationDate: String?
) : Parcelable