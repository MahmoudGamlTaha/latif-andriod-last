package com.latifapp.latif.ui.auth.login

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.R
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants.Companion.USER_ID_PREFS
import com.latifapp.latif.data.models.LoginRequest
import com.latifapp.latif.network.ErrorResponse
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(val repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    BaseViewModel(
        appPrefsStorage
    ) {

    val validateLiveData = MutableLiveData<SignUpFiled>(null)
    val errorIputsMsg = MutableLiveData<Int>(null)
    val successInputs = MutableLiveData<Boolean>(false)
    fun login(username: String, password: String) {
        loader.value=true
        if (validate(username, password)) {
            val loginRequest = LoginRequest(username, password)
            repo.login(loginRequest, object : LoginViewModel.getlogin {
                override fun onSuccess(token: String) {
                    loader.value=false
                    AppPrefsStorage.token=token
                    viewModelScope.launch {
                        appPrefsStorage.setValue(USER_ID_PREFS, token)
                    }
                    successInputs.value=true
                }

                override fun onFailure(error: ResultWrapper<ErrorResponse>?) {
                    loader.value=false
                    if (error != null)
                        viewModelScope.launch {
                            getErrorMsg(error)
                        }
                    else {
                        errorIputsMsg.value = (R.string.checkinputs)
                    }


                }
            })
        }
    }

    private fun validate(username: String, password: String): Boolean {
        var valid = true
        if (Patterns.EMAIL_ADDRESS.matcher(username).matches() || Patterns.PHONE.matcher(username)
                .matches()
        ) {

        } else {
            validateLiveData.value = SignUpFiled.email_

            valid = false
        }

        if (password.length >= 5) {

        } else {
            validateLiveData.value = SignUpFiled.password_
            valid = false
        }
        validateLiveData.value = null
        return valid
    }

    enum class SignUpFiled {
        email_, password_, phone_, name_, address_, country_, city_, governorate_
    }

    interface getlogin {
        fun onSuccess(token: String)
        fun onFailure(error: ResultWrapper<ErrorResponse>?)
    }
}