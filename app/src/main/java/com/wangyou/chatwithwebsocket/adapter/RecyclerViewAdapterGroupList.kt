package com.wangyou.chatwithwebsocket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.ItemGroupBinding
import com.wangyou.chatwithwebsocket.entity.Group

class RecyclerViewAdapterGroupList(var groupList: MutableList<Group>): RecyclerView.Adapter<RecyclerViewAdapterGroupList.GroupListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListHolder {
        val binding = DataBindingUtil.inflate<ItemGroupBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_group,
            parent,
            false
        )
        return GroupListHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupListHolder, position: Int) {
        holder.binding.group = groupList[position]
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    class GroupListHolder(var binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root){

    }
}