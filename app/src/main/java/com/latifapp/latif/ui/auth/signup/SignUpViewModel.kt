package com.latifapp.latif.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.CategoryModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.main.pets.PetsFragment
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
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

        if (register_request == null||register_request?.email.isNullOrEmpty()) {
            errorMsg.value = if (lang.equals("en")) "Please add correct data" else "من فضلك ادخل بياناتك بشكل صحيح"
        } else {
            loader.value = true
            viewModelScope.launch(Dispatchers.IO) {

                register_request?.latitude = lat
                register_request?.longitude = lag
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
            val result = repo.setIntrestCategories(interestList, "$id")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Utiles.log_D("xmmxmxmmxmxmssx", "onCleared")
    }


}