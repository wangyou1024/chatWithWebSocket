package com.wangyou.chatwithwebsocket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.ItemUserBinding
import com.wangyou.chatwithwebsocket.entity.User

class RecyclerViewAdapterUserList(var userList: MutableList<User>): RecyclerView.Adapter<RecyclerViewAdapterUserList.UserListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        val binding = DataBindingUtil.inflate<ItemUserBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_user,
            parent,
            false
        )
        return UserListHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListHolder, position: Int) {
        holder.binding.user = userList[position]
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserListHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){

    }
}