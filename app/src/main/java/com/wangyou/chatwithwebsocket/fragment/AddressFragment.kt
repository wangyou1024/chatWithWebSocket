package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.MainUIViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment : BaseFragment() {

    private var binding: FragmentAddressBinding? = null
    private var navController: NavController? = null
    private val mainUIViewModel: MainUIViewModel by activityViewModels<MainUIViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false)
        binding!!.lifecycleOwner = this

        navController =
            Navigation.findNavController(binding!!.root.findViewById(R.id.addressListFragment))
        return binding!!.root
    }

    override fun onActivityResume() {
        super.onActivityResume()
        // 初始化选项
        changeStatusSelected()
        binding!!.friendList.setOnClickListener { enterFriendList() }
        binding!!.groupList.setOnClickListener { enterGroupList() }
        binding!!.searchButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragmentAll).navigate(R.id.searchFragment)
        }
        binding!!.friendApplication.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragmentAll).navigate(R.id.friendApplicationFragment)
        }
        binding!!.groupApplication.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragmentAll).navigate(R.id.groupApplicationFragment)
        }
    }

    private fun enterFriendList(){
        if (!navController!!.popBackStack(R.id.userListFragment, false)) {
            mainUIViewModel.setAddress(0)
            changeStatusSelected()
        }
    }

    private fun enterGroupList(){
        if (!navController!!.popBackStack(R.id.groupListFragment, false)) {
            mainUIViewModel.setAddress(1)
            changeStatusSelected()
        }
    }

    private fun changeStatusSelected(){
        var selected : TextView? = null
        var unselected : TextView? = null
        if (mainUIViewModel.getAddress() == 0){
            selected = binding!!.friendList
            unselected = binding!!.groupList
            navController!!.navigate(R.id.userListFragment)
        } else {
            selected = binding!!.groupList
            unselected = binding!!.friendList
            navController!!.navigate(R.id.groupListFragment)
        }
        selected.setTextColor(resources.getColor(R.color.gray_800, null))
        selected.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_title))
        unselected.setTextColor(resources.getColor(R.color.gray_500, null))
        unselected.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_content))
    }

}