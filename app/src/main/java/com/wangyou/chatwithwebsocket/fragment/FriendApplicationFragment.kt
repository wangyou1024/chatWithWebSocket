package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.FriendApplicationViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.data.UserListViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentFriendApplicationBinding

class FriendApplicationFragment : Fragment() {

    private var binding: FragmentFriendApplicationBinding? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_friend_application,
            container,
            false
        )
        binding!!.friendApplicationViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(FriendApplicationViewModel::class.java)
        binding!!.userListViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(UserListViewModel::class.java)
        binding!!.oneself = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(PersonalViewModel::class.java).getPersonal().value
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding!!.backAddress.setOnClickListener {
            Log.i("return", "address")
            navController!!.popBackStack()
        }
    }
}