package com.wangyou.chatwithwebsocket.adapter

import android.app.ActionBar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.ItemChatBinding
import com.wangyou.chatwithwebsocket.entity.Chat
import com.wangyou.chatwithwebsocket.entity.User

class RecyclerViewAdapterChat(
    var users: MutableMap<Long, User>?, var chats: MutableList<Chat>?,
    var personal: User?
) : RecyclerView.Adapter<RecyclerViewAdapterChat.TalkHolder>() {

    override fun getItemViewType(position: Int): Int {
        // 是否是本人/发送时间间隔是否超过1分钟
        return if(chats!![position].sender == personal!!.uid){
            if (position > 0 && chats!![position].updateTime!! - chats!![position-1].updateTime!! < 60L){
                SELF_LAST
            } else {
                SELF_NEW
            }
        } else {
            if (position > 0 && chats!![position].updateTime!! - chats!![position-1].updateTime!! < 60L){
                OTHER_LAST
            } else {
                OTHER_NEW
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkHolder {
        val binding = DataBindingUtil.inflate<ItemChatBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_chat,
            parent,
            false
        )
        // 身份
        when (viewType) {
            SELF_LAST, SELF_NEW -> {
                binding.headerOther.visibility = View.INVISIBLE
                binding.nameChat.gravity = Gravity.END
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )
                layoutParams.gravity = Gravity.END
                binding.cardViewContentChat.layoutParams = layoutParams
            }
            else -> {
                binding.headerSelf.visibility = View.INVISIBLE
                binding.nameChat.gravity = Gravity.START
                // 重新设置子布局的属性，避免由于recyclerView缓存机制导致的样式未能根据内容刷新
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )
                layoutParams.gravity = Gravity.START
                binding.cardViewContentChat.layoutParams = layoutParams
            }
        }
        // 时间标签是否显示
        when (viewType) {
            SELF_LAST, OTHER_LAST -> {
                binding.itemTime.visibility = View.GONE
            }
            else -> {
                binding.itemTime.visibility = View.VISIBLE
            }
        }
        return TalkHolder(binding)
    }

    override fun onBindViewHolder(holder: TalkHolder, position: Int) {
        val chat = chats!![position]
        holder.binding!!.chat = chat
        holder.binding!!.user = users!![chat.sender]
    }

    override fun getItemCount(): Int {
        return chats!!.size
    }

    class TalkHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemChatBinding? = null

        constructor(binding: ItemChatBinding?) : this(binding!!.root){
            this.binding = binding
        }

    }

    companion object {
        const val OTHER_LAST: Int = 0
        const val OTHER_NEW: Int = 1
        const val SELF_LAST: Int = 2
        const val SELF_NEW: Int = 4
    }
}