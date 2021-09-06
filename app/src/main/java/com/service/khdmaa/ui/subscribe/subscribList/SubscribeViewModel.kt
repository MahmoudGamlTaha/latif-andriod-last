package com.service.khdmaa.ui.subscribe.subscribList

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

class SubscribeViewModel @Inject constructor(appPrefsStorage: AppPrefsStorage, val repo: DataRepo) :
    BaseViewModel(appPrefsStorage) {
    var page = 0
    fun getSubscribeList(): StateFlow<List<SubscribeModel>> {
        val flow_ = MutableStateFlow<List<SubscribeModel>>(arrayListOf())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getSubscribeList(
                page = page
            )
            when (result) {
                is ResultWrapper.Success -> {
                    Utiles.log_D("nvnnvnvnvnnvnv", "${page}")
                    flow_.value = result.value.response.data!!
                    page++
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }
}