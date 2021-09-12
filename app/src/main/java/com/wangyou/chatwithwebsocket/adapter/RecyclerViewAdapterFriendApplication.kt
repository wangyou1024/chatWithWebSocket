package com.wangyou.chatwithwebsocket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.ItemFriendApplicationBinding
import com.wangyou.chatwithwebsocket.entity.User
import com.wangyou.chatwithwebsocket.entity.UserRelation

class RecyclerViewAdapterFriendApplication(
    var userMap: MutableMap<Long, User>,
    var userRelationList: MutableList<UserRelation>,
    var oneself: User,
    var listener: OnClickListener
) :
    RecyclerView.Adapter<RecyclerViewAdapterFriendApplication.FriendApplicationHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendApplicationHolder {
        val binding = DataBindingUtil.inflate<ItemFriendApplicationBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_friend_application,
            parent,
            false
        )
        return FriendApplicationHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendApplicationHolder, position: Int) {
        // 如果当前申请未操作，且被申请人是自己才能进行同意操作
        holder.binding.role = if (userRelationList[position].enable == 0 && userRelationList[position].uidLatter == oneself.uid) {
            holder.binding.agreeApplication.setOnClickListener{
                listener.agree(userRelationList[position].uidFormer!!, userRelationList[position].uidLatter!!)
            }
            0
        } else 1
        // 判断自己的角色
        if (userRelationList[position].uidLatter == oneself.uid){
            holder.binding.user = userMap[userRelationList[position].uidFormer]

        } else {
            holder.binding.user = userMap[userRelationList[position].uidLatter]
        }
        holder.binding.root.setOnClickListener{
            listener.viewPersonalDetail(holder.binding.user!!)
        }
        holder.binding.applicationStatus.text = when (userRelationList[position].enable) {
            0 -> "响应中"
            1 -> "已拒绝"
            else -> "已同意"
        }
    }

    override fun getItemCount(): Int {
        return userRelationList.size
    }

    class FriendApplicationHolder(var binding: ItemFriendApplicationBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    interface OnClickListener{
        fun agree(former: Long, latter: Long)
        fun viewPersonalDetail(user: User)
    }

}