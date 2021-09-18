package com.wangyou.chatwithwebsocket.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(): ViewModel() {

    private var group: MutableLiveData<Group>? = null
    private var groupLeader: MutableLiveData<User>? = null
    private var groupMembers: MutableLiveData<MutableList<User>>? = null
    // 登录者相对于当前群的角色：0：群主；1：群成员；2：陌生人
    private var role: MutableLiveData<Int>? = null

    init {
        group = MutableLiveData(Group(1, "群聊", "无名", "something", "测试群", 1, 1))
        groupLeader = MutableLiveData(User(1L, "群主", "" ,"群主", "kdjks","12434567732", 23, "重庆", "@qq.com", 0, "自称", 1, 1, 1))
        groupMembers = MutableLiveData(mutableListOf<User>())
        for (i in 1L..10L) {
            val str = "${i}something"
            groupMembers!!.value!!.add(User(
                i,
                str,
                str,
                str,
                str,
                str,
                i.toInt(),
                str,
                str,
                i.toInt(),
                str,
                i.toInt(),
                i.toInt(),
                i.toInt()
            ))
        }
        role = MutableLiveData(0)
    }

    fun createGroup(){
        val newGroup = Group(-1, "", "", "", "", 0, 1)
        group?.value = newGroup
        role?.value = 0
    }

    fun saveGroup(){
        Log.i(Const.TAG, "保存：${group?.value?.groupName}+${group?.value?.introduce}")
    }

    fun getGroup(): MutableLiveData<Group>{
        return group!!
    }

    fun getGroupLeader(): MutableLiveData<User>{
        return groupLeader!!
    }

    fun getGroupMembers(): MutableLiveData<MutableList<User>>{
        return groupMembers!!
    }

    fun getRole(): MutableLiveData<Int>{
        return role!!
    }
}