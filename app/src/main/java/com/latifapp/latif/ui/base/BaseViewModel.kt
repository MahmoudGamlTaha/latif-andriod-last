package com.latifapp.latif.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants.Companion.Lang_PREFS
import com.latifapp.latif.data.local.PreferenceConstants.Companion.USER_ID_PREFS
import com.latifapp.latif.data.models.BlogsModel
import com.latifapp.latif.data.models.ResponseModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class BaseViewModel(val appPrefsStorage: AppPrefsStorage) : ViewModel() {
    private val networkErrorMsgEn = "No Connection !!"
    private val networkErrorMsgAr = "لا يوجد اتصال بالشبكة !!"
    private var language = "en"
    val lang: String
        get() = language
    private var tokenStr = ""
    val token: String
        get() = tokenStr

    protected var errorMsg = MutableLiveData<String>("")
    public val errorMsg_: LiveData<String>
        get() = errorMsg

    protected var loader = MutableStateFlow<Boolean>(false)
    public val loader_: StateFlow<Boolean>
        get() = loader

    init {
        viewModelScope.launch {
            if (!AppPrefsStorage.language_.isNullOrEmpty())
                appPrefsStorage.getValueAsFlow(Lang_PREFS, "en").collect {
                    language = it
                    AppPrefsStorage.language_ = it
                }
        }
        viewModelScope.launch {
            appPrefsStorage.getValueAsFlow(USER_ID_PREFS, "").collect {
                tokenStr = it
                AppPrefsStorage.token = it
            }

        }
    }

    protected suspend fun getErrorMsg(result: ResultWrapper<Any>) {
        withContext(Dispatchers.Main) {
            when (result) {
                is ResultWrapper.NetworkError -> errorMsg.value =
                    if (lang.equals("en")) networkErrorMsgEn else networkErrorMsgAr
                is ResultWrapper.GenericError -> errorMsg.value = result.error!!.message

            }
        }
    }

    protected suspend fun getErrorMsgString(result: String) {
        withContext(Dispatchers.Main) {
            errorMsg.value = result


        }
    }

    override fun onCleared() {
        super.onCleared()
        // reset all
        errorMsg.value = ""

    }
}