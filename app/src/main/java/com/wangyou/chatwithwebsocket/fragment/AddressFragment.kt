package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.SearchContentModel
import com.wangyou.chatwithwebsocket.databinding.FragmentAddressBinding

class AddressFragment : Fragment() {

    private var binding: FragmentAddressBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false)
        val searchContentModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(SearchContentModel::class.java)
        binding!!.searchContentModel = searchContentModel
        return binding!!.root
    }

}