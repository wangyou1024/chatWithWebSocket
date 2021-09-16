package com.wangyou.chatwithwebsocket.net.api

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.wangyou.chatwithwebsocket.conf.Const
import ua.naiksoftware.stomp.StompClient
import java.util.*

/**
 * 与activity生命周期绑定，在销毁时取消关闭连接
 */
class StompClientLifecycle constructor(var stompClient: StompClient) : LifecycleObserver {

    private var tryTime = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun connect() {
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                if (!stompClient.isConnected && tryTime < 10) {
                    Log.i(Const.TAG, "尝试第${tryTime}次连接")
                    stompClient.connect()
                    tryTime++
                } else if (tryTime == 10){
                    Log.i(Const.TAG, "重连次数过多")
                    timer.cancel()
                } else {
                    Log.i(Const.TAG, "连接成功")
                    tryTime = 0
                    timer.cancel()
                }
            }
        }
        timer.schedule(task, 0, 3000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disConnect() {
        Log.i(Const.TAG, "断开连接")
        stompClient.disconnect()
    }
}