package com.latifapp.latif.ui.main.chat.chatPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.ChatResponseModel
import com.latifapp.latif.data.models.SendMsgBody
import com.latifapp.latif.network.ResultWrapper
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatViewModel @Inject constructor(val repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    BaseViewModel(appPrefsStorage) {
    var ad_id = "";
    var adOwner_id: String? = null;
    var room: String? = null
        set(value) {
            if (value.isNullOrEmpty())
                field =null
            else field =value
        }


    init {
        getUserId()
    }

    fun sendMsg(msg: String): LiveData<Boolean> {
        val livedate = MutableLiveData<Boolean>(false)
        loader.value = true
        val body = SendMsgBody(
            ad_item = ad_id,
            recevier = adOwner_id,
            sender = userID,
            message = msg,
            room = room
        )
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.sendMsg(body)
            when (result) {
                is ResultWrapper.Success -> {
                    room = result.value.response.data
                    withContext(Dispatchers.Main) {
                        livedate.value = true
                    }
                }
                else -> getErrorMsg(result)
            }

            loader.value = false
        }
        return livedate
    }

    fun getchatRoomMsgs(msgID: String?): LiveData<List<ChatResponseModel>> {
        val livedate = MutableLiveData<List<ChatResponseModel>>(null)
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getChatOfRoom(msgID,room)

            loader.value = false
            when (result) {
                is ResultWrapper.Success -> {
                    withContext(Dispatchers.Main) {
                        livedate.value = result?.value?.response?.data
                    }
                }
                else -> getErrorMsg(result)
            }


        }
        return livedate
    }
}