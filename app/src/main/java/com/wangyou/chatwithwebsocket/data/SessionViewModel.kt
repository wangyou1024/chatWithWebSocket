package com.wangyou.chatwithwebsocket.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor() : ViewModel() {

    private var chatList: MutableLiveData<MutableList<Chat>> = MutableLiveData(mutableListOf())
    private var userMap: MutableLiveData<MutableMap<Long, User>> = MutableLiveData(mutableMapOf())
    private var groupMap: MutableLiveData<MutableMap<Long, Group>> = MutableLiveData(mutableMapOf())


    fun getChatList(): MutableLiveData<MutableList<Chat>> {
        return chatList
    }

    fun setChatList(chatList: MutableLiveData<MutableList<Chat>>) {
        this.chatList = chatList
    }

    fun getUserMap():MutableLiveData<MutableMap<Long, User>> {
        return userMap
    }

    fun getGroupMap(): MutableLiveData<MutableMap<Long, Group>> {
        return groupMap
    }

}