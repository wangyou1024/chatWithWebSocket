package com.wangyou.chatwithwebsocket.net.client

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer2
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.WebSocketTransformer
import ua.naiksoftware.stomp.StompClient
import java.util.*

/**
 * 与activity生命周期绑定，在销毁时取消关闭连接
 */
class StompClientLifecycle constructor(var stompClient: StompClient, var compositeDisposableLifecycle: CompositeDisposableLifecycle) : LifecycleObserver {

    private var tryTime = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun connect() {
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                if (stompClient.isConnected){
                    Log.i(Const.TAG, "Stomp 连接成功")
                    tryTime = 0
                    topicFriendApplication()
                    timer.cancel()
                }else if (tryTime < 10) {
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
        timer.schedule(task, 0, 30000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disConnect() {
        Log.i(Const.TAG, "Stomp 断开连接")
        stompClient.disconnect()
    }

    fun topicFriendApplication(){
        val subscribe = stompClient.topic(Const.chatResponse)
            .compose(WebSocketTransformer.option())
            .subscribe({
                Log.i(Const.TAG, "Stomp re==$it");
            }, object : ErrorConsumer2() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "Stomp 好友申请订阅失败")
                }
            });
        compositeDisposableLifecycle.addDisposable(subscribe)
    }

}