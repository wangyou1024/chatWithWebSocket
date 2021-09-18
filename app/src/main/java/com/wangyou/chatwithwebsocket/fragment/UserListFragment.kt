package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.adapter.RecyclerViewAdapterUserList
import com.wangyou.chatwithwebsocket.data.UserListViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentUserListBinding
import com.wangyou.chatwithwebsocket.entity.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : BaseFragment() {

    private var binding: FragmentUserListBinding? = null
    private var navController: NavController? = null

    private val userListViewModel: UserListViewModel by activityViewModels<UserListViewModel>()
    private val listener: RecyclerViewAdapterUserList.OnClickListener = object : RecyclerViewAdapterUserList.OnClickListener{
        override fun viewDetailPerson(user: User) {
            val bundle =
                PersonalDetailFragmentArgs.Builder().setUid(user.uid.toString()).build()
                    .toBundle()
            navController?.navigate(R.id.personalDetailFragment, bundle)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentUserListBinding>(
            inflater,
            R.layout.fragment_user_list,
            container,
            false
        )
        binding!!.lifecycleOwner = this
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        binding!!.listener = listener
        binding!!.userListViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(UserListViewModel::class.java)
        return binding!!.root
    }

    override fun onCreated() {
        super.onCreated()
        userListViewModel.findFriends()
    }

}