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
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupEditBinding

class GroupEditFragment : Fragment() {

    private var binding: FragmentGroupEditBinding? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_edit, container, false)
        binding!!.lifecycleOwner = this
        binding!!.groupDetailViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(GroupDetailViewModel::class.java)
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)

        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding!!.popBack.setOnClickListener {
            navController!!.popBackStack()
        }
    }



}