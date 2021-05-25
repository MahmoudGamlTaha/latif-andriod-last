package com.latifapp.latif.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants.Companion.Lang_PREFS
import com.latifapp.latif.data.local.PreferenceConstants.Companion.USER_ID_PREFS
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel @Inject constructor(val repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    BaseViewModel(appPrefsStorage) {

        fun logout(){
            AppPrefsStorage.token=""
            viewModelScope.launch {
                appPrefsStorage.setValue(USER_ID_PREFS, "")

            }
        }

    fun changeLanguage(lang:String): LiveData<Boolean> {
        val changeLanguage =MutableLiveData<Boolean>(false)
        AppPrefsStorage.language_ = lang
        viewModelScope.launch {
            appPrefsStorage.setValue(Lang_PREFS, lang)
            withContext(Dispatchers.Main)
            {
                changeLanguage.value=true
            }
        }
        return changeLanguage
    }
}