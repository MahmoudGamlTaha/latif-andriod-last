package com.latifapp.latif.ui.filter.filter_list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.data.models.ResponseModel
import com.latifapp.latif.data.models.SaveformModelRequest
import com.latifapp.latif.data.models.UserAds
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@HiltViewModel
class FilterListViewModel @Inject  constructor(val repo: DataRepo, appPrefsStorage: AppPrefsStorage) :BaseViewModel(appPrefsStorage){
    val flow_ = MutableLiveData<ResponseModel<List<AdsModel>>>()
    fun filter(
        url: String,
        hashMap: MutableMap<String, Any>,
        type: String?
    ): LiveData<ResponseModel<List<AdsModel>>> {
        val list = mutableListOf<UserAds>()
        for (model in hashMap)
            list.add(UserAds(model.key, model.value))

         val model = SaveformModelRequest(type, list)

        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.saveFilter(url, model)

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

    override fun onCleared() {
        super.onCleared()
        flow_.value=null
    }
}