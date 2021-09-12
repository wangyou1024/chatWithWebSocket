package com.wangyou.chatwithwebsocket.data

import android.os.Build
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.util.DateTimeUtil

class TalkViewModel : ViewModel() {
    // 聊天群
    private var group: MutableLiveData<Group>? = null

    // 聊天对象
    private var users: MutableLiveData<MutableMap<Long, User>>? = null

    // 聊天记录
    private var chats: MutableLiveData<MutableList<Chat>>? = null

    // 草稿
    var draft: MutableLiveData<String>? = null

    init {
        group = MutableLiveData(Group(1,"123", "klo", "jlsdf", "jkls", 0, 0))
        users = MutableLiveData(mutableMapOf())
        for (i in 1L..10L) {
            val str = "${i}something"
            users!!.value!![i.toLong()] = User(
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
        chats = MutableLiveData(mutableListOf())
        for (i in 1L..20L){
            val str = "${i}something"
            chats!!.value!!.add(Chat(i, i, i, i, str, i, i.toInt()))
        }
        draft = MutableLiveData("ds")
    }

    fun getUsers(): MutableLiveData<MutableMap<Long, User>>{
        return users!!
    }


    fun getChats(): MutableLiveData<MutableList<Chat>>{
        return chats!!
    }


    fun send(personal: User){
        Log.i("send", draft!!.value!!.length.toString())
        val chat = Chat(
            chats!!.value!!.size.toLong(),
            personal.uid,
            -1,
            1,
            draft!!.value,
            DateTimeUtil.getTimeNow(),
            1
        )
        this.chats!!.value!!.add(chat)
        this.draft!!.value = ""
        this.chats!!.value = this.chats!!.value
    }


}