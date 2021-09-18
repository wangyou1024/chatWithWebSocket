package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.LoginViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentPersonalBinding
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalFragment : BaseFragment() {

    var binding: FragmentPersonalBinding? = null
    private var navController: NavController? = null

    private val personalViewModel by activityViewModels<PersonalViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal, container, false)
        binding!!.lifecycleOwner = this
        binding!!.personalViewModel = personalViewModel
        personalViewModel.loadUser(loginViewModel.getUsername())
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    override fun onActivityResume() {
        super.onActivityResume()
        binding!!.editPersonal.setOnClickListener{
            navController!!.navigate(R.id.personalDetailFragment)
        }
    }

}