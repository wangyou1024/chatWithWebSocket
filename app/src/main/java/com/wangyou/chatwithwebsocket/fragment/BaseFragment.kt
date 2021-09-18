package com.wangyou.chatwithwebsocket.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

open class BaseFragment: Fragment(), LifecycleObserver {

    /**
     * activity生命周期监听
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onActivityResume(){
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(this)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycle.removeObserver(this)
    }
}