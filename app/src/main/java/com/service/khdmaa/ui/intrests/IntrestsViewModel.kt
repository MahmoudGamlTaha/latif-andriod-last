package com.service.khdmaa.ui.intrests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.CategoryItemsModel
import com.service.khdmaa.data.models.CategoryModel
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.base.BaseViewModel
import com.service.khdmaa.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class IntrestsViewModel @Inject constructor(appPrefsStorage: AppPrefsStorage, val repo: DataRepo) :
    BaseViewModel(appPrefsStorage) {

    init {
        getUserId()
    }
    fun getAllCategories(page:Int)  : LiveData<List<CategoryModel>> {
        val liveData= MutableLiveData<List<CategoryModel>>(null)
        loader.value = true

        viewModelScope.launch(Dispatchers.IO) {
             
            val result = repo.getAllCategories(page)
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        liveData.value=result.value.response.data
                        Utiles.log_D("dnndndndndnnd555522", "${result.value.response.data}")
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return liveData
    }

    fun getMyCategories() : LiveData<List<CategoryItemsModel>> {
        val liveData= MutableLiveData<List<CategoryItemsModel>>(null)
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getIntrestCategories()
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        liveData.value=result.value.response.data
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }

        return liveData
    }
     fun setInterst(interestList: MutableList<Int?>) : LiveData<Boolean> {
         val liveData = MutableLiveData<Boolean>(false)
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.setIntrestCategories(interestList,userID)
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                    liveData.value=true
                     }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
         return liveData
        }

}