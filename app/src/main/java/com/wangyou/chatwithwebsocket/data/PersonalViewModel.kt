package com.wangyou.chatwithwebsocket.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.User

class PersonalViewModel: ViewModel() {
    private var personal: MutableLiveData<User>? = null

    init {
        val i = 1L
        val str = "name"
        personal = MutableLiveData<User>(User(
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

    fun getPersonal(): MutableLiveData<User>{
        return personal!!
    }

}