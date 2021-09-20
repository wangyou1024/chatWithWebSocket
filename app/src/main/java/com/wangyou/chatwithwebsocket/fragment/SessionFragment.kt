package com.wangyou.chatwithwebsocket.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.adapter.RecyclerViewAdapterSession
import com.wangyou.chatwithwebsocket.data.GroupListViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.data.SessionViewModel
import com.wangyou.chatwithwebsocket.data.UserListViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentSessionBinding
import com.wangyou.chatwithwebsocket.entity.Chat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionFragment : BaseFragment() {

    private var binding: FragmentSessionBinding? = null
    private val sessionViewModel: SessionViewModel by activityViewModels<SessionViewModel>()
    private val userListViewModel: UserListViewModel by activityViewModels<UserListViewModel>()
    private val groupListViewModel: GroupListViewModel by activityViewModels<GroupListViewModel>()
    private val personalViewModel: PersonalViewModel by activityViewModels<PersonalViewModel>()
    private val listener: RecyclerViewAdapterSession.SessionListener =
        object : RecyclerViewAdapterSession.SessionListener {
            override fun onClickListener(chat: Chat) {
                val builder = ChatFragmentArgs.Builder()
                if (chat.enable == Chat.PRIVATE_CHAT){
                    if (personalViewModel.getSelf().value?.uid == chat.sender){
                        builder.setUid(chat.recipient.toString())
                    }else{
                        builder.setUid(chat.sender.toString())
                    }
                }else{
                    builder.setGid(chat.gid.toString())
                }
                val bundle = builder.build().toBundle()
                Navigation.findNavController(requireActivity(), R.id.fragmentAll)
                    .navigate(R.id.chatFragment, bundle)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session, container, false)
        binding!!.lifecycleOwner = this
        binding!!.sessionViewModel = sessionViewModel
        binding!!.userListViewModel = userListViewModel
        binding!!.groupListViewModel = groupListViewModel
        binding!!.listener = listener
        binding!!.personalViewModel = personalViewModel
        return binding!!.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResume() {
        super.onActivityResume()
        sessionViewModel.getChatList().observe(requireActivity(), {
            val uids = mutableSetOf<Long>()
            val gids = mutableSetOf<Long>()
            for (chat in it) {
                uids.add(chat.sender!!)
                uids.add(chat.recipient!!)
                gids.add(chat.gid!!)
            }
            userListViewModel.loadUserByIds(uids)
            groupListViewModel.loadGroupListByIds(gids)
            binding!!.rvSession.adapter?.notifyDataSetChanged()
        })
        userListViewModel.getUserList().observe(requireActivity(), {
            binding!!.rvSession.adapter?.notifyDataSetChanged()
        })
        groupListViewModel.getGroupList().observe(requireActivity(), {
            binding!!.rvSession.adapter?.notifyDataSetChanged()
        })
        sessionViewModel.loadSessionList()
    }

}