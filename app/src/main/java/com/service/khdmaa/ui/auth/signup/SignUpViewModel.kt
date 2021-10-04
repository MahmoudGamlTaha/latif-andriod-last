package com.service.khdmaa.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.CategoryModel
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.main.pets.PetsFragment
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScoped
class SignUpViewModel @Inject constructor(
    repo: DataRepo,
    appPrefsStorage: AppPrefsStorage
) : CountryViewModel(repo, appPrefsStorage) {
    var lat: Double = PetsFragment.Latitude_
    var lag: Double = PetsFragment.Longitude_

    val policesLiveData = MutableLiveData<String>(null)
    var interestList = mutableListOf<Int?>()

    fun register()
            : LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>(false)
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            register_request.latitude=lat
            register_request.longitude=lag
            val result = repo.register(register_request)
            when (result) {
                is ResultWrapper.Success -> {
                    setInterst(result.value?.response?.data?.id)
                    withContext(Dispatchers.Main) {
                        liveData.value = (true)
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
        return liveData
    }


    fun getPolices() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getPolices()
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        var policy = result.value.response.data?.description
                        if (!lang?.equals("en"))
                            policy = result.value.response.data?.descriptionAr
                        policesLiveData.value = policy
                    }
                }
                else -> getErrorMsg(result)
            }
        }
    }

    fun getAllCategories(page: Int): LiveData<List<CategoryModel>> {
        val liveData = MutableLiveData<List<CategoryModel>>(null)
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getAllCategories(page)
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

    private fun setInterst(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.setIntrestCategories(interestList,"$id")
        }
    }
}