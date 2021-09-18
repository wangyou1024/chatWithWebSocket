package com.wangyou.chatwithwebsocket.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.LoginViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentLoginBinding
import com.wangyou.chatwithwebsocket.net.api.LoginServiceAPI
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val personalViewModel by activityViewModels<PersonalViewModel>()

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
        loginViewModel.isLogining().observe(requireActivity(), {
            if (it) {
                binding!!.avi.show()
            } else {
                binding!!.avi.hide()
            }
        })
        binding!!.loginViewModel = loginViewModel
        return binding!!.root
    }
}