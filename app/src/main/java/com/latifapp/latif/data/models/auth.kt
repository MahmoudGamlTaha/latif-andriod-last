package com.latifapp.latif.data.models

public data class LoginRequest(val mobile: String, val password: String)
public data class RegisterRequest(
    val phone: String, val password: String,
    val passwordRepeat: String = password,
    val name: String, val email: String,
    val city: String, val country: String,
    val address: String, val governorate: String,
)