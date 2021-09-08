package com.wangyou.chatwithwebsocket.adapter

import android.app.Activity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.fragment.ChatFragmentArgs

object RecyclerViewBindingAdapter {

    @BindingAdapter(value = ["bindChat"])
    @JvmStatic
    fun bindMessage(recyclerView: RecyclerView?, list: MutableLiveData<MutableList<Chat>>?){
        var data: MutableLiveData<MutableList<Chat>> = list ?: MutableLiveData(ArrayList<Chat>())
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterMessage(list!!.value, object :RecyclerViewAdapterMessage.MessageListener{
            override fun onClickListener(chat: Chat) {
                val bundle = ChatFragmentArgs.Builder()
                        .setUserName(chat.name!!)
                        .build().toBundle()
                bundle.putSerializable("chat", chat)
                Navigation.findNavController(recyclerView.context as Activity, R.id.fragment).navigate(R.id.to_chatFragment, bundle)
            }
        })
        list.observe(recyclerView.context as Activity as LifecycleOwner, {
            recyclerView.adapter!!.notifyItemInserted(recyclerView.adapter!!.itemCount-1)
        })
    }
}