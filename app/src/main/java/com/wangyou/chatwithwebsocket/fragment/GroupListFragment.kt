package com.wangyou.chatwithwebsocket.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.GroupListViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupListFragment : BaseFragment() {

    private var binding: FragmentGroupListBinding? = null
    private val groupListViewModel: GroupListViewModel by activityViewModels<GroupListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_list, container, false)
        binding!!.lifecycleOwner = this
        binding!!.groupListViewModel = groupListViewModel
        return binding!!.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResume() {
        super.onActivityResume()
        groupListViewModel.getGroupList().observe(requireActivity(),{
            binding!!.rvJoinedGroupList.adapter?.notifyDataSetChanged()
        })
        groupListViewModel.loadJoinedGroupList()
    }



}