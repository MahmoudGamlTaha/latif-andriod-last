package com.service.khdmaa.ui.auth.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.data.models.CityModel
import com.service.khdmaa.data.models.CountryModel
import com.service.khdmaa.network.ResultWrapper
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.utiles.Utiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class CountryViewModel(
    val repo: DataRepo,
    appPrefsStorage: AppPrefsStorage
) : ValidationViewModel(appPrefsStorage) {

    var countryList = listOf<CountryModel>()
    var cityList = listOf<CityModel>()
    val cityModelLiveData = MutableLiveData<String>(null)
    val countryModelLiveData = MutableLiveData<String>(null)
    var countryId = ""
    var cityId = ""
    fun getCountries(): LiveData<List<CountryModel>> {
        loader.value = true
        val liveData = MutableLiveData<List<CountryModel>>(null)
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getCountries()
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        var list = result.value.response.data
                        if (list != null) {
                            countryList = list
                        }
                        liveData.value = list

                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false

        }
        return liveData
    }

    fun setCountry(id: String, name: String) {
        Utiles.log_D("nxnxnxnxnnxnxnx",name + "...")
        countryModelLiveData.value = name
        countryId = id
        cityId=""
        cityModelLiveData.value = null
        getCities(id)
    }

    fun setCity(id: String, name: String) {
        Utiles.log_D("vmvmmvmvmvmv",name +" ....")
        cityModelLiveData.value = name
        cityId = id
    }

    fun getCities(id: String){
        loader.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getCities(id)
            when (result) {
                is ResultWrapper.Success -> {

                    var list = result.value.response.data
                    if (list != null) {
                        cityList = list
                    }


                }
                else -> getErrorMsg(result)
            }
            loader.value = false

        }
    }
}