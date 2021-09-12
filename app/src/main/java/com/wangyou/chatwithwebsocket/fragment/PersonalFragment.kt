package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.FragmentPersonalBinding
import com.wangyou.chatwithwebsocket.data.PersonalViewModel


class PersonalFragment : Fragment() {

    var binding: FragmentPersonalBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal, container, false)
        binding!!.lifecycleOwner = this
        binding!!.personalViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(PersonalViewModel::class.java)
        binding!!.editPersonal.setOnClickListener{
            Navigation.findNavController(requireActivity(), R.id.fragmentAll).navigate(R.id.personalDetailFragment)
        }
        return binding!!.root
    }

}