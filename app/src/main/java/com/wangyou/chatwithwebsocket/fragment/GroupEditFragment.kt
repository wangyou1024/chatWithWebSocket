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
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupEditFragment : BaseFragment() {

    private var binding: FragmentGroupEditBinding? = null
    private var navController: NavController? = null
    private val groupDetailViewModel: GroupDetailViewModel by activityViewModels<GroupDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_edit, container, false)
        binding!!.lifecycleOwner = this
        binding!!.groupDetailViewModel = groupDetailViewModel
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    override fun onActivityResume() {
        super.onActivityResume()
        val bundle = GroupEditFragmentArgs.fromBundle(requireArguments())
        if (bundle.gid == "create"){
            groupDetailViewModel.createGroup()
        }
        binding!!.popBack.setOnClickListener {
            navController!!.popBackStack()
        }
    }

}