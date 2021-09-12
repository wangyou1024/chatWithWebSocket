package com.wangyou.chatwithwebsocket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.ItemGroupApplicationBinding
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.GroupRelation
import com.wangyou.chatwithwebsocket.entity.User

class RecyclerViewAdapterGroupApplication(
    var userMap: MutableMap<Long, User>,
    var groupMap: MutableMap<Long, Group>,
    var groupRelationList: MutableList<GroupRelation>,
    var oneself: User,
    var listener: OnClickListener
) :
    RecyclerView.Adapter<RecyclerViewAdapterGroupApplication.GroupApplicationHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupApplicationHolder {
        val binding = DataBindingUtil.inflate<ItemGroupApplicationBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_group_application,
            parent,
            false
        )
        return GroupApplicationHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupApplicationHolder, position: Int) {
        // 如果当前申请未操作，且被申请人是自己才能进行同意操作
        holder.binding.role =
            if (groupRelationList[position].enable == 0 && groupRelationList[position].uid != oneself.uid) {
                holder.binding.agreeApplication.setOnClickListener {
                    listener.agree(
                        groupRelationList[position].gid!!,
                        groupRelationList[position].uid!!
                    )
                }
                0
            } else 1
        holder.binding.user = userMap[groupRelationList[position].uid]
        holder.binding.root.setOnClickListener{
            listener.viewPersonalDetail(holder.binding.user!!)
        }
        holder.binding.group = groupMap[groupRelationList[position].gid]
        holder.binding.groupApplication.setOnClickListener {
            listener.viewGroupDetail(holder.binding.group!!)
        }
        holder.binding.applicationStatus.text = when (groupRelationList[position].enable) {
            0 -> "响应中"
            1 -> "已拒绝"
            else -> "已同意"
        }
    }

    override fun getItemCount(): Int {
        return groupRelationList.size
    }

    class GroupApplicationHolder(var binding: ItemGroupApplicationBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    interface OnClickListener {
        fun agree(former: Long, latter: Long)
        fun viewPersonalDetail(user: User)
        fun viewGroupDetail(group: Group)
    }

}