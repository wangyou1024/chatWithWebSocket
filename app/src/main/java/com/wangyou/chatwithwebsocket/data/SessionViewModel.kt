package com.wangyou.chatwithwebsocket.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.net.api.ChatServiceAPI
import com.wangyou.chatwithwebsocket.net.client.StompClientLifecycle
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.naiksoftware.stomp.StompClient
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    var toast: Toast,
    var stompClient: StompClient,
    var chatServiceAPI: ChatServiceAPI,
    var stompClientLifecycle: StompClientLifecycle,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
) : ViewModel() {

    private var chatList: MutableLiveData<MutableList<Chat>> = MutableLiveData(mutableListOf())


    fun loadSessionList(){
        chatServiceAPI.findSessionList()
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "获取聊天会话 -> ${it.size}")
                chatList.value?.clear()
                chatList.value?.addAll(it)
                chatList.value = chatList.value
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "获取聊天会话 -> ${ex.errorMsg}")
                }

            })
    }

    fun getChatList(): MutableLiveData<MutableList<Chat>> {
        return chatList
    }

    fun setChatList(chatList: MutableLiveData<MutableList<Chat>>) {
        this.chatList = chatList
    }

}