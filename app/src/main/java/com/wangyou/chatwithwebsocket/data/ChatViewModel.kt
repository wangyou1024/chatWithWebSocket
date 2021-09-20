package com.wangyou.chatwithwebsocket.data

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.net.api.ChatServiceAPI
import com.wangyou.chatwithwebsocket.net.api.GroupServiceAPI
import com.wangyou.chatwithwebsocket.net.api.UserServiceAPI
import com.wangyou.chatwithwebsocket.net.client.StompClientLifecycle
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import com.wangyou.chatwithwebsocket.util.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.naiksoftware.stomp.StompClient
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    var toast: Toast,
    var stompClient: StompClient,
    var userServiceAPI: UserServiceAPI,
    var chatServiceAPI: ChatServiceAPI,
    var groupServiceAPI: GroupServiceAPI,
    var stompClientLifecycle: StompClientLifecycle,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
) : ViewModel() {
    // 聊天群
    private var group: MutableLiveData<Group> = MutableLiveData(Group())

    // 聊天对象
    private var users: MutableLiveData<MutableMap<Long, User>> = MutableLiveData(mutableMapOf())

    // 聊天记录
    private var chats: MutableLiveData<MutableList<Chat>> = MutableLiveData(mutableListOf())

    // 草稿
    var draft: MutableLiveData<String> = MutableLiveData("")

    // 1代表私聊，2代表群聊
    var type: MutableLiveData<Int> = MutableLiveData(Chat.PRIVATE_CHAT)

    fun loadChat(id: Long) {
        if (type.value == Chat.PRIVATE_CHAT){
            loadChatObject(id)
            stompClientLifecycle.setType(type)

        } else if (type.value == Chat.GROUP_CHAT){
            loadGroup(id)
            loadLeader(id)
            stompClientLifecycle.setType(type)
        }
        stompClientLifecycle.setChatList(chats)
        stompClientLifecycle.setId(MutableLiveData(id))
        chatServiceAPI.findChatList(type.value!!, id)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "获取聊天记录 -> ${it.size}")
                chats.value?.clear()
                chats.value?.addAll(it)
                chats.value = chats.value
            }, object : ErrorConsumer(){
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "获取聊天记录 -> ${ex.errorMsg}")
                }

            })
    }

    private fun loadGroup(gid: Long){
        groupServiceAPI.findGroupById(gid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "获取聊天群聊信息 -> $it")
                group.value = it
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "获取聊天群聊信息-> ${ex.errorMsg}")
                }
            })
    }

    fun loadLeader(gid: Long) {
        userServiceAPI.findLeader(gid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "加载聊天群主 -> $it")
                users.value?.clear()
                users.value?.put(it.uid!!, it)
                // 同时加载成员
                loadMembers(gid)
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "加载聊天群主 -> ${ex.errorMsg}")
                }

            })
    }

    private fun loadMembers(gid: Long) {
        userServiceAPI.findMembers(gid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "获取群成员 -> ${it.size}")
                for (user in it) {
                    users.value?.put(user.uid!!, user)
                }
                users.value = users.value
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "获取群成员 -> ${ex.errorMsg}")
                }
            })
    }

    private fun loadChatObject(uid: Long) {
        userServiceAPI.findUserById(uid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "加载聊天对象 -> $it")
                users.value?.clear()
                users.value?.put(it.uid!!, it)
                users.value = users.value
            }, object : ErrorConsumer(){
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "加载聊天对象 -> ${ex.errorMsg}")
                }

            })
    }

    fun send(personal: User){
        if(!stompClient.isConnected){
            stompClientLifecycle.connect()
        }
        Log.i("send", draft.value!!.length.toString())
        val chat = Chat(
            null,
            personal.uid,
            -1,
            -1,
            draft.value,
            DateTimeUtil.getTimeNow(),
            type.value!!
        )
        if (type.value == Chat.PRIVATE_CHAT){
            // 私聊的对象只有一个
            chat.recipient = users.value?.values?.first()?.uid
        } else {
            chat.gid = group.value?.gid
        }
        stompClient.send(Const.chat, Gson().toJson(chat)).subscribe()
        this.draft.value = ""
    }

    fun getGroup(): MutableLiveData<Group> {
        return group
    }

    fun getUsers(): MutableLiveData<MutableMap<Long, User>>{
        return users
    }


    fun getChats(): MutableLiveData<MutableList<Chat>>{
        return chats
    }

}