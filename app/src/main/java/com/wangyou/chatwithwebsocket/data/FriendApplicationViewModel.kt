package com.wangyou.chatwithwebsocket.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.entity.UserRelation

class FriendApplicationViewModel(): ViewModel() {
    private var userMap: MutableLiveData<MutableMap<Long, User>>? = null
    private var userRelationList: MutableLiveData<MutableList<UserRelation>>? = null

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
        userRelationList = MutableLiveData(mutableListOf())
        userRelationList!!.value!!.add(UserRelation(1, 1, 2, 1, 1, 0))
        userRelationList!!.value!!.add(UserRelation(2, 1, 3, 1, 1, 1))
        userRelationList!!.value!!.add(UserRelation(3, 1, 4, 1, 1, 2))
        userRelationList!!.value!!.add(UserRelation(4, 5, 1, 1, 1, 0))
        userRelationList!!.value!!.add(UserRelation(5, 6, 1, 1, 1, 1))
        userRelationList!!.value!!.add(UserRelation(6, 7, 1, 1, 1, 2))
    }

    fun getUserMap(): MutableLiveData<MutableMap<Long, User>> {
        return userMap!!
    }

    fun getUserRelationList(): MutableLiveData<MutableList<UserRelation>>{
        return userRelationList!!
    }
}