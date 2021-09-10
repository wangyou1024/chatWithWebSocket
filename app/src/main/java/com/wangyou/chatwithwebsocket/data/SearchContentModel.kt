package com.wangyou.chatwithwebsocket.data

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class SearchContentModel() : ViewModel() {
    private var searchContent: ObservableField<String>? = null;

    init {
        this.searchContent = ObservableField("")
    }

    fun getSearchContent():String?{
        return searchContent?.get()
    }

    fun setSearchContent(searchContent: String){
        this.searchContent?.set(searchContent)
    }
}