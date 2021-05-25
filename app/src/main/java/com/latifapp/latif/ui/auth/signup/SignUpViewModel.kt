package com.latifapp.latif.ui.auth.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.data.models.RegisterRequest
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.auth.login.LoginViewModel
import com.latifapp.latif.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpViewModel @Inject constructor(val repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    BaseViewModel(appPrefsStorage) {
    val validateLiveData = MutableLiveData<LoginViewModel.SignUpFiled>(null)
    private lateinit var register_request: RegisterRequest
    fun validate(
        name: String,
        email: String,
        password: String,
        phone: String,
        country: String,
        address: String,
        city: String,
        govs: String
    ): Boolean {
        var valid = true
        if (name.length < 3) {
            validateLiveData.value = LoginViewModel.SignUpFiled.name_
            valid = false
        }
        if (password.length < 5) {
            validateLiveData.value = LoginViewModel.SignUpFiled.password_
            valid = false
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
                password = password,
                address = address,
                city = city,
                country = country,
                email = email,
                governorate = govs,
                name = name,
                phone = phone
            )
        }
        return valid
    }


    fun register()
            : LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>(false)
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.register(register_request)
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        liveData.value = (true)
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return liveData
    }
}