package com.latifapp.latif.ui.subscribe.subscribeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.SubscribeModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubscribeDetailsViewModel @Inject constructor(val repo: DataRepo, appPrefsStorage: AppPrefsStorage) :BaseViewModel(appPrefsStorage) {

    fun getDetails(id:String):LiveData<SubscribeModel>{
        val flow_:MutableLiveData<SubscribeModel> = MutableLiveData(null)
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getSubscribeDetails(id)

            when (result) {
                is ResultWrapper.Success -> {
                    if (result.value.success!!) {
                        withContext(Dispatchers.Main) {
                            flow_.value = result?.value?.response?.data
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

}