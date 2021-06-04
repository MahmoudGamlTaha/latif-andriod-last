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
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class SignUpViewModel @Inject constructor(val repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    ValidationViewModel(appPrefsStorage) {


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