package com.wangyou.chatwithwebsocket.net.client

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.gson.Gson
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
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

    private var tryTime = 0

    private var self: MutableLiveData<User>? = null
    private var userRelationList: MutableLiveData<MutableList<UserRelation>>? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun connect() {
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                if (stompClient.isConnected) {
                    Log.i(Const.TAG, "Stomp 连接成功")
                    tryTime = 0
                    topicFriendApplication()
                    timer.cancel()
                } else if (tryTime < 10) {
                    Log.i(Const.TAG, "Stomp 尝试第${tryTime}次连接")
                    stompClient.connect()
                    tryTime++
                } else {
                    Log.i(Const.TAG, "Stomp 重连次数过多")
                    tryTime = 10
                    timer.cancel()
                }
            }
        }
        timer.schedule(task, 0, 3000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disConnect() {
        Log.i(Const.TAG, "Stomp 断开连接")
        stompClient.disconnect()
    }

    fun topicFriendApplication() {
        val subscribe = stompClient.topic(Const.friendApplicationResponse)
            .compose(WebSocketTransformer.option())
            .subscribe({
                val userRelation = Gson().fromJson(it, UserRelation::class.java)
                self?.value = self?.value
                val newList = mutableListOf<UserRelation>()
                for (i in userRelationList?.value!!){
                    if (i.urid?.equals(userRelation.urid) == true){
                        newList.add(userRelation)
                    }else {
                        newList.add(i)
                    }
                }
                userRelationList?.value = newList
                if (self?.value?.uid != null) {
                    if (self?.value?.uid == userRelation.uidFormer) {
                        // 申请者
                        when (userRelation.enable) {
                            0 -> toast.setText("请求成功")
                            1 -> toast.setText("申请被拒")
                            2 -> toast.setText("申请通过")
                            3 -> toast.setText("您被删除")
                            else -> toast.setText("申请出错")
                        }
                    } else {
                        // 被申请者
                        when (userRelation.enable) {
                            0 -> toast.setText("有新的申请")
                            1 -> toast.setText("拒绝成功")
                            2 -> toast.setText("已同意")
                            3 -> toast.setText("已删除")
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
                    toast.setText("好友申请失败")
                    toast.show()
                    topicFriendApplication()
                }
            });
        compositeDisposableLifecycle.addDisposable(subscribe)
    }

    fun setSelf(user: MutableLiveData<User>) {
        this.self = user
    }

    fun setUserRelationList(userRelationList: MutableLiveData<MutableList<UserRelation>>){
        this.userRelationList = userRelationList
    }

}