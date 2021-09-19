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
import com.wangyou.chatwithwebsocket.adapter.RecyclerViewAdapterGroupList
import com.wangyou.chatwithwebsocket.data.GroupListViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupListBinding
import com.wangyou.chatwithwebsocket.entity.Group
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupListFragment : BaseFragment() {

    private var binding: FragmentGroupListBinding? = null
    private val groupListViewModel: GroupListViewModel by activityViewModels<GroupListViewModel>()
    private val listener: RecyclerViewAdapterGroupList.OnClickListener =
        object : RecyclerViewAdapterGroupList.OnClickListener {
            override fun enterGroupDetail(group: Group) {
                Navigation.findNavController(
                    requireActivity(),
                    R.id.fragmentAll
                ).navigate(
                    R.id.groupDetailFragment,
                    GroupDetailFragmentArgs.Builder().setGid(group.gid.toString()).build()
                        .toBundle()
                )
            }
        }

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
        binding!!.joinedGroupListener = listener
        return binding!!.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResume() {
        super.onActivityResume()
        groupListViewModel.getGroupList().observe(requireActivity(), {
            binding!!.rvJoinedGroupList.adapter?.notifyDataSetChanged()
        })
        groupListViewModel.loadJoinedGroupList()
    }


}