package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.GroupDetailViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentPersonalDetailBinding

class PersonalDetailFragment : Fragment() {

    private var binding: FragmentPersonalDetailBinding? = null
    private var personalViewModel: PersonalViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_personal_detail, container, false)
        binding!!.lifecycleOwner = this
        personalViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(
            PersonalViewModel::class.java
        )
        binding!!.personalViewModel = personalViewModel
        binding!!.groupDetailViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(GroupDetailViewModel::class.java)
        binding!!.popBack.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragmentAll).popBackStack()
        }
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = PersonalDetailFragmentArgs.fromBundle(requireArguments())
        personalViewModel!!.username = MutableLiveData(bundle.uid)
    }
}