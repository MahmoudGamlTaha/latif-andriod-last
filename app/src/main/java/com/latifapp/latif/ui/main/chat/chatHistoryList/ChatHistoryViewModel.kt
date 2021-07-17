package com.latifapp.latif.ui.main.chat.chatHistoryList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.ChatResponseModel
import com.latifapp.latif.data.models.MsgNotification
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatHistoryViewModel @Inject constructor(
    appPrefsStorage: AppPrefsStorage,
    val repo: DataRepo
) : BaseViewModel(appPrefsStorage) {

    fun getAllMyRoomsChat(page:Int): LiveData<List<MsgNotification>> {
        loader.value = true
        val livedate = MutableLiveData<List<MsgNotification>>(null)
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getAllMyRooms(page)
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        livedate.value = result?.value?.response?.data
                    }
                }
                else -> getErrorMsg(result)
            }

            loader.value = false
        }
        return livedate
    }

}