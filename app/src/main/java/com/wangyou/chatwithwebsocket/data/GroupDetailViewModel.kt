package com.wangyou.chatwithwebsocket.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.GroupRelation
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.net.api.GroupRelationServiceAPI
import com.wangyou.chatwithwebsocket.net.api.GroupServiceAPI
import com.wangyou.chatwithwebsocket.net.api.UserServiceAPI
import com.wangyou.chatwithwebsocket.net.client.StompClientLifecycle
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import com.wangyou.chatwithwebsocket.util.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.naiksoftware.stomp.StompClient
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    var toast: Toast,
    var stompClient: StompClient,
    var userServiceAPI: UserServiceAPI,
    var groupServiceAPI: GroupServiceAPI,
    var stompClientLifecycle: StompClientLifecycle,
    var groupRelationServiceAPI: GroupRelationServiceAPI,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
) : ViewModel() {

    private var group: MutableLiveData<Group> = MutableLiveData(Group())
    private var groupLeader: MutableLiveData<User> = MutableLiveData(User())
    private var groupMembers: MutableLiveData<MutableList<User>> = MutableLiveData(mutableListOf())

    // 登录者相对于当前群的角色：0：群主；1：群成员；2：陌生人
    private var role: MutableLiveData<Int> = MutableLiveData(2)

    fun sendGroupApplication() {
        if (!stompClient.isConnected) {
            stompClientLifecycle.connect()
        }
        val groupRelation = GroupRelation(
            0,
            group.value?.gid,
            null,
            DateTimeUtil.getTimeNow().toInt(),
            DateTimeUtil.getTimeNow().toInt(),
            GroupRelation.NO_DEAL
        )
        stompClient.send(Const.groupApplication, Gson().toJson(groupRelation)).subscribe()
    }

    fun exitGroup(){
        if (!stompClient.isConnected){
            stompClientLifecycle.connect()
        }
        val groupRelation = GroupRelation(
            0,
            group.value?.gid,
            null,
            DateTimeUtil.getTimeNow().toInt(),
            DateTimeUtil.getTimeNow().toInt(),
            GroupRelation.DELETE
        )
        stompClient.send(Const.groupApplication, Gson().toJson(groupRelation)).subscribe()
    }

    fun dismiss(){
        if (!stompClient.isConnected){
            stompClientLifecycle.connect()
        }
        val groupRelation = GroupRelation(
            0,
            group.value?.gid,
            null,
            DateTimeUtil.getTimeNow().toInt(),
            DateTimeUtil.getTimeNow().toInt(),
            GroupRelation.DISMISS
        )
        stompClient.send(Const.groupApplication, Gson().toJson(groupRelation)).subscribe()
    }

    fun loadGroup(gid: Long) {
        groupServiceAPI.findGroupById(gid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "获取群聊 -> $it")
                group.value = it
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "获取群聊-> ${ex.errorMsg}")
                }
            })
        loadLeader(gid)
        loadMember(gid)
        loadRelation(gid)
    }

    private fun loadLeader(gid: Long) {
        userServiceAPI.findLeader(gid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "获取群主 -> $it")
                groupLeader.value = it
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "获取群主 -> ${ex.errorMsg}")
                }
            })
    }

    private fun loadMember(gid: Long) {
        userServiceAPI.findMembers(gid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "获取群成员 -> ${it.size}")
                groupMembers.value?.clear()
                groupMembers.value?.addAll(it)
                groupMembers.value = groupMembers.value
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "获取群成员 -> ${ex.errorMsg}")
                }
            })
    }

    private fun loadRelation(gid: Long) {
        groupRelationServiceAPI.findRelation(gid)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                Log.i(Const.TAG, "获取关系 -> $it")
                if (it != null) {
                    when (it.enable) {
                        GroupRelation.LEADER -> role.value = 0
                        GroupRelation.AGREE -> role.value = 1
                        else -> role.value = 2
                    }
                } else {
                    role.value = 2
                }
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "获取关系 -> ${ex.errorMsg}")
                }
            })
    }

    fun createGroup() {
        val newGroup = Group(-1, "", "", "", "", 0, 1)
        group.value = newGroup
        role.value = 0
    }

    fun saveGroup() {
        if (group.value?.gid == -1L) {
            groupServiceAPI.createGroup(group.value!!)
                .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
                .subscribe({
                    group.value = it
                    toast.setText("创建成功")
                    toast.show()
                }, object : ErrorConsumer() {
                    override fun error(ex: APIException) {
                        toast.setText(ex.errorMsg)
                        toast.show()
                    }
                })
        } else {
            groupServiceAPI.updateGroup(group.value!!)
                .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
                .subscribe({
                    group.value = it
                    toast.setText("修改成功")
                    toast.show()
                }, object : ErrorConsumer(){
                    override fun error(ex: APIException) {
                        Log.i(Const.TAG, "修改群信息 -> ${ex.errorMsg}")
                        toast.setText("修改失败")
                        toast.show()
                    }

                })
        }
    }

    fun getGroup(): MutableLiveData<Group> {
        return group
    }

    fun getGroupLeader(): MutableLiveData<User> {
        return groupLeader
    }

    fun getGroupMembers(): MutableLiveData<MutableList<User>> {
        return groupMembers
    }

    fun getRole(): MutableLiveData<Int> {
        return role
    }
}