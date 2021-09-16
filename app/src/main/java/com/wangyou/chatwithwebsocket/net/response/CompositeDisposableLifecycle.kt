package com.wangyou.chatwithwebsocket.net.response

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.wangyou.chatwithwebsocket.conf.Const
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.disposables.CompositeDisposable as CompositeDisposable2
import io.reactivex.disposables.Disposable as Disposable2

/**
 * 管理网络订阅：在activity启动时创建，在activity销毁时取消所有订阅，避免内存泄漏
 */
class CompositeDisposableLifecycle : LifecycleObserver {
    var compositeDisposable: CompositeDisposable? = null
    var compositeDisposable2: CompositeDisposable2? = null
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun startNet(){
        compositeDisposable = CompositeDisposable()
        compositeDisposable2 = CompositeDisposable2()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroyNet(){
        Log.i(Const.TAG, "停止所有请求")
        compositeDisposable!!.dispose()
        compositeDisposable2!!.dispose()
    }

    fun addDisposable(disposable: Disposable2){
        compositeDisposable2?.add(disposable)
    }

    fun addDisposable(disposable: Disposable){
        compositeDisposable?.add(disposable)
    }

}