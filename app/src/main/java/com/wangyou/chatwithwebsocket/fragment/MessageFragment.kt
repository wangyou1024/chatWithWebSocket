package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.ChatViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentMessageBinding
import com.wangyou.chatwithwebsocket.entity.Chat

class MessageFragment : Fragment() {

    private var binding: FragmentMessageBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false)
        binding!!.lifecycleOwner = this
        val chatViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(ChatViewModel::class.java)
        binding!!.chatViewModel = chatViewModel
        return binding!!.root
    }

}