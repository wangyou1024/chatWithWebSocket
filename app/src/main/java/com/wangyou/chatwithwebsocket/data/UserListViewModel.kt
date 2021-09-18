package com.wangyou.chatwithwebsocket.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.conf.Const
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
        userMap = MutableLiveData(mutableMapOf())
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

    fun loadUserByIds(ids: MutableSet<Long>) {
        val list = mutableListOf<Long>(-1)
        list.addAll(ids)
        userServiceAPI.findUserListByIds(list)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                this.userList?.value = it
                associate()
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, ex.errorMsg)
                }
            })
    }

    fun findFriends() {
        userServiceAPI.findFriends()
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                this.userList?.value = it
                associate()
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "加载好友数据错误 -> ${ex.errorMsg}")
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

    fun associate() {
        userList!!.value?.let { it -> userMap!!.value?.putAll(it.associateBy { it.uid!! }) }
    }
}