package com.latifapp.latif.ui.sell

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.latifapp.latif.R
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.*
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.CategoriesViewModel
import com.latifapp.latif.utiles.ExpressionEvaluator
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
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

    fun uploadImage(path: String, Module:String): LiveData<String> {
        val livedata = MutableLiveData<String>()
        val file= File(path)
        val imagePart = MultipartBody.Part.createFormData("file",
                                     file.name,RequestBody.create(MediaType.parse("image/*"), file))

        viewModelScope.launch(Dispatchers.IO) {

            val result = repo.UploadFiles(imagePart,Module)
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        if (result.value.response.data is List<*>) {
                            val a: List<String> =
                                result.value.response.data!!.filterIsInstance<String>()

                            livedata.value = a.get(0);
                        }
                        Utiles.log_D("dnndndndndnnd555522", "${result.value.response.data}")
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return livedata
    }


    public fun submitAdForm(
        hashMap: MutableMap<String, Any>,
        CurrentForm: MutableMap<String, Boolean?>,
        CurrentFormRequiredCond: MutableMap<String, String?>,
        CurrentFormEng: MutableMap<String, String?>,
        context: Context?
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
                            errorMsg.value =  CurrentFormEng[key] +" "+ context?.getString(R.string.is_required)

                            return
                        }
                    } catch (e: Exception) {
                        errorMsg.value =  CurrentFormEng[key] +" "+ context?.getString(R.string.is_required)

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