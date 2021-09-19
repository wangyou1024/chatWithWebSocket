package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.GroupDetailViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentPersonalDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PersonalDetailFragment : BaseFragment() {

    @Inject
    lateinit var toast: Toast
    private var binding: FragmentPersonalDetailBinding? = null
    private val personalViewModel: PersonalViewModel by activityViewModels<PersonalViewModel>()
    private val groupDetailViewModel: GroupDetailViewModel by activityViewModels<GroupDetailViewModel>()
    private var navController: NavController? = null

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
        binding!!.personalViewModel = personalViewModel
        binding!!.groupDetailViewModel = groupDetailViewModel
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    override fun onActivityResume() {
        super.onActivityResume()
        val bundle = PersonalDetailFragmentArgs.fromBundle(requireArguments())
        if (bundle.uid != "unknown") {
            personalViewModel.loadUserById(bundle.uid.toLong())
        } else {
            personalViewModel.loadSelf()
        }
        if (bundle.gid != "unknown") {
            personalViewModel.setGid(bundle.gid.toLong())
        } else {
            personalViewModel.setGid(0)
        }
        binding!!.popBack.setOnClickListener {
            navController!!.popBackStack()
        }
    }
}