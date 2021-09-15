package com.wangyou.chatwithwebsocket.net.response

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CompositeDisposableLifecycle : LifecycleObserver {
    var compositeDisposable: CompositeDisposable? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun startNet(){
        compositeDisposable = CompositeDisposable()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroyNet(){
        compositeDisposable!!.dispose()
    }


}