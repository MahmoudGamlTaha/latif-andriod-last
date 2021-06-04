package com.latifapp.latif.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants
import com.latifapp.latif.data.models.UserModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class ProfileUserInfoViewModel(val repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    BaseViewModel(appPrefsStorage) {
    val successInputs = MutableLiveData<Boolean>(false)
    val userInfo = MutableLiveData<UserModel>(null)
    fun getUserInfo() {
        loader.value=true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getUserInfo()
            when (result) {
                is ResultWrapper.Success -> {
                    if (result.value.success==true) {
                        appPrefsStorage.setValue(
                            PreferenceConstants.USER_UserID_PREFS,
                            "${result.value.response?.data?.id}"
                        )

                        withContext(Dispatchers.Main) {
                            successInputs.value = true
                            userInfo.value=result.value.response?.data
                        }
                    } else {
                        AppPrefsStorage.token = ""
                        appPrefsStorage.setValue(PreferenceConstants.USER_TOKEN_PREFS, "")
                        getErrorMsgString("${result.value?.msg}")
                    }
                }
                else -> {
                    AppPrefsStorage.token = ""
                    appPrefsStorage.setValue(PreferenceConstants.USER_TOKEN_PREFS, "")
                    getErrorMsg(result)
                }
            }
            loader.value = false

        }

    }

//    if (result.value.response?.data != null) {
//        appPrefsStorage.setValue(
//            PreferenceConstants.USER_ADDRESS,
//            "${result.value.response?.data?.address}"
//        )
//        appPrefsStorage.setValue(
//            PreferenceConstants.USER_NAME,
//            "${result.value.response?.data?.firstName} ${result.value.response?.data?.lastName}"
//        )
//        appPrefsStorage.setValue(
//            PreferenceConstants.USER_CITY,
//            "${result.value.response?.data?.city}"
//        )
//        appPrefsStorage.setValue(
//            PreferenceConstants.USER_COUNTRY,
//            "${result.value.response?.data?.country}"
//        )
//        appPrefsStorage.setValue(
//            PreferenceConstants.USER_GOVS,
//            "${result.value.response?.data?.state}"
//        )
//        appPrefsStorage.setValue(
//            PreferenceConstants.USER_EMAIL,
//            "${result.value.response?.data?.email}"
//        )
//        appPrefsStorage.setValue(
//            PreferenceConstants.USER_PHONE,
//            "${result.value.response?.data?.phone}"
//        )
//        appPrefsStorage.setValue(
//            PreferenceConstants.USER_REG_DATE,
//            "${result.value.response?.data?.registrationDate}"
//        )
//    }
}
