package com.latifapp.latif.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.data.models.ReportedReasonsList
import com.latifapp.latif.data.models.ReportedRequestAd
import com.latifapp.latif.data.models.ResponseModel
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class DetailsViewModel @Inject constructor(val repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    BaseViewModel(appPrefsStorage) {
    private var id=0
    val report_List_liveData =MutableLiveData<List<ReportedReasonsList>>(null)
    val report_liveData =MutableLiveData<ResponseModel<AdsModel>>(null)

    fun getAdDetails(id:Int?) : LiveData<AdsModel> {

            this.id=id?:0
            val liveData =MutableLiveData<AdsModel>(null)
             loader.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val result = repo.getAdDetails(id)
                when (result) {
                    is ResultWrapper.Success -> {
                        withContext(Dispatchers.Main) {
                            liveData.value = (result.value.response.data)
                        }
                    }
                    else -> getErrorMsg(result)
                }
                loader.value = false
            }
             return liveData
        }

    fun reportAd(reason:String){
        loader.value = true
        val reportedRequestAd= ReportedRequestAd(adId = id,reason = reason,type = "REPORT")
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.reportAd(reportedRequestAd)
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        report_liveData.value = (result.value)
                        report_liveData.value = (null)
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
    }

    fun reportReasonsList(){
        loader.value = true
         viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getReportedReasonsList()
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        report_List_liveData.value = result.value?.response?.data

                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false
        }
    }

    fun favAd():LiveData<ResponseModel<AdsModel>> {
        val liveData = MutableLiveData<ResponseModel<AdsModel>>(null)
        loader.value = true
        val reportedRequestAd = ReportedRequestAd(adId = id, type = "INTEREST", reason = null)
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.favAd(reportedRequestAd)
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        liveData.value = (result.value)
                        liveData.value = (null)
                    }
                }
                else -> getErrorMsg(result)
            }
            loader.value = false


        }
        return liveData
    }
}