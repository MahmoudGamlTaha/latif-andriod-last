package com.latifapp.latif.ui.sell

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants
import com.latifapp.latif.data.models.*
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import com.latifapp.latif.ui.base.CategoriesViewModel
import com.latifapp.latif.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SellViewModel @Inject constructor(repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    CategoriesViewModel(appPrefsStorage, repo) {

    init {
        getUserId()
    }

    private val flow_ = MutableStateFlow<List<AdsTypeModel>>(arrayListOf())
    private var adType = ""


    fun getAdsTypeList(): StateFlow<List<AdsTypeModel>> {
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getAdsTypeList()
            Utiles.log_D("dndnndnddnndnd", " $result")
            when (result) {
                is ResultWrapper.Success -> flow_.value = result.value.response.data!!
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

    fun getCreateForm(type: String): StateFlow<SellFormModel> {
        adType = type
        val flow_ = MutableStateFlow<SellFormModel>(SellFormModel())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getCreateForm(type)
            Utiles.log_D("dndnndnddnndnd", " $result")
            when (result) {
                is ResultWrapper.Success -> if (result.value.response != null)
                    flow_.value = result.value.response?.data!!
                else getErrorMsg(result)
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

    fun saveForm(
        url: String,
        hashMap: MutableMap<String, Any>
    ): LiveData<ResponseModel<SellFormModel>> {
        val list = mutableListOf<UserAds>()
        val flow_ = MutableLiveData<ResponseModel<SellFormModel>>()

        viewModelScope.launch(Dispatchers.IO) {
            for (model in hashMap)
                list.add(UserAds(model.key, model.value))

            list.add(UserAds("created_by", userID))
            loader.value = true
            val model = SaveformModelRequest(adType, list)
            Utiles.log_D("mxmmxmmxmxmxm",model)

            val result = repo.saveForm(url, model)
            when (result) {
                is ResultWrapper.Success -> {
                    if (result.value.success!!) {
                        withContext(Dispatchers.Main) {
                            flow_.value = result.value
                        }
                    } else {
                        result.value.msg?.let { getErrorMsgString(it) }
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

    fun uploadImage(path: String): LiveData<String> {
        val livedata = MutableLiveData<String>()
        val requestId: String =
            MediaManager.get().upload(path).callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    // your code here
                    loader.value = true
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    Log.d("11nononProgress", requestId)
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>?) {
                    // your code here
                    Log.d("11nononSuccess", requestId + " " + resultData)
                    livedata.value = resultData?.get("url").toString()
                    loader.value = false
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    // your code here
                    Log.d("11nononError", requestId + " " + error.description)
                    livedata.value = "-1"
                    errorMsg.value = "try again"
                    loader.value = false

                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // your code here
                    Log.d("11nonReschedule", requestId + " " + error.description)
                    livedata.value = "-1"
                    errorMsg.value = "try again"
                    loader.value = false

                }
            })
                .dispatch()

        return livedata
    }


}