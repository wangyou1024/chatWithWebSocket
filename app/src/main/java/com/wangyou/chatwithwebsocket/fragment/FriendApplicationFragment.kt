package com.wangyou.chatwithwebsocket.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.adapter.RecyclerViewAdapterFriendApplication
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.FriendApplicationViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.data.UserListViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentFriendApplicationBinding
import com.wangyou.chatwithwebsocket.entity.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendApplicationFragment : BaseFragment() {

    private var binding: FragmentFriendApplicationBinding? = null
    private var navController: NavController? = null
    private val friendApplicationViewModel by activityViewModels<FriendApplicationViewModel>()
    private val userListViewModel by activityViewModels<UserListViewModel>()
    private val personalViewModel by activityViewModels<PersonalViewModel>()
    private val clickListener: RecyclerViewAdapterFriendApplication.OnClickListener =
        object : RecyclerViewAdapterFriendApplication.OnClickListener {
            override fun agree(former: Long, latter: Long) {
                friendApplicationViewModel.agreeApplication(former, latter)
            }
            override fun viewPersonalDetail(user: User) {
                val bundle =
                    PersonalDetailFragmentArgs.Builder().setUid(user.uid.toString()).build()
                        .toBundle()
                Navigation.findNavController(
                    requireActivity(),
                    R.id.fragmentAll
                ).navigate(R.id.personalDetailFragment, bundle)
            }
        }


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
        binding!!.lifecycleOwner = this
        binding!!.friendApplicationViewModel = friendApplicationViewModel
        binding!!.userListViewModel = userListViewModel
        binding!!.oneself = personalViewModel.getSelf().value
        binding!!.listener = clickListener
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResume() {
        super.onActivityResume()
        friendApplicationViewModel.getUserRelationList().observe(requireActivity(), { list ->
            Log.i(Const.TAG, "userRelation更新")
            val ids = mutableSetOf<Long>()
            list.forEach {
                ids.add(it.uidFormer!!)
                ids.add(it.uidLatter!!)
            }
            userListViewModel.loadUserByIds(ids)
             binding?.rvRelation?.adapter?.notifyDataSetChanged()
        })
        userListViewModel.getUserList().observe(requireActivity(), {
            binding?.rvRelation?.adapter?.notifyDataSetChanged()
        })
        friendApplicationViewModel.loadUserRelationList()
        binding!!.backAddress.setOnClickListener {
            Log.i("return", "address")
            navController!!.popBackStack()
        }
    }

}