package com.wangyou.chatwithwebsocket.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.User

class PersonalViewModel: ViewModel() {
    private var personal: MutableLiveData<User>? = null
    // 当前登录者与展示对象的关系：0：自己；1：陌生人；2：好友
    private var relation: MutableLiveData<Int>? = null

    var username: MutableLiveData<String>? = null
    var realName: MutableLiveData<String>? = null
    var imageUrl: MutableLiveData<String>? = null
    var phone: MutableLiveData<String>? = null
    var age: MutableLiveData<String>? = null
    var address: MutableLiveData<String>? = null
    var email: MutableLiveData<String>? = null
    private var gender: MutableLiveData<Int>? = null
    var introduce: MutableLiveData<String>? = null

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
        relation = MutableLiveData(1)

        username = MutableLiveData(personal!!.value!!.username)
        realName = MutableLiveData(personal!!.value!!.realName)
        imageUrl = MutableLiveData(personal!!.value!!.imageUrl)
        phone = MutableLiveData(personal!!.value!!.phone)
        age = MutableLiveData(personal!!.value!!.age.toString())
        address = MutableLiveData(personal!!.value!!.address)
        email = MutableLiveData(personal!!.value!!.email)
        gender = MutableLiveData(personal!!.value!!.gender)
        introduce = MutableLiveData(personal!!.value!!.introduce)
    }

    fun getPersonal(): MutableLiveData<User>{
        return personal!!
    }

    fun setPersonal(user: MutableLiveData<User>){
        personal = user
    }

    fun getGender(): MutableLiveData<Int>{
        return gender!!
    }

    fun setGender(gender: Int){
        Log.i("gender", gender.toString())
        this.gender!!.value = gender
    }

    fun getRelation():MutableLiveData<Int>{
        return relation!!
    }

}