package com.wangyou.chatwithwebsocket.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.User

class UserListViewModel: ViewModel() {
    private var userList: MutableLiveData<MutableList<User>>? = null

    init {
        userList = MutableLiveData(mutableListOf<User>())
        for (i in 1L..10L) {
            val str = "${i}something"
            userList!!.value!!.add(User(
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
    }

    fun getUserList(): MutableLiveData<MutableList<User>>{
        return userList!!
    }

    fun addUser(user: User){
        userList!!.value!!.add(user)
        userList!!.value = userList!!.value
    }
}