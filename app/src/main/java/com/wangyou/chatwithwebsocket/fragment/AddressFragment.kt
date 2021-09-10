package com.wangyou.chatwithwebsocket.fragment

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.FragmentAddressBinding

class AddressFragment : Fragment() {

    private var binding: FragmentAddressBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false)
        binding!!.lifecycleOwner = this

        val navController =
            Navigation.findNavController(binding!!.root.findViewById(R.id.addressListFragment))
        binding!!.friendList.setOnClickListener {
            if (!navController.popBackStack(R.id.userListFragment, false)) {
                changeStatusSelected(binding!!.friendList, binding!!.groupList)
                navController.navigate(R.id.userListFragment)
            }
        }
        binding!!.groupList.setOnClickListener {
            if (!navController.popBackStack(R.id.groupListFragment, false)) {
                changeStatusSelected(binding!!.groupList, binding!!.friendList)
                navController.navigate(R.id.groupListFragment)
            }
        }
        binding!!.searchButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragmentAll).navigate(R.id.searchFragment)
        }
        return binding!!.root
    }

    private fun changeStatusSelected(selected: TextView?, unselected: TextView?){
        selected!!.setTextColor(resources.getColor(R.color.gray_800, null))
        selected.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_title))
        unselected!!.setTextColor(resources.getColor(R.color.gray_500, null))
        unselected.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_content))
    }

}