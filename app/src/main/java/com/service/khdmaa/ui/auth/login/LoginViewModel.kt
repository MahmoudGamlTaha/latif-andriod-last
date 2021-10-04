package com.service.khdmaa.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cloudinary.Util
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.installations.Utils
import com.google.firebase.messaging.FirebaseMessaging
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.local.PreferenceConstants.Companion.USER_TOKEN_PREFS
import com.service.khdmaa.data.models.LoginRequest
import com.service.khdmaa.network.ErrorResponse
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.main.profile.ProfileUserInfoViewModel
import com.service.khdmaa.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor(repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    ProfileUserInfoViewModel(repo, appPrefsStorage) {

    val validateLiveData = MutableLiveData<SignUpFiled>(null)
    val errorIputsMsg = MutableLiveData<Int>(null)
    var refreshedToken = ""
    init {
        getToken()
    }
    private fun getToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isComplete) {
                refreshedToken = it.result.toString()
                // DO your thing with your firebase token
                Utiles.log_D("cncncnncncncnc", refreshedToken)
            }
        }
    }

    fun login(username: String, password: String) {

        if (validate(username, password)) {
            loader.value = true

            Utiles.log_D("cncncnncncncnc", refreshedToken)
            val loginRequest = LoginRequest(username, password, refreshedToken)
            viewModelScope.launch(Dispatchers.IO) {
                val result = repo.login(loginRequest)
                when (result) {
                    is ResultWrapper.Success -> {
                        AppPrefsStorage.token = result.value.Authorization
                        appPrefsStorage.setValue(USER_TOKEN_PREFS, result.value.Authorization)
                        getUserInfo()
                    }
                    else -> {
                        getErrorMsg(result)
                        loader.value = false
                    }
                }

            }
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