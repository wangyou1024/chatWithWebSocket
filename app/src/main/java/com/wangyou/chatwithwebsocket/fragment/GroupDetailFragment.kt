package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
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
import com.wangyou.chatwithwebsocket.data.GroupDetailViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailFragment : BaseFragment() {

    private var binding: FragmentGroupDetailBinding? = null
    private var navController: NavController? = null
    private val groupDetailViewModel: GroupDetailViewModel by activityViewModels<GroupDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_group_detail, container, false)
        binding!!.lifecycleOwner = this
        binding!!.groupDetailViewModel = groupDetailViewModel
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    override fun onActivityResume() {
        super.onActivityResume()
        val bundle = GroupDetailFragmentArgs.fromBundle(requireArguments())
        groupDetailViewModel.getGroup().value!!.groupName = bundle.gid
        binding!!.popBack.setOnClickListener {
            navController!!.popBackStack()
        }
        binding!!.editGroup.setOnClickListener {
            navController!!.navigate(
                R.id.groupEditFragment,
                GroupEditFragmentArgs.Builder().setGid(bundle.gid).build().toBundle()
            )
        }
        binding!!.groupLeader.setOnClickListener {
            navController!!.navigate(
                R.id.personalDetailFragment, PersonalDetailFragmentArgs.Builder()
                    .setUid(groupDetailViewModel.getGroupLeader().value!!.uid.toString()).build()
                    .toBundle()
            )
        }
    }

}