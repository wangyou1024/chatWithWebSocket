package com.wangyou.chatwithwebsocket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.ItemSessionBinding
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.User

class RecyclerViewAdapterSession(
    var chats: MutableList<Chat>?,
    var userMap: MutableMap<Long, User>,
    var groupMap: MutableMap<Long, Group>,
    var self: User,
    var listener: SessionListener
) : RecyclerView.Adapter<RecyclerViewAdapterSession.SessionHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionHolder {
        val binding = DataBindingUtil.inflate<ItemSessionBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_session,
            parent,
            false
        )
        return SessionHolder(binding)
    }

    fun getAll(): MutableList<Chat> {
        return chats!!
    }

    override fun onBindViewHolder(holder: SessionHolder, position: Int) {
        val chat = chats!![position]
        holder.binding!!.chat = chat
        holder.binding!!.group = groupMap[chat.gid]
        // 如果是私聊，由于登录者不存在于userMap中，所以找到的即是对话的另一方
        if (chat.sender != self.uid){
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

    class SessionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemSessionBinding? = null

        constructor(binding: ItemSessionBinding?) : this(binding!!.root) {
            this.binding = binding
        }
    }

    interface SessionListener {
        fun onClickListener(chat: Chat)
    }
}