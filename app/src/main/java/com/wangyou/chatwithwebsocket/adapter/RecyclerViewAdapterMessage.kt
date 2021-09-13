package com.wangyou.chatwithwebsocket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.ItemMessageBinding
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.User

class RecyclerViewAdapterMessage(
    var chats: MutableList<Chat>?,
    var userMap: MutableMap<Long, User>,
    var groupMap: MutableMap<Long, Group>,
    var listener: MessageListener
) : RecyclerView.Adapter<RecyclerViewAdapterMessage.MessageHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val binding = DataBindingUtil.inflate<ItemMessageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_message,
            parent,
            false
        )
        return MessageHolder(binding)
    }

    fun getAll(): MutableList<Chat> {
        return chats!!
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val chat = chats!![position]
        holder.binding!!.chat = chat
        holder.binding!!.group = groupMap[chat.gid]
        // 如果是私聊，由于登录者不存在于userMap中，所以找到的即是对话的另一方
        if (userMap[chat.sender] != null){
            holder.binding!!.user = userMap[chat.sender]
        }else{
            holder.binding!!.user = userMap[chat.recipient]
        }
        holder.binding!!.root.setOnClickListener {
            listener.onClickListener(chats!![position])
        }
    }

    override fun getItemCount(): Int {
        return chats!!.size
    }

    class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemMessageBinding? = null

        constructor(binding: ItemMessageBinding?) : this(binding!!.root) {
            this.binding = binding
        }
    }

    interface MessageListener {
        fun onClickListener(chat: Chat)
    }
}