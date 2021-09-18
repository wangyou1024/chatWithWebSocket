package com.wangyou.chatwithwebsocket.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.adapter.RecyclerViewAdapterUserList
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.GroupListViewModel
import com.wangyou.chatwithwebsocket.data.SearchContentViewModel
import com.wangyou.chatwithwebsocket.data.UserListViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentSearchBinding
import com.wangyou.chatwithwebsocket.entity.User
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private var binding: FragmentSearchBinding? = null
    private var navController: NavController? = null
    private val userListViewModel by activityViewModels<UserListViewModel>()
    private val groupListViewModel by activityViewModels<GroupListViewModel>()
    private val searchContentViewModel by activityViewModels<SearchContentViewModel>()
    private val userClick: RecyclerViewAdapterUserList.OnClickListener =
        object : RecyclerViewAdapterUserList.OnClickListener {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding?.lifecycleOwner = this
        binding?.userListViewModel = userListViewModel
        binding?.groupListViewModel = groupListViewModel
        binding?.searchContentViewModel = searchContentViewModel
        binding?.onClickListener = userClick
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentAll)
        return binding!!.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResume() {
        super.onActivityResume()
        binding?.backMain?.setOnClickListener {
            navController?.popBackStack()
        }
        // 关闭键盘
        binding?.searchContent?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i("search", binding!!.searchContentViewModel?.getSearchContent()!!)
                    userListViewModel.searchUserList(searchContentViewModel.getSearchContent())
                    groupListViewModel.searchGroupList(searchContentViewModel.getSearchContent())
                    val manager: InputMethodManager =
                        context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(
                        view!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    binding!!.searchContent.clearFocus()
                    return true
                }
                return false
            }
        })
        userListViewModel.getUserList().observe(requireActivity(), {
            binding?.userList?.adapter?.notifyDataSetChanged()
        })
        groupListViewModel.getGroupList().observe(requireActivity(), {
            binding?.groupList?.adapter?.notifyDataSetChanged()
        })

        binding?.userList?.adapter?.notifyDataSetChanged()
    }

}