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
import com.wangyou.chatwithwebsocket.data.GroupApplicationViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupApplicationBinding
import com.wangyou.chatwithwebsocket.databinding.FragmentGroupListBinding

class GroupApplicationFragment : Fragment() {

    private var binding: FragmentGroupApplicationBinding? = null
    private var navController: NavController? = null

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
        binding!!.groupApplicationViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(GroupApplicationViewModel::class.java)
        binding!!.oneself = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(PersonalViewModel::class.java).getPersonal().value
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)

        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding!!.backAddress.setOnClickListener {
            navController!!.popBackStack()
        }
    }
}