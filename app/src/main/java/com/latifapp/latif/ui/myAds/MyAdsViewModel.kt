package com.latifapp.latif.ui.myAds

import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.data.models.SubscribeModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import com.latifapp.latif.ui.main.pets.PetsFragment
import com.latifapp.latif.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
}