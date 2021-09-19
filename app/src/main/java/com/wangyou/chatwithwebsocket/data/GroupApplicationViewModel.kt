package com.wangyou.chatwithwebsocket.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.GroupRelation
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.entity.UserRelation
import com.wangyou.chatwithwebsocket.net.api.GroupRelationServiceAPI
import com.wangyou.chatwithwebsocket.net.client.StompClientLifecycle
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupApplicationViewModel @Inject constructor(
    var stompClientLifecycle: StompClientLifecycle,
    val groupRelationServiceAPI: GroupRelationServiceAPI,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
) : ViewModel() {
    private var groupRelationList: MutableLiveData<MutableList<GroupRelation>> =
        MutableLiveData(mutableListOf())

    fun loadGroupRelationList() {
        groupRelationServiceAPI.findRelationList()
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "获取关系 -> ${it.size}")
                groupRelationList.value?.clear()
                groupRelationList.value?.addAll(it)
                groupRelationList.value = groupRelationList.value
                stompClientLifecycle.setGroupRelationList(groupRelationList)
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "获取关系 -> ${ex.errorMsg}")
                }

            })
    }

    fun getGroupRelationList(): MutableLiveData<MutableList<GroupRelation>> {
        return groupRelationList
    }
}