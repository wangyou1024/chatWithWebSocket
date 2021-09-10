package com.wangyou.chatwithwebsocket.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.Chat

class ChatViewModel(): ViewModel() {

    private var chatList: MutableLiveData<MutableList<Chat>>? = null

    init {
        chatList = MutableLiveData()
        chatList!!.value = ArrayList<Chat>()
        for (i in 1..10){
            chatList!!.value!!.add(Chat("第${i}个", "第${i}个消息", "https://images.shobserver.com/news/690_390/2021/9/6/a1a3070d696f4a69a4c039a79514c8ca.jpg", 1631026488))
        }
    }

    fun getChatList():MutableLiveData<MutableList<Chat>>{
        if (chatList == null){
            chatList = MutableLiveData()
            chatList!!.value = ArrayList<Chat>()
        }
        return chatList!!
    }

    fun setChatList(data: MutableLiveData<MutableList<Chat>>){
        chatList = data
    }

}