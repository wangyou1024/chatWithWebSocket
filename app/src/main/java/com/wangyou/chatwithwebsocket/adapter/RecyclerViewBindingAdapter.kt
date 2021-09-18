package com.wangyou.chatwithwebsocket.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.entity.*

object RecyclerViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["bindSession", "talkerMap", "groupMap", "sessionListener"])
    fun bindChat(
        recyclerView: RecyclerView?,
        chats: MutableLiveData<MutableList<Chat>>?,
        talkerMap: MutableLiveData<MutableMap<Long, User>>?,
        groupMap: MutableLiveData<MutableMap<Long, Group>>?,
        listener: RecyclerViewAdapterSession.SessionListener?
    ) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterSession(
                chats!!.value,
                talkerMap!!.value!!,
                groupMap!!.value!!,
                listener!!)
    }

    @JvmStatic
    @BindingAdapter(value = ["chats", "speakers", "personal"], requireAll = false)
    fun bindTalk(
        recyclerView: RecyclerView?,
        chats: MutableLiveData<MutableList<Chat>>?,
        speakers: MutableLiveData<MutableMap<Long, User>>?,
        personal: MutableLiveData<User>?
    ) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterChat(speakers!!.value, chats!!.value, personal!!.value)
    }

    @JvmStatic
    @BindingAdapter(value = ["userList", "userListOnClick"], requireAll = false)
    fun bindUserList(
        recyclerView: RecyclerView,
        userList: MutableLiveData<MutableList<User>>,
        onClickListener: RecyclerViewAdapterUserList.OnClickListener?
    ) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        val users = userList.value
        recyclerView.adapter =
            RecyclerViewAdapterUserList(
                users!!,
                onClickListener ?: object : RecyclerViewAdapterUserList.OnClickListener {
                    override fun viewDetailPerson(user: User) {
                        Log.i(Const.TAG, "未实现")
                    }

                })
        // 停止动画，避免闪烁
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    @JvmStatic
    @BindingAdapter(value = ["groupList"])
    fun bindGroupList(
        recyclerView: RecyclerView?,
        groupList: MutableLiveData<MutableList<Group>>?
    ) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        val groups = if (groupList != null) groupList.value else mutableListOf<Group>()
        recyclerView.adapter =
            RecyclerViewAdapterGroupList(
                groups!!,
                object : RecyclerViewAdapterGroupList.OnClickListener {
                    override fun enterGroupDetail(group: Group) {
                        Navigation.findNavController(
                            recyclerView.context as Activity,
                            R.id.fragmentAll
                        ).navigate(R.id.groupDetailFragment)
                    }

                })
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    @JvmStatic
    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter(value = ["friendApplicationUserMap", "userRelationList", "friendApplicationLogin", "friendApplicationListener"])
    fun bindFriendApplication(
        recyclerView: RecyclerView?,
        userMap: MutableLiveData<MutableMap<Long, User>>,
        userRelationList: MutableLiveData<MutableList<UserRelation>>,
        friendApplicationLogin: User,
        listener: RecyclerViewAdapterFriendApplication.OnClickListener

    ) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterFriendApplication(
                userMap.value!!,
                userRelationList.value!!,
                friendApplicationLogin,
                listener
            )
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    @JvmStatic
    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter(value = ["groupApplicationUserMap", "groupApplicationGroupMap", "groupRelationList", "groupApplicationLogin", "groupApplicationListener"])
    fun bindGroupApplication(
        recyclerView: RecyclerView?,
        userMap: MutableLiveData<MutableMap<Long, User>>,
        groupMap: MutableLiveData<MutableMap<Long, Group>>,
        groupRelationList: MutableLiveData<MutableList<GroupRelation>>,
        groupApplicationLogin: User,
        groupApplicationListener: RecyclerViewAdapterGroupApplication.OnClickListener
    ) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterGroupApplication(
                userMap.value!!,
                groupMap.value!!,
                groupRelationList.value!!,
                groupApplicationLogin,
                groupApplicationListener
            )
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

}