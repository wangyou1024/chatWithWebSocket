package com.wangyou.chatwithwebsocket.data

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchContentViewModel @Inject constructor() : ViewModel() {
    private var searchContent: ObservableField<String> = ObservableField("");

    fun getSearchContent():String{
        return searchContent.get()!!
    }

    fun setSearchContent(searchContent: String){
        this.searchContent.set(searchContent)
    }
}