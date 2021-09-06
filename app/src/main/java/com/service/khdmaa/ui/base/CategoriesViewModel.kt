package com.service.khdmaa.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.CategoryModel
import com.service.khdmaa.data.models.OptionsModel
import com.service.khdmaa.data.models.RequireModel
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.utiles.Utiles
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


open class CategoriesViewModel(appPrefsStorage: AppPrefsStorage, val repo: DataRepo) :
    BaseViewModel(appPrefsStorage) {

    protected var job: Job= Job()

    fun getCategoriesList(type: Int): StateFlow<List<CategoryModel>> {
        job.cancel()
        job= Job()
        val flow_ = MutableStateFlow<List<CategoryModel>>(arrayListOf())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO+ job) {
            val result = repo.getCategoriesList(type)

            Utiles.log_D("dndnndnddnndnd", " $result")
            when (result) {
                is ResultWrapper.Success -> flow_.value = result.value.response.data!!

                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

    fun getUrlInfo(model: RequireModel): LiveData<RequireModel> {
        val flow_ = MutableLiveData<RequireModel>()
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getCategoriesListFromUrl(model.url!!)
            Utiles.log_D("getCategoriesListFromUrl", " $result")

            when (result) {
                is ResultWrapper.Success -> if (result.value.response != null) {
                    val list = result.value.response?.data?.map {

                            OptionsModel(
                                label = if (lang.equals("en")) it.category.name else it.category.nameAr,
                                code = "${it.category.id}"
                            )
                    }

                    val requireModel = RequireModel(
                        type = "dropDown",
                        required = model.required,
                        name = model.name,
                        label = model.label,
                        label_ar = model.label_ar,
                        options = list
                    )
                    withContext(Dispatchers.Main) {
                        flow_.value = requireModel
                    }
                } else getErrorMsg(result)
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_

    }
}