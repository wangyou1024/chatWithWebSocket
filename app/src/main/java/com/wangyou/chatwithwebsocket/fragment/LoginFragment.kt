package com.wangyou.chatwithwebsocket.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.LoginViewModel
import com.wangyou.chatwithwebsocket.data.MainUIViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentLoginBinding
import com.wangyou.chatwithwebsocket.net.api.LoginAPI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    @Inject
    lateinit var loginAPI: LoginAPI

    private val loginViewModel by viewModels<LoginViewModel>()

    private var binding: FragmentLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Toast
        // SwipeRefreshLayout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding!!.lifecycleOwner = this

        loginViewModel.setNavController(
            Navigation.findNavController(
                requireActivity(),
                R.id.fragmentAll
            )
        )

        loginViewModel.isLogining().observe(viewLifecycleOwner, {
            if (it){
                binding!!.avi.show()
            } else {
                binding!!.avi.hide()
            }
        })
        binding!!.loginViewModel = loginViewModel
        return binding!!.root
    }
}