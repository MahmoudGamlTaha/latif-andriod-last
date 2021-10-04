package com.service.khdmaa.ui.fav

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
import javax.inject.Inject

class FavViewModel @Inject constructor(appPrefsStorage: AppPrefsStorage, val repo: DataRepo) :
    BaseViewModel(appPrefsStorage) {
    var page = 0
    fun getFavAds(): StateFlow<List<AdsModel>> {
        val flow_ = MutableStateFlow<List<AdsModel>>(arrayListOf())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.favAds(
                page = page
            )
            when (result) {
                is ResultWrapper.Success -> {
                    Utiles.log_D("nvnnvnvnvnnvnv", "${page}")
                    val list=result.value.response.data?.map {
                        it.ad
                    }
                    flow_.value = list?: arrayListOf()
                    page++
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }
}