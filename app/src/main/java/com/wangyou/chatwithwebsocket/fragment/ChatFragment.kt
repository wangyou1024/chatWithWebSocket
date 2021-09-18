package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.ChatViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment() {

    private var binding: FragmentChatBinding? = null
    private var navController: NavController? = null
    private val chatViewModel: ChatViewModel by activityViewModels<ChatViewModel>()
    private val personalViewModel: PersonalViewModel by activityViewModels<PersonalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding!!.lifecycleOwner = this
        binding!!.chatViewModel = chatViewModel
        binding!!.personalViewModel = personalViewModel
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    override fun onActivityResume() {
        super.onActivityResume()
        binding!!.backChat.setOnClickListener {
            navController!!.popBackStack()
        }
        val args = ChatFragmentArgs.fromBundle(requireArguments())
        binding!!.chatUser.text = args.cid
        chatViewModel.getChats().observe(requireActivity(), {
            binding!!.rvChat.adapter!!.notifyItemInserted(binding!!.rvChat.adapter!!.itemCount - 1)
            binding!!.rvChat.scrollToPosition(binding!!.rvChat.adapter!!.itemCount - 1)
        })
    }

}