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

    private var chatList: MutableLiveData<MutableList<Chat>>? = null
    private var userMap: MutableLiveData<MutableMap<Long, User>>? = null
    private var groupMap: MutableLiveData<MutableMap<Long, Group>>? = null

    init {
        chatList = MutableLiveData()
        chatList!!.value = ArrayList<Chat>()
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, -1, "个人聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        chatList!!.value!!.add(
            Chat(1L, 1L, 2L, 1, "群聊天", 2, 0)
        )
        userMap = MutableLiveData(mutableMapOf())
        for (i in 2L..10L) {
            val str = "${i}something"
            userMap!!.value!![i.toLong()] = User(
                i,
                str,
                str,
                str,
                str,
                str,
                i.toInt(),
                str,
                str,
                i.toInt(),
                str,
                i.toInt(),
                i.toInt(),
                i.toInt()
            )
        }
        groupMap = MutableLiveData(mutableMapOf())
        for (i in 1L..10L) {
            val str = "${i}群聊"
            groupMap!!.value!![i.toLong()] = Group(i, str, str, str, str, 1, 1)
        }
    }

    fun getChatList(): MutableLiveData<MutableList<Chat>> {
        if (chatList == null) {
            chatList = MutableLiveData()
            chatList!!.value = ArrayList<Chat>()
        }
        return chatList!!
    }

    fun setChatList(chatList: MutableLiveData<MutableList<Chat>>) {
        this.chatList = chatList
    }

    fun getUserMap():MutableLiveData<MutableMap<Long, User>> {
        return userMap!!
    }

    fun getGroupMap(): MutableLiveData<MutableMap<Long, Group>> {
        return groupMap!!
    }

}