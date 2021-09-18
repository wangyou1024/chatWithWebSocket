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
import com.wangyou.chatwithwebsocket.adapter.RecyclerViewAdapterGroupApplication
import com.wangyou.chatwithwebsocket.data.GroupApplicationViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupApplicationBinding
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupListBinding
import com.wangyou.chatwithwebsocket.entity.Group
import com.wangyou.chatwithwebsocket.entity.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupApplicationFragment : BaseFragment() {

    private var binding: FragmentGroupApplicationBinding? = null
    private var navController: NavController? = null
    private val groupApplicationViewModel: GroupApplicationViewModel by activityViewModels<GroupApplicationViewModel>()
    private val personalViewModel: PersonalViewModel by activityViewModels<PersonalViewModel>()
    private val listener: RecyclerViewAdapterGroupApplication.OnClickListener = object : RecyclerViewAdapterGroupApplication.OnClickListener{
        override fun agree(gid: Long, uid: Long) {
            Log.i("agree", "${uid}申请加入${gid}")
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

        override fun viewGroupDetail(group: Group) {
            val bundle =
                GroupDetailFragmentArgs.Builder().setGid(group.gid.toString()).build()
                    .toBundle()
            Navigation.findNavController(
                requireActivity(),
                R.id.fragmentAll
            ).navigate(R.id.groupDetailFragment, bundle)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentGroupApplicationBinding>(
            inflater,
            R.layout.fragment_group_application,
            container,
            false
        )
        binding!!.groupApplicationViewModel = groupApplicationViewModel
        binding!!.oneself = personalViewModel.getSelf().value
        binding!!.listener = listener
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResume() {
        super.onActivityResume()
        binding!!.backAddress.setOnClickListener {
            navController!!.popBackStack()
        }
        binding!!.create.setOnClickListener {
            navController!!.navigate(
                R.id.groupEditFragment,
                GroupEditFragmentArgs.Builder().setGid("create").build().toBundle()
            )
        }
        groupApplicationViewModel.getGroupRelationList().observe(requireActivity(), {
            binding!!.rvGroupApplications.adapter?.notifyDataSetChanged()
        })
    }
}