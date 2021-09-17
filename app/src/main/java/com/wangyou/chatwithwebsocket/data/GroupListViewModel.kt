package com.wangyou.chatwithwebsocket.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangyou.chatwithwebsocket.entity.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor() : ViewModel() {
    private var groupList: MutableLiveData<MutableList<Group>>? = null

    init {
        groupList = MutableLiveData(mutableListOf<Group>())
        for (i in 1L..10L) {
            val str = "${i}something"
            groupList!!.value!!.add(
                Group(
                    i,
                    str,
                    str,
                    str,
                    str,
                    i.toInt(),
                    i.toInt(),
                )
            )
        }
    }

    fun getGroupList(): MutableLiveData<MutableList<Group>> {
        return groupList!!
    }

    fun addGroup(group: Group) {
        groupList!!.value!!.add(group)
        groupList!!.value = groupList!!.value
    }
}