package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.GroupDetailViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupDetailBinding


class GroupDetailFragment : Fragment() {

    private var binding: FragmentGroupDetailBinding? = null
    private var groupDetailViewModel: GroupDetailViewModel? = null
    private var navController: NavController? = null

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
        groupDetailViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(GroupDetailViewModel::class.java)
        binding!!.groupDetailViewModel = groupDetailViewModel
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = GroupDetailFragmentArgs.fromBundle(requireArguments())
        groupDetailViewModel!!.getGroup().value!!.groupName = bundle.gid
        binding!!.popBack.setOnClickListener {
            navController!!.popBackStack()
        }
        binding!!.editGroup.setOnClickListener {
            navController!!.navigate(R.id.groupEditFragment)
        }
        binding!!.groupLeader.setOnClickListener {
            navController!!.navigate(
                R.id.personalDetailFragment, PersonalDetailFragmentArgs.Builder()
                    .setUid(groupDetailViewModel!!.getGroupLeader().value!!.uid.toString()).build()
                    .toBundle()
            )
        }
    }

}