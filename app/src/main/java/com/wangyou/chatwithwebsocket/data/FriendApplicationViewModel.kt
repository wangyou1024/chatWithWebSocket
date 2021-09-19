package com.wangyou.chatwithwebsocket.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.entity.UserRelation
import com.wangyou.chatwithwebsocket.net.api.UserRelationServiceAPI
import com.wangyou.chatwithwebsocket.net.client.StompClientLifecycle
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseData
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import com.wangyou.chatwithwebsocket.util.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.naiksoftware.stomp.StompClient
import javax.inject.Inject

@HiltViewModel
class FriendApplicationViewModel @Inject constructor(
    var toast: Toast,
    var stompClient: StompClient,
    var stompClientLifecycle: StompClientLifecycle,
    var userRelationServiceAPI: UserRelationServiceAPI,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
) : ViewModel() {
    private var userRelationList: MutableLiveData<MutableList<UserRelation>>? = null

    init {
        userRelationList = MutableLiveData(mutableListOf())
    }

    fun loadUserRelationList() {
        userRelationServiceAPI.findUserRelationList()
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                userRelationList?.value = it as MutableList<UserRelation>?
                stompClientLifecycle.setUserRelationList(userRelationList!!)
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, ex.errorMsg)
                }
            })
    }

    fun agreeApplication(former: Long, latter: Long) {
        if (!stompClient.isConnected) {
            stompClientLifecycle.connect()
        }
        val userRelation = UserRelation(
            0,
            former,
            latter,
            DateTimeUtil.getTimeNow().toInt(),
            DateTimeUtil.getTimeNow().toInt(),
            UserRelation.AGREE
        )
        stompClient.send(Const.friendApplication, Gson().toJson(userRelation)).subscribe()
    }


    fun getUserRelationList(): MutableLiveData<MutableList<UserRelation>> {
        return userRelationList!!
    }
}