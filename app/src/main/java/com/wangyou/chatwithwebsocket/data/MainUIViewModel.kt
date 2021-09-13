package com.wangyou.chatwithwebsocket.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainUIViewModel: ViewModel() {
    private var page : MutableLiveData<Int> = MutableLiveData(0)

    fun getPage(): Int{
        return page.value!!
    }

    fun setPage(page: Int){
        this.page.value = page
    }
}