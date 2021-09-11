package com.wangyou.chatwithwebsocket.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.GroupRelation
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.entity.UserRelation

class GroupApplicationViewModel(): ViewModel() {
    private var userMap: MutableLiveData<MutableMap<Long, User>>? = null
    private var groupMap: MutableLiveData<MutableMap<Long, Group>>? = null
    private var groupRelationList: MutableLiveData<MutableList<GroupRelation>>? = null

    init {
        userMap = MutableLiveData(mutableMapOf())
        for (i in 1L..6L) {
            val str = "${i}something"
            userMap!!.value!![i.toLong()] = User(
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
            )
        }
        groupMap = MutableLiveData(mutableMapOf())
        for (i in 1L..6L) {
            val str = "${i}something"
            groupMap!!.value!![i.toLong()] = Group(
                i,
                str,
                str,
                str,
                str,
                i.toInt(),
                1
            )
        }
        groupRelationList = MutableLiveData(mutableListOf())
        groupRelationList!!.value!!.add(GroupRelation(1, 1, 2, 1, 1, 0))
        groupRelationList!!.value!!.add(GroupRelation(2, 1, 3, 1, 1, 1))
        groupRelationList!!.value!!.add(GroupRelation(3, 1, 4, 1, 1, 2))
        groupRelationList!!.value!!.add(GroupRelation(4, 2, 1, 1, 1, 0))
        groupRelationList!!.value!!.add(GroupRelation(5, 3, 1, 1, 1, 1))
        groupRelationList!!.value!!.add(GroupRelation(6, 4, 1, 1, 1, 2))
    }

    fun getUserMap(): MutableLiveData<MutableMap<Long, User>> {
        return userMap!!
    }

    fun getGroupMap(): MutableLiveData<MutableMap<Long, Group>> {
        return groupMap!!
    }

    fun getGroupRelationList(): MutableLiveData<MutableList<GroupRelation>>{
        return groupRelationList!!
    }
}