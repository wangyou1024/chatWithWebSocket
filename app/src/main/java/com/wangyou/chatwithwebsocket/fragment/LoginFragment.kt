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
import com.wangyou.chatwithwebsocket.data.LoginViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding!!.lifecycleOwner = this
        val loginViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(LoginViewModel::class.java)
        loginViewModel.setNavController(Navigation.findNavController(requireActivity(), R.id.fragmentMain))
        binding!!.loginViewModel = loginViewModel
        return binding!!.root
    }
}