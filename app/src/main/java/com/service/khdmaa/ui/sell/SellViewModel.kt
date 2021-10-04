package com.service.khdmaa.ui.sell

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.*
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.base.CategoriesViewModel
import com.service.khdmaa.utiles.ExpressionEvaluator
import com.service.khdmaa.utiles.Utiles
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScoped
class SellViewModel @Inject constructor(repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    CategoriesViewModel(appPrefsStorage, repo) {
    public var url: String? = ""
    var isSellAction: Boolean = true
    val responseOfSubmit = MutableLiveData<ResponseModel<SellFormModel>>()
    private val flow_ = MutableStateFlow<List<AdsTypeModel>>(arrayListOf())
     val submitClick = MutableStateFlow<Boolean>(false)
     val submitBtnVisible = MutableStateFlow<Boolean>(false)
     val hashMapFlow = MutableLiveData< MutableMap<String, Any>>(null)
    private var adType = ""
    init {
        getUserId()
    }


   fun clearFilter(){
       hashMapFlow.value = null
   }

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
            val result = repo.getCreateForm(type, isSellAction)
            Utiles.log_D("dndnndnddnndnd", " $result")
            when (result) {
                is ResultWrapper.Success ->
                    if (result.value.response != null) {
                        flow_.value = result.value.response?.data!!
                        url = result.value.response?.data.url
                        submitBtnVisible.value=true
                    }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

    fun saveForm(
        hashMap: MutableMap<String, Any>
    ): LiveData<ResponseModel<SellFormModel>> {
        val list = mutableListOf<UserAds>()


        viewModelScope.launch(Dispatchers.IO) {
            for (model in hashMap)
                list.add(UserAds(model.key, model.value))

            list.add(UserAds("created_by", userID))
            loader.value = true
            val model = SaveformModelRequest(adType, list)
            Utiles.log_D("mxmmxmmxmxmxm", model)

            val result = repo.saveForm(url!!, model)
            when (result) {
                is ResultWrapper.Success -> {
                    if (result.value.success!!) {
                        withContext(Dispatchers.Main) {
                            responseOfSubmit.value = result.value
                        }
                    } else {
                        result.value.msg?.let { getErrorMsgString(it) }
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return responseOfSubmit
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


    public fun submitAdForm(
        hashMap: MutableMap<String, Any>,
        CurrentForm: MutableMap<String, Boolean?>,
        CurrentFormRequiredCond: MutableMap<String, String?>,
        CurrentFormEng: MutableMap<String, String?>
    ) {
        submitClick.value=false

        if (isSellAction) {
            var ecx = ExpressionEvaluator()
            ecx.jexlContext.clear()
            for ((key, value) in hashMap) {
                ecx.jexlContext.set(key, value)
            }
            var CheckConditionRes = true
            for ((key, value) in CurrentForm) {

                if (!CurrentFormRequiredCond[key].isNullOrBlank()) {
                    CheckConditionRes = ecx.evaluateAsBoolean(CurrentFormRequiredCond[key]!!)!!
                } else {
                    CheckConditionRes = true
                }
                if (value == true && CheckConditionRes == true) {
                    try {
                        val str = hashMap[key]
                        if (str == null || str.toString().trim().isEmpty()) {
                             if(lang.equals("en")) {
                                 errorMsg.value =
                                     "The " + CurrentFormEng[key] + " Field is mandatory"
                             }else{
                                 errorMsg.value =
                                     "قيمة " + CurrentFormEng[key] + " مطلوبة"
                             }
                            return
                        }
                    } catch (e: Exception) {
                        if(lang.equals("en")) {
                            errorMsg.value =
                                "The " + CurrentFormEng[key] + " Field is mandatory"
                        }else{
                            errorMsg.value =
                                "قيمة " + CurrentFormEng[key] + " مطلوبة"
                        }

                        return
                    }
                }

            }

//        try {
//            val str = hashMap["longitude"]
//            if (str == null) {
//                err
//                toastMsg_Warning(getString(R.string.addFormLoc), binding.root, this)
//                return
//            }
//        } catch (e: Exception) {
//            toastMsg_Warning(getString(R.string.addFormLoc), binding.root, this)
//            return
//        }
//        if (hashMap.isNullOrEmpty())
//            toastMsg_Warning(getString(R.string.addFormValue), binding.root, this)

            saveForm( hashMap)
        }else{

            hashMapFlow.value=hashMap
        }


    }

    fun submit() {
        submitClick.value=true
    }

}