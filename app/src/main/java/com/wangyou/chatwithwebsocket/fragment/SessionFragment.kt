package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.SessionViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentSessionBinding

class SessionFragment : Fragment() {

    private var binding: FragmentSessionBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session, container, false)
        binding!!.lifecycleOwner = this
        val sessionViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(SessionViewModel::class.java)
        binding!!.sessionViewModel = sessionViewModel
        return binding!!.root
    }

}