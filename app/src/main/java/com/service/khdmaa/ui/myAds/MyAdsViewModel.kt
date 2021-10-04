package com.service.khdmaa.ui.myAds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.AdsModel
import com.service.khdmaa.data.models.SubscribeModel
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.base.BaseViewModel
import com.service.khdmaa.ui.main.pets.PetsFragment
import com.service.khdmaa.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyAdsViewModel @Inject constructor(appPrefsStorage: AppPrefsStorage, val repo: DataRepo) :
    BaseViewModel(appPrefsStorage) {
    var page = 0
    fun getMyAds(): StateFlow<List<AdsModel>> {
        val flow_ = MutableStateFlow<List<AdsModel>>(arrayListOf())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.myAds(
                page = page
            )
            when (result) {
                is ResultWrapper.Success -> {
                    Utiles.log_D("nvnnvnvnvnnvnv", "${page}")
                    flow_.value = result.value.response.data?: arrayListOf()
                    page++
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

    fun activateAd(activeAd: Boolean, id: Int?):LiveData<Boolean> {
        val liveData=MutableLiveData<Boolean>(null)
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
       val result =repo.activateAd(activeAd, id)
            var success=false
            when (result) {
                is ResultWrapper.Success -> {

                 success=result?.value?.success?:false
                }
                else -> {
                    getErrorMsg(result)
                    success=false
                }

            }
           withContext(Dispatchers.Main){
               liveData.value=success
           }
            loader.value = false
        }

       return liveData
    }
}