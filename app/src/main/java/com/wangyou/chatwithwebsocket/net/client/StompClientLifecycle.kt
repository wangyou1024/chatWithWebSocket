package com.wangyou.chatwithwebsocket.net.client

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.gson.Gson
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.entity.GroupRelation
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.entity.UserRelation
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer2
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.WebSocketTransformer
import ua.naiksoftware.stomp.StompClient
import java.util.*
import javax.inject.Inject

/**
 * 与activity生命周期绑定，在销毁时取消关闭连接
 */

class StompClientLifecycle constructor(
    var stompClient: StompClient,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle,
    var toast: Toast
) : LifecycleObserver {

    private var self: MutableLiveData<User> = MutableLiveData(User())
    private var userRelationList: MutableLiveData<MutableList<UserRelation>> =
        MutableLiveData(mutableListOf())
    private var groupRelationList: MutableLiveData<MutableList<GroupRelation>> =
        MutableLiveData(mutableListOf())

    // 当前聊天状态
    private var chatList: MutableLiveData<MutableList<Chat>> = MutableLiveData(mutableListOf())
    private var type: MutableLiveData<Int> = MutableLiveData(Chat.PRIVATE_CHAT)
    private var id: MutableLiveData<Long> = MutableLiveData(-1L)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun connect() {
        val task: Runnable = object : Runnable {
            override fun run() {
                stompClient.disconnect()
                stompClient.connect()
                topicFriendApplication()
                topicGroupApplication()
                topicChat()
            }
        }
        Thread(task).start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disConnect() {
        Log.i(Const.TAG, "Stomp 断开连接")
        stompClient.disconnect()
    }

    fun topicChat() {
        val subscribe = stompClient.topic(Const.chatResponse)
            .compose(WebSocketTransformer.option())
            .subscribe({
                val chat = Gson().fromJson(it, Chat::class.java)
                if (type.value == Chat.PRIVATE_CHAT
                    && chat.enable == Chat.PRIVATE_CHAT
                    && ((chat.sender == id.value
                            && chat.recipient == self.value?.uid)
                            || (chat.sender == self.value?.uid
                            && chat.recipient == id.value))
                ) {
                    // 收到当前私聊会话的消息
                    chatList.value?.add(chat)
                    chatList.value = chatList.value
                } else if (type.value == Chat.GROUP_CHAT && chat.enable == Chat.GROUP_CHAT && id.value == chat.gid) {
                    // 收到当前群聊会话的消息
                    chatList.value?.add(chat)
                    chatList.value = chatList.value
                } else {
                    toast.setText("你有新的消息")
                    toast.show()
                }
            }, object : ErrorConsumer2() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "Stomp 消息获取失败${ex.errorMsg}")
                    toast.setText("消息获取失败")
                    toast.show()
                }
            });
        compositeDisposableLifecycle.addDisposable(subscribe)
    }


    fun topicFriendApplication() {
        val subscribe = stompClient.topic(Const.friendApplicationResponse)
            .compose(WebSocketTransformer.option())
            .subscribe({
                val userRelation = Gson().fromJson(it, UserRelation::class.java)
                val newList = mutableListOf<UserRelation>()
                for (i in userRelationList.value!!) {
                    if (i.urid?.equals(userRelation.urid) == true) {
                        newList.add(userRelation)
                    } else {
                        newList.add(i)
                    }
                }
                userRelationList.value = newList
                if (self.value?.uid != null) {
                    if (self.value?.uid == userRelation.uidFormer) {
                        // 申请者
                        when (userRelation.enable) {
                            UserRelation.NO_DEAL -> toast.setText("请求成功")
                            UserRelation.REFUSE -> toast.setText("申请被拒")
                            UserRelation.AGREE -> toast.setText("申请通过")
                            UserRelation.DELETE -> toast.setText("您被删除")
                            else -> toast.setText("申请出错")
                        }
                    } else {
                        // 被申请者
                        when (userRelation.enable) {
                            UserRelation.NO_DEAL -> toast.setText("有新的申请")
                            UserRelation.REFUSE -> toast.setText("拒绝成功")
                            UserRelation.AGREE -> toast.setText("已同意")
                            UserRelation.DELETE -> toast.setText("已删除")
                            else -> toast.setText("申请出错")
                        }
                    }
                } else {
                    toast.setText("好友申请状态更新啦")
                }
                toast.show()
            }, object : ErrorConsumer2() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "Stomp 好友申请失败${ex.errorMsg}")
                    toast.setText("操作失败")
                    toast.show()
                }
            });
        compositeDisposableLifecycle.addDisposable(subscribe)
    }

    fun topicGroupApplication() {
        val subscribe = stompClient.topic(Const.groupApplicationResponse)
            .compose(WebSocketTransformer.option())
            .subscribe({
                val groupRelation = Gson().fromJson(it, GroupRelation::class.java)
                val newList = mutableListOf<GroupRelation>()
                for (i in groupRelationList.value!!) {
                    if (i.grid?.equals(groupRelation.grid) == true) {
                        newList.add(groupRelation)
                    } else {
                        newList.add(i)
                    }
                }
                groupRelationList.value?.clear()
                groupRelationList.value?.addAll(newList)
                groupRelationList.value = groupRelationList.value
                if (self.value?.uid != null) {
                    if (self.value?.uid == groupRelation.uid) {
                        // 申请者
                        when (groupRelation.enable) {
                            GroupRelation.NO_DEAL -> toast.setText("请求成功")
                            GroupRelation.REFUSE -> toast.setText("申请被拒")
                            GroupRelation.AGREE -> toast.setText("申请通过")
                            GroupRelation.DELETE -> toast.setText("退出群聊")
                            GroupRelation.DISMISS -> toast.setText("群聊解散")
                            else -> toast.setText("申请出错")
                        }
                    } else {
                        // 群主
                        when (groupRelation.enable) {
                            GroupRelation.NO_DEAL -> toast.setText("有新的申请")
                            GroupRelation.REFUSE -> toast.setText("拒绝成功")
                            GroupRelation.AGREE -> toast.setText("已同意")
                            GroupRelation.DELETE -> toast.setText("群成员退出")
                            GroupRelation.DISMISS -> toast.setText("群聊解散")
                            else -> toast.setText("申请出错")
                        }
                    }
                } else {
                    toast.setText("群申请状态更新啦")
                }
                toast.show()
            }, object : ErrorConsumer2() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "Stomp 群申请失败${ex.errorMsg}")
                    toast.setText("操作失败")
                    toast.show()
                }
            });
        compositeDisposableLifecycle.addDisposable(subscribe)
    }

    fun setSelf(user: MutableLiveData<User>) {
        this.self = user
    }

    fun setUserRelationList(userRelationList: MutableLiveData<MutableList<UserRelation>>) {
        this.userRelationList = userRelationList
    }

    fun setGroupRelationList(groupRelationList: MutableLiveData<MutableList<GroupRelation>>) {
        this.groupRelationList = groupRelationList
    }

    fun setChatList(chatList: MutableLiveData<MutableList<Chat>>) {
        this.chatList = chatList
    }

    fun setType(type: MutableLiveData<Int>) {
        this.type = type
    }

    fun setId(id: MutableLiveData<Long>) {
        this.id = id
    }

}