package com.latifapp.latif.ui.auth.editProfile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.UserModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.auth.signup.CountryViewModel
import com.latifapp.latif.ui.auth.signup.ValidationViewModel
import com.latifapp.latif.ui.main.profile.ProfileUserInfoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(  repo: DataRepo, appPrefsStorage: AppPrefsStorage):
    CountryViewModel(repo,appPrefsStorage) {
        init {
            getUserId()
        }
    var avatar:String?=null
    fun edit(): LiveData<UserModel> {
        val liveData = MutableLiveData<UserModel>(null)
        loader.value = true
        register_request?.id=userID
        register_request?.avatar=avatar
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.editProfile(register_request)
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        liveData.value = result.value.response.data
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return liveData
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
                    avatar=resultData?.get("url").toString()
                    livedata.value = avatar
                    loader.value = false
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    // your code here
                    Log.d("11nononError", requestId + " " + error.description)
                    livedata.value = "-1"
                    errorMsg.value = "try again"
                    restErrorMsg()
                    loader.value = false

                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // your code here
                    Log.d("11nonReschedule", requestId + " " + error.description)
                    livedata.value = "-1"
                    errorMsg.value = "try again"
                    restErrorMsg()
                    loader.value = false

                }
            })
                .dispatch()

        return livedata
    }

}