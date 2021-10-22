package com.latifapp.latif.ui.main.blogs.createBlog

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants
import com.latifapp.latif.data.models.CategoryItemsModel
import com.latifapp.latif.data.models.CreateBlogsModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateBlogViewModel @Inject constructor(
    appPrefsStorage: AppPrefsStorage,
    val repo: DataRepo
) : BaseViewModel(appPrefsStorage) {

    fun getBlogsCategoryList(): StateFlow<List<CategoryItemsModel>> {
        val flow_ = MutableStateFlow<List<CategoryItemsModel>>(arrayListOf())
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getBlogsCategoryList()
            when (result) {
                is ResultWrapper.Success -> {
                    flow_.value = result.value.response.data!!
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return flow_
    }

    init {
        getUserId()
    }
    fun uploadImage(path: String): LiveData<String> {
        val livedata = MutableLiveData<String>()
        val requestId: String =
            MediaManager.get().upload(path).callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    // your code here
                    loader.value = true
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    Log.d("11nononProgress", requestId)
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>?) {
                    // your code here
                    Log.d("11nononSuccess", requestId + " " + resultData)
                    livedata.value = resultData?.get("url").toString()
                    loader.value = false
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    // your code here
                    Log.d("11nononError", requestId + " " + error.description)
                    livedata.value = "-1"
                    errorMsg.value = "try again"
                    loader.value = false

                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // your code here
                    Log.d("11nonReschedule", requestId + " " + error.description)
                    livedata.value = "-1"
                    errorMsg.value = "try again"
                    loader.value = false

                }
            })
                .dispatch()

        return livedata
    }

    fun createBlog(
        blogCategoryID: Int?,
        imagesList: List<String>?,
        title: String,
        description: String
    ): Flow<Boolean> {
        val flow_ = MutableStateFlow(false)
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {

            val CreateBlogsModel = CreateBlogsModel(
                title = title,
                category = blogCategoryID,
                description = description,
                extrnImage = imagesList,
                userId = userID
            )

                val result = repo.createBlog(CreateBlogsModel)
                when (result) {
                    is ResultWrapper.Success -> {

                        flow_.value = true
                    }
                    else -> getErrorMsg(result)
                }
                loader.value = false

            }

        return flow_
    }
}