package com.wangyou.chatwithwebsocket.net.response

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.wangyou.chatwithwebsocket.conf.Const
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * 管理网络订阅：在activity启动时创建，在activity销毁时取消所有订阅，避免内存泄漏
 */
class CompositeDisposableLifecycle : LifecycleObserver {
    var compositeDisposable: CompositeDisposable? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun startNet(){
        compositeDisposable = CompositeDisposable()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroyNet(){
        Log.i(Const.TAG, "停止所有请求")
        compositeDisposable!!.dispose()
    }


}