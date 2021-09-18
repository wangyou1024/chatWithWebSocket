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
import com.wangyou.chatwithwebsocket.data.SessionViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentSessionBinding
import com.wangyou.chatwithwebsocket.entity.Chat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionFragment : BaseFragment() {

    private var binding: FragmentSessionBinding? = null
    private val sessionViewModel: SessionViewModel by activityViewModels<SessionViewModel>()
    private val listener: RecyclerViewAdapterSession.SessionListener =
        object : RecyclerViewAdapterSession.SessionListener {
            override fun onClickListener(chat: Chat) {
                val bundle = ChatFragmentArgs.Builder()
                    .setCid(chat.cid.toString())
                    .build().toBundle()
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
        binding!!.listener = listener
        return binding!!.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResume() {
        super.onActivityResume()
        sessionViewModel.getChatList().observe(requireActivity(), {
            binding!!.rvSession.adapter?.notifyDataSetChanged()
        })
    }

}