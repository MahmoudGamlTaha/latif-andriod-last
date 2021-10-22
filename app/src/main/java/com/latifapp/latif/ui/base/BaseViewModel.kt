package com.latifapp.latif.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants
import com.latifapp.latif.data.local.PreferenceConstants.Companion.Lang_PREFS
import com.latifapp.latif.data.local.PreferenceConstants.Companion.USER_TOKEN_PREFS
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.utiles.Utiles
import com.latifapp.latif.utiles.Utiles.LANGUAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel(val appPrefsStorage: AppPrefsStorage) : ViewModel() {
    var generated=false
    private val networkErrorMsgEn = "No Connection !!"
    private val networkErrorMsgAr = "لا يوجد اتصال بالشبكة !!"
    private var language = "en"
    val lang: String
        get() = language
    private var tokenStr = ""
    val token: String
        get() = tokenStr

    protected var errorMsg = MutableLiveData<String>("")
    protected var loginAgain = MutableLiveData<Boolean>(false)

    public val errorMsg_: LiveData<String>
        get() = errorMsg
    public val loginAgain_: LiveData<Boolean>
        get() = loginAgain

    fun restErrorMsg(){
        errorMsg.value=null
    }
    protected var loader = MutableStateFlow<Boolean>(false)
    public val loader_: StateFlow<Boolean>
        get() = loader
    var userID = ""
    init {
        viewModelScope.launch {
            if (!AppPrefsStorage.language_.isNullOrEmpty())
                appPrefsStorage.getValueAsFlow(Lang_PREFS, "en").collect {
                    language = it
                    LANGUAGE = it
                    AppPrefsStorage.language_ = it
                }
        }
        viewModelScope.launch {
            appPrefsStorage.getValueAsFlow(USER_TOKEN_PREFS, "").collect {
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
                is ResultWrapper.GenericError -> {
                    errorMsg.value = result.error?.message
                    if (result?.code==401 ){
                        loginAgain.value=true
                        // reset livedata
                        loginAgain.value=null
                    }
                }

            }
            restErrorMsg()
        }
    }

    protected suspend fun getErrorMsgString(result: String) {
        withContext(Dispatchers.Main) {
            errorMsg.value = result
        }
    }

    fun getUserId(){
        viewModelScope.launch {
            appPrefsStorage.getValueAsFlow(PreferenceConstants.USER_UserID_PREFS, "").collect {
                userID = it
                Utiles.log_D("mxmmxmmxmxmxm", userID)
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        // reset all
        errorMsg.value = ""

    }
}