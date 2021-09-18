package com.wangyou.chatwithwebsocket.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.net.api.GroupServiceAPI
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    var toast: Toast,
    var groupServiceAPI: GroupServiceAPI,
    var compositeDisposableLifecycle: CompositeDisposableLifecycle
) : ViewModel() {
    private var groupList: MutableLiveData<MutableList<Group>> = MutableLiveData(mutableListOf<Group>())

    fun searchGroupList(searchKey : String) {
        groupServiceAPI.searchGroups(searchKey)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                groupList.value = it
                Log.i(Const.TAG, "搜索群聊成功${it.size}")
            }, object : ErrorConsumer(){
                override fun error(ex: APIException) {
                    toast.setText(ex.errorMsg)
                    toast.show()
                }

            })
    }

    fun loadJoinedGroupList(){
        groupServiceAPI.findJoinedGroups()
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                groupList.value = it
                Log.i(Const.TAG, "加载已加入群聊成功")
            }, object : ErrorConsumer(){
                override fun error(ex: APIException) {
                    toast.setText(ex.errorMsg)
                    toast.show()
                }
            })
    }

    fun getGroupList(): MutableLiveData<MutableList<Group>> {
        return groupList
    }

    fun addGroup(group: Group) {
        groupList.value!!.add(group)
        groupList.value = groupList.value
    }
}