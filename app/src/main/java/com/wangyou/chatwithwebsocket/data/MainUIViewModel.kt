package com.wangyou.chatwithwebsocket.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainUIViewModel @Inject constructor(): ViewModel() {
    // 0代表聊天页面，1代表联系人，2代表个人中心
    private var page : MutableLiveData<Int> = MutableLiveData(0)
    // 0代表好友，1代表群聊
    private var address: MutableLiveData<Int> = MutableLiveData(0)

    fun getAddress(): Int {
        return address.value!!
    }

    fun setAddress(address: Int) {
        this.address.value = address
    }

    fun getPage(): Int{
        return page.value!!
    }

    fun setPage(page: Int){
        this.page.value = page
    }
}