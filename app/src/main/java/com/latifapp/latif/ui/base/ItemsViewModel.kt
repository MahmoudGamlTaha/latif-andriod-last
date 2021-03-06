package com.latifapp.latif.ui.base

import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.main.pets.PetsFragment
import com.latifapp.latif.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

open class ItemsViewModel(appPrefsStorage: AppPrefsStorage, repo: DataRepo) :
    CategoriesViewModel(appPrefsStorage, repo) {
    var page = 0
    var category_: Int? = null
    protected var jobPets: Job = Job()
    fun getItems(type: String?, category: Int? = null): StateFlow<List<AdsModel>> {
        val flow_ = MutableStateFlow<List<AdsModel>>(arrayListOf())
        loader.value = true
        if (category_ !=category) {
            jobPets.cancel()
            jobPets = Job()
        }
        category_=category
        Utiles.onSearchDebounce(500L, viewModelScope, {
            viewModelScope.launch(Dispatchers.IO+ jobPets) {
                val result = repo.getNearestAds(
                    type = type, lat = PetsFragment.Latitude_,
                    lag = PetsFragment.Longitude_,
                    page = page, category = category
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
        })
        return flow_


    }
}