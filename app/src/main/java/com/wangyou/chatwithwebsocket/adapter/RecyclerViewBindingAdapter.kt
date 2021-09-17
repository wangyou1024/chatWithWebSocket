package com.wangyou.chatwithwebsocket.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.FriendApplicationViewModel
import com.wangyou.chatwithwebsocket.data.GroupApplicationViewModel
import com.wangyou.chatwithwebsocket.entity.*
import com.wangyou.chatwithwebsocket.fragment.ChatFragmentArgs
import com.wangyou.chatwithwebsocket.fragment.GroupDetailFragmentArgs
import com.wangyou.chatwithwebsocket.fragment.PersonalDetailFragmentArgs

object RecyclerViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["bindSession", "talkerMap", "groupMap"])
    fun bindChat(
        recyclerView: RecyclerView?,
        chats: MutableLiveData<MutableList<Chat>>?,
        talkerMap: MutableLiveData<MutableMap<Long, User>>?,
        groupMap: MutableLiveData<MutableMap<Long, Group>>?,
    ) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterMessage(
                chats!!.value,
                talkerMap!!.value!!,
                groupMap!!.value!!,
                object : RecyclerViewAdapterMessage.MessageListener {
                    override fun onClickListener(chat: Chat) {
                        val bundle = ChatFragmentArgs.Builder()
                            .setCid(chat.cid.toString())
                            .build().toBundle()
                        Navigation.findNavController(
                            recyclerView.context as Activity,
                            R.id.fragmentAll
                        ).navigate(R.id.chatFragment, bundle)
                    }
                })
        chats.observe(recyclerView.context as LifecycleOwner, {
            recyclerView.adapter!!.notifyItemInserted(recyclerView.adapter!!.itemCount - 1)
        })
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
            RecyclerViewAdapterTalk(speakers!!.value, chats!!.value, personal!!.value)
        chats.observe(recyclerView.context as LifecycleOwner,
            Observer {
                recyclerView.adapter!!.notifyItemInserted(recyclerView.adapter!!.itemCount - 1)
                recyclerView.scrollToPosition(recyclerView.adapter!!.itemCount - 1)
            })
    }

    @JvmStatic
    @BindingAdapter(value = ["userList", "userListOnClick"], requireAll = false)
    fun bindUserList(recyclerView: RecyclerView, userList: MutableLiveData<MutableList<User>>, onClickListener: RecyclerViewAdapterUserList.OnClickListener?) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        val users = userList.value
        recyclerView.adapter =
            RecyclerViewAdapterUserList(users!!, onClickListener?:object : RecyclerViewAdapterUserList.OnClickListener{
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
    @BindingAdapter(value = ["friendApplicationUserMap", "userRelationList", "friendApplicationLogin"])
    fun bindFriendApplication(
        recyclerView: RecyclerView?,
        userMap: MutableLiveData<MutableMap<Long, User>>,
        userRelationList: MutableLiveData<MutableList<UserRelation>>,
        friendApplicationLogin: User
    ) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterFriendApplication(
                userMap.value!!,
                userRelationList.value!!,
                friendApplicationLogin,
                object : RecyclerViewAdapterFriendApplication.OnClickListener {
                    override fun agree(former: Long, latter: Long) {
                        Log.i("agree", "${former}申请成为${latter}好友")
                    }

                    override fun viewPersonalDetail(user: User) {
                        val bundle =
                            PersonalDetailFragmentArgs.Builder().setUid(user.uid.toString()).build()
                                .toBundle()
                        Navigation.findNavController(
                            recyclerView.context as Activity,
                            R.id.fragmentAll
                        ).navigate(R.id.personalDetailFragment, bundle)
                    }
                })
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        userRelationList.observe(recyclerView.context as LifecycleOwner, {
                recyclerView.adapter!!.notifyDataSetChanged()
            })
    }

    @JvmStatic
    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter(value = ["groupApplicationUserMap","groupApplicationGroupMap", "groupRelationList", "groupApplicationLogin"])
    fun bindGroupApplication(
        recyclerView: RecyclerView?,
        userMap: MutableLiveData<MutableMap<Long, User>>,
        groupMap: MutableLiveData<MutableMap<Long, Group>>,
        groupRelationList: MutableLiveData<MutableList<GroupRelation>>,
        groupApplicationLogin: User
    ) {
        recyclerView!!.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter =
            RecyclerViewAdapterGroupApplication(
                userMap.value!!,
                groupMap.value!!,
                groupRelationList.value!!,
                groupApplicationLogin,
                object : RecyclerViewAdapterGroupApplication.OnClickListener {
                    override fun agree(former: Long, latter: Long) {
                        Log.i("agree", "${former}申请成为${latter}好友")
                    }

                    override fun viewPersonalDetail(user: User) {
                        val bundle =
                            PersonalDetailFragmentArgs.Builder().setUid(user.uid.toString()).build()
                                .toBundle()
                        Navigation.findNavController(
                            recyclerView.context as Activity,
                            R.id.fragmentAll
                        ).navigate(R.id.personalDetailFragment, bundle)
                    }

                    override fun viewGroupDetail(group: Group) {
                        val bundle =
                            GroupDetailFragmentArgs.Builder().setGid(group.gid.toString()).build()
                                .toBundle()
                        Navigation.findNavController(
                            recyclerView.context as Activity,
                            R.id.fragmentAll
                        ).navigate(R.id.groupDetailFragment, bundle)
                    }
                })
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        groupRelationList.observe(recyclerView.context as LifecycleOwner, {
                recyclerView.adapter!!.notifyDataSetChanged()
            })
    }

}