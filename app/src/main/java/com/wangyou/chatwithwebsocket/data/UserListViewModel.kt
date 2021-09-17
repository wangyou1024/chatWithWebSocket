package com.wangyou.chatwithwebsocket.data

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.net.api.UserServiceAPI
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    var toast: Toast,
    var userServiceAPI: UserServiceAPI,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
) : ViewModel() {
    private var userList: MutableLiveData<MutableList<User>>? = null
    private var userMap: MutableLiveData<MutableMap<Long, User>>? = null

    init {
        userList = MutableLiveData(mutableListOf<User>())
        for (i in 1L..10L) {
            val str = "${i}something"
            userList!!.value!!.add(
                User(
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
            )
        }
        userMap = MutableLiveData(mutableMapOf())
        userList!!.value?.let { it -> userMap!!.value?.putAll(it.associateBy { it.uid!! }) }
    }

    fun searchUserList(searchKey: String) {
        userServiceAPI.findUserListBySearchKey(searchKey)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                userList?.value = it
                associate()
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    toast.setText(ex.errorMsg)
                    toast.show()
                }

            })
    }

    fun getUserMap(): MutableLiveData<MutableMap<Long, User>> {
        return userMap!!
    }

    fun getUserList(): MutableLiveData<MutableList<User>> {
        return userList!!
    }

    fun addUser(user: User) {
        userList!!.value!!.add(user)
        userList!!.value = userList!!.value
    }

    fun associate(){
        userList!!.value?.let { it -> userMap!!.value?.putAll(it.associateBy { it.uid!!}) }
    }
}