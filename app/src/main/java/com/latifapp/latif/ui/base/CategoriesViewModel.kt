package com.latifapp.latif.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.CategoryModel
import com.latifapp.latif.data.models.OptionsModel
import com.latifapp.latif.data.models.RequireModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


open class CategoriesViewModel(appPrefsStorage: AppPrefsStorage, val repo: DataRepo) :
    BaseViewModel(appPrefsStorage) {

    fun getCategoriesList(type: Int): StateFlow<List<CategoryModel>> {
        val flow_ = MutableStateFlow<List<CategoryModel>>(arrayListOf())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
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