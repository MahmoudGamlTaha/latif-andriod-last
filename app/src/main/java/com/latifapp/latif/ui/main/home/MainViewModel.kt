package com.latifapp.latif.ui.main.home

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
import com.latifapp.latif.ui.base.ItemsViewModel
import com.latifapp.latif.utiles.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class MainViewModel @Inject constructor( repo: DataRepo,appPrefsStorage: AppPrefsStorage):
ItemsViewModel(
appPrefsStorage, repo
) {
    val typeFlow= MutableStateFlow<TYPES>(TYPES())
    val categoryFlow= MutableStateFlow<Int>(-1)




    class TYPES(val categoryType:Int= AppConstants.PETS, val itemType:String= AppConstants.PETS_STR)

    override fun onCleared() {
        super.onCleared()
        errorMsg.value = null

    }


    fun filter(
        url: String,
        hashMap: MutableMap<String, Any>,
        type: String?
    ): LiveData<ResponseModel<List<AdsModel>>> {
        val list = mutableListOf<UserAds>()
        for (model in hashMap)
            list.add(UserAds(model.key, model.value))

        val model = SaveformModelRequest(type, list)
        val flow_ = MutableLiveData<ResponseModel<List<AdsModel>>>()
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
}