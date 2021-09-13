package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.data.TalkViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var binding: FragmentChatBinding? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding!!.lifecycleOwner = this
        binding!!.talkViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(TalkViewModel::class.java)
        binding!!.personalViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(PersonalViewModel::class.java)
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding!!.backChat.setOnClickListener {
            navController!!.popBackStack()
        }
        val args = ChatFragmentArgs.fromBundle(requireArguments())
        binding!!.chatUser.text = args.cid
    }

}