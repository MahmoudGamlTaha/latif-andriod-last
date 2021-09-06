package com.service.khdmaa.ui.main.pets

import androidx.lifecycle.viewModelScope
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.AdsModel
import com.service.khdmaa.data.models.BlogsModel
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.base.CategoriesViewModel
import com.service.khdmaa.ui.base.ItemsViewModel
import com.service.khdmaa.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PetsViewModel @Inject constructor(appPrefsStorage: AppPrefsStorage, repo: DataRepo) :
    ItemsViewModel(
        appPrefsStorage, repo
    ) {


}