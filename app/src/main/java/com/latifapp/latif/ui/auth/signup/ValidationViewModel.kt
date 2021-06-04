package com.latifapp.latif.ui.auth.signup

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.RegisterRequest
import com.latifapp.latif.ui.auth.login.LoginViewModel
import com.latifapp.latif.ui.base.BaseViewModel

open class ValidationViewModel (appPrefsStorage: AppPrefsStorage) :
    BaseViewModel(appPrefsStorage){
    val validateLiveData = MutableLiveData<LoginViewModel.SignUpFiled>(null)
    lateinit var register_request: RegisterRequest
    fun validate(
        name: String,
        email: String,
        password: String,
        phone: String,
        country: String,
        address: String,
        city: String,
        govs: String,
        checkPass:Boolean=true
    ): Boolean {
        var valid = true
        if (name.length < 3) {
            validateLiveData.value = LoginViewModel.SignUpFiled.name_
            valid = false
        }
        if (checkPass) {
            if (password.length < 5) {
                validateLiveData.value = LoginViewModel.SignUpFiled.password_
                valid = false
            }
        }
        if (country.length < 3) {
            validateLiveData.value = LoginViewModel.SignUpFiled.country_
            valid = false
        }
        if (city.length < 3) {
            validateLiveData.value = LoginViewModel.SignUpFiled.city_
            valid = false
        }
        if (address.length < 5) {
            validateLiveData.value = LoginViewModel.SignUpFiled.address_
            valid = false
        }
        if (govs.length < 3) {
            validateLiveData.value = LoginViewModel.SignUpFiled.governorate_
            valid = false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validateLiveData.value = LoginViewModel.SignUpFiled.email_
            valid = false
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            validateLiveData.value = LoginViewModel.SignUpFiled.phone_
            valid = false
        }
        validateLiveData.value = null
        if (valid) {
            register_request = RegisterRequest(
                password = if (checkPass) password else null,
                address = address,
                city = city,
                country = country,
                email = email,
                state = govs,
                name = name,
                phone = phone
            )
        }
        return valid
    }

}