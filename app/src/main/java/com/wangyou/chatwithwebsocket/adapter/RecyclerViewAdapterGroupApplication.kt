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
        val groupRelation = groupRelationList[position]
        val user = userMap[groupRelation.uid]
        val group = groupMap[groupRelation.gid]
        // 如果当前申请未操作，且被申请人是自己才能进行同意操作
        holder.binding.role =
            if (groupRelation.enable == GroupRelation.NO_DEAL && groupRelation.uid != oneself.uid) {
                holder.binding.agreeApplication.setOnClickListener {
                    listener.agree(groupRelation.gid!!, groupRelation.uid!!)
                }
                0
            } else 1
        holder.binding.user = user
        holder.binding.root.setOnClickListener{
            listener.viewPersonalDetail(user?.uid!!, group?.gid!!)
        }
        holder.binding.group = group
        holder.binding.groupApplication.setOnClickListener {
            listener.viewGroupDetail(group!!)
        }
        holder.binding.applicationStatus.text = when (groupRelation.enable) {
            GroupRelation.NO_DEAL -> "响应中"
            GroupRelation.REFUSE -> "已拒绝"
            GroupRelation.AGREE -> "已同意"
            GroupRelation.DELETE -> "已删除"
            GroupRelation.DISMISS -> "已解散"
            else -> "状态异常"
        }
    }

    override fun getItemCount(): Int {
        return groupRelationList.size
    }

    class GroupApplicationHolder(var binding: ItemGroupApplicationBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    interface OnClickListener {
        fun agree(gid: Long, uid: Long)
        fun viewPersonalDetail(uid: Long, gid: Long)
        fun viewGroupDetail(group: Group)
    }

}