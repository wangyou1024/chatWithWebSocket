package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.GroupListViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupListBinding

class GroupListFragment : Fragment() {

    private var binding: FragmentGroupListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_list, container, false)
        binding!!.lifecycleOwner = this
        binding!!.groupListViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(GroupListViewModel::class.java)
        return binding!!.root
    }

}