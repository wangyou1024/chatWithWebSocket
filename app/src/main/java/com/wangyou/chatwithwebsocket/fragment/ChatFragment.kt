package com.wangyou.chatwithwebsocket.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.ChatViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentChatBinding
import com.wangyou.chatwithwebsocket.entity.Chat
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResume() {
        super.onActivityResume()
        binding!!.backChat.setOnClickListener {
            navController!!.popBackStack()
        }
        chatViewModel.getChats().observe(requireActivity(), {
            // 在群聊中可能突然加入一个新人
            if (it.size != 0 && it[0].enable == Chat.GROUP_CHAT){
                for (chat in it) {
                    if (chatViewModel.getUsers().value?.containsKey(chat.sender!!) == false
                        || chat.sender != personalViewModel.getSelf().value?.uid){
                        chatViewModel.loadLeader(chat.gid!!)
                    }
                }
            }
            binding!!.rvChat.adapter?.notifyItemInserted(it.size-1)
        })
        val args = ChatFragmentArgs.fromBundle(requireArguments())
        if (args.uid != "unknown"){
            chatViewModel.type.value = Chat.PRIVATE_CHAT
            chatViewModel.loadChat(args.uid.toLong())
        }else{
            chatViewModel.type.value = Chat.GROUP_CHAT
            chatViewModel.loadChat(args.gid.toLong())
        }
        // 设置聊天顶部名称
        chatViewModel.getUsers().observe(requireActivity(), {
            if (chatViewModel.type.value == Chat.PRIVATE_CHAT){
                binding!!.chatUser.text = chatViewModel.getUsers().value?.get(args.uid.toLong())?.realName
            }
            binding!!.rvChat.adapter?.notifyDataSetChanged()
        })
        chatViewModel.getGroup().observe(requireActivity(), {
            if (chatViewModel.type.value == Chat.GROUP_CHAT) {
                binding!!.chatUser.text = chatViewModel.getGroup().value?.groupName
            }
        })
        binding!!.tvMoreAction.setOnClickListener {
            if (args.uid != "unknown"){
                val bundle =
                    PersonalDetailFragmentArgs.Builder().setUid(args.uid).build().toBundle()
                if (!navController!!.popBackStack(R.id.personalDetailFragment, false)){
                    navController!!.navigate(R.id.personalDetailFragment, bundle)
                }
            } else {
                val bundle = GroupDetailFragmentArgs.Builder().setGid(args.gid).build().toBundle()
                if (!navController!!.popBackStack(R.id.groupDetailFragment, false)) {
                    navController!!.navigate(R.id.groupDetailFragment, bundle)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        chatViewModel.type.value = Chat.DISABLE
        Log.i(Const.TAG, "退出聊天")
    }

}