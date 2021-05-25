package com.latifapp.latif.ui.filter.filter_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.*
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.CategoriesViewModel
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FilterViewModel @Inject constructor(repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    CategoriesViewModel(appPrefsStorage,repo) {

    fun getCreateForm(type: String): StateFlow<SellFormModel> {
         val flow_ = MutableStateFlow<SellFormModel>(SellFormModel())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.createFilterForm(type)
            when (result) {
                is ResultWrapper.Success -> if (result.value.response != null)
                    flow_.value = result.value.response?.data!!
                else getErrorMsg(result)
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }


}