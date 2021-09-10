package com.wangyou.chatwithwebsocket.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.GroupListViewModel
import com.wangyou.chatwithwebsocket.data.SearchContentViewModel
import com.wangyou.chatwithwebsocket.data.UserListViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding!!.lifecycleOwner = this
        binding!!.backMain.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragmentAll).popBackStack()
        }
        binding!!.userListViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(UserListViewModel::class.java)
        binding!!.groupListViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(GroupListViewModel::class.java)
        binding!!.searchContentViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(SearchContentViewModel::class.java)
        binding!!.searchContent.setOnEditorActionListener(object :TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    Log.i("search", binding!!.searchContentViewModel?.getSearchContent()!!)
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
        return binding!!.root
    }

}