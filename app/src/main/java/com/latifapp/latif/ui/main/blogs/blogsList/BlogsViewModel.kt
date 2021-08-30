package com.latifapp.latif.ui.main.blogs.blogsList

import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.BlogsModel
import com.latifapp.latif.data.models.CategoryItemsModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import com.latifapp.latif.utiles.Utiles.onSearchDebounce
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