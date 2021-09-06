package com.service.khdmaa.ui.main.blogs.blogsList

import androidx.lifecycle.viewModelScope
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.BlogsModel
import com.service.khdmaa.data.models.CategoryItemsModel
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.base.BaseViewModel
import com.service.khdmaa.utiles.Utiles.onSearchDebounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BlogsViewModel @Inject constructor(val repo: DataRepo, appPrefsStorage: AppPrefsStorage):BaseViewModel(appPrefsStorage) {
    var page = 0

    fun getListOfBlogs(category: Int?): StateFlow<List<BlogsModel>> {
        val flow_ = MutableStateFlow<List<BlogsModel>>(arrayListOf())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getBlogsList(page,category)
            when (result) {
                is ResultWrapper.Success -> {
                    flow_.value = result.value.response.data!!
                    page++
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

    fun getBlogsCategoryList(): StateFlow<List<CategoryItemsModel>> {
        val flow_ = MutableStateFlow<List<CategoryItemsModel>>(arrayListOf())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getBlogsCategoryList()
            when (result) {
                is ResultWrapper.Success -> {
                    flow_.value = result.value.response.data!!
                    page++
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

    suspend fun getSearchBlogs(txt:String) : StateFlow<List<BlogsModel>> {

        val flow_ = MutableStateFlow<List<BlogsModel>>(arrayListOf())
        loader.value = true
        onSearchDebounce(500L, viewModelScope, {
            viewModelScope.launch(Dispatchers.IO) {
                val result = repo.getSearchBlogs(txt,page)
                when (result) {
                    is ResultWrapper.Success -> {
                        flow_.value = result.value.response.data!!
                    }
                    else -> getErrorMsg(result)
                }
                loader.value = false
            }
        })

        return flow_
    }


}