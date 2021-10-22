package com.latifapp.latif.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants
import com.latifapp.latif.data.local.PreferenceConstants.Companion.Lang_PREFS
import com.latifapp.latif.data.local.PreferenceConstants.Companion.USER_TOKEN_PREFS
import com.latifapp.latif.data.models.UserModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    ProfileUserInfoViewModel(repo, appPrefsStorage) {


    fun logout(): LiveData<Boolean> {
        val logout_ = MutableLiveData<Boolean>(false)
        AppPrefsStorage.token = ""
        viewModelScope.launch(Dispatchers.IO) {
            loader.value = true
            val result = repo.logout(userID)
            when (result) {
                is ResultWrapper.Success -> {
                    if (result.value.success == true&& result.value?.response?.data ==true) {
                        appPrefsStorage.setValue(USER_TOKEN_PREFS, "")
                        withContext(Dispatchers.Main){
                            logout_.value=true
                        }
                    } else {

                        getErrorMsgString("${result.value?.msg}")
                    }
                }
                else -> {

                    getErrorMsg(result)
                }
            }
            loader.value = false
        }

        return logout_
    }

    fun changeLanguage(lang: String): LiveData<Boolean> {
        val changeLanguage = MutableLiveData<Boolean>(false)
        AppPrefsStorage.language_ = lang
        viewModelScope.launch {
            appPrefsStorage.setValue(Lang_PREFS, lang)
            withContext(Dispatchers.Main)
            {
                changeLanguage.value = true
            }
        }
        return changeLanguage
    }

}