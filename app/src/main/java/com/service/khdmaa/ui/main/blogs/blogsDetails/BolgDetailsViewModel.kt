package com.service.khdmaa.ui.main.blogs.blogsDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.BlogsModel
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BolgDetailsViewModel@Inject constructor(appPrefsStorage: AppPrefsStorage,val repo: DataRepo)
    :BaseViewModel(appPrefsStorage){

    fun getDetailsOfBlog(id: Int?): LiveData<BlogsModel> {
        val flow_ = MutableLiveData<BlogsModel>()
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getDetailsOfBlog(id)
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        flow_.value = result.value.response.data!!
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

}