package com.wangyou.chatwithwebsocket.adapter

import android.annotation.SuppressLint
import android.app.Activity
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.fragment.ChatFragmentArgs

object RecyclerViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["bindChat"])
    fun bindChat(recyclerView: RecyclerView?, list: MutableLiveData<MutableList<Chat>>?) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterMessage(
                list!!.value,
                object : RecyclerViewAdapterMessage.MessageListener {
                    override fun onClickListener(chat: Chat) {
                        val bundle = ChatFragmentArgs.Builder()
                            .setUserName(chat.name!!)
                            .build().toBundle()
                        bundle.putSerializable("chat", chat)
                        Navigation.findNavController(
                            recyclerView.context as Activity,
                            R.id.fragmentAll
                        ).navigate(R.id.to_chatFragment, bundle)
                    }
                })
        list.observe(recyclerView.context as LifecycleOwner, {
            recyclerView.adapter!!.notifyItemInserted(recyclerView.adapter!!.itemCount - 1)
        })
    }

    @JvmStatic
    @BindingAdapter(value = ["chats", "users", "personal"], requireAll = false)
    fun bindTalk(
        recyclerView: RecyclerView?,
        chats: MutableLiveData<MutableList<Chat>>?,
        users: MutableLiveData<MutableMap<Long, User>>?,
        personal: MutableLiveData<User>?
    ) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterTalk(users!!.value, chats!!.value, personal!!.value)
        chats.observe(recyclerView.context as LifecycleOwner,
            Observer {
                recyclerView.adapter!!.notifyItemInserted(recyclerView.adapter!!.itemCount - 1)
                recyclerView.scrollToPosition(recyclerView.adapter!!.itemCount - 1)
            })
    }

    @JvmStatic
    @BindingAdapter(value = ["userList"])
    @SuppressLint("NotifyDataSetChanged")
    fun bindUserList(recyclerView: RecyclerView?, userList: MutableLiveData<MutableList<User>>?) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        val users = if (userList != null) userList.value else mutableListOf<User>()
        recyclerView.adapter =
            RecyclerViewAdapterUserList(users!!)
        // 停止动画，避免闪烁
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        userList!!.observe(recyclerView.context as LifecycleOwner, {
            recyclerView.adapter!!.notifyDataSetChanged()
        })
    }

    @JvmStatic
    @BindingAdapter(value = ["groupList"])
    @SuppressLint("NotifyDataSetChanged")
    fun bindGroupList(recyclerView: RecyclerView?, groupList: MutableLiveData<MutableList<Group>>?) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        val groups = if (groupList != null) groupList.value else mutableListOf<Group>()
        recyclerView.adapter =
            RecyclerViewAdapterGroupList(groups!!)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        groupList!!.observe(recyclerView.context as LifecycleOwner, {
            recyclerView.adapter!!.notifyDataSetChanged()
        })
    }

}